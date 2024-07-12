package com.example.indexcards

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.ui.theme.IndexCardsTheme
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.home.HomeScreenViewModel
import com.example.indexcards.utils.notification.NOTIFICATION_REQUEST_CODES
import com.example.indexcards.utils.notification.NotificationRequest
import com.example.indexcards.utils.notification.NotificationService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // All are needed for cancelling the old notification
        val requestId = intent.getIntExtra("id", -1)
        val boxId = intent.getLongExtra("boxId", -1)
        val level = intent.getIntExtra("level", -1)

        val boxIdPass = if (requestId in listOf(
                NotificationRequest.GO_TO_BOX,
                NotificationRequest.GO_TO_TRAINING
            )
        ) {
            boxId
        } else {
            (-1).toLong()
        }
        val levelPass = if (requestId == NotificationRequest.GO_TO_TRAINING) {
            level
        } else {
            -1
        }

        val notificationChannel = NotificationChannel(
            NotificationService.CHANNEL_ID,
            "IndexCards",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.description = "Reminds you of your training"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        setContent {
            val context = LocalContext.current
            val service = NotificationService(applicationContext)

            if (NOTIFICATION_REQUEST_CODES.contains(requestId)) {
                service.closeNotification(boxId, level, 0)
            }

            val appRepository = OfflineAppRepository(AppDatabase.getDatabase(context).appDao())

            fun cancelAllNotifications() {
                lifecycleScope.launch {
                    val boxList = appRepository.getAllBoxesStream().first()
                    for (box in boxList) {
                        for (lvl in 0..4) {
                            service.closeNotification(boxId = box.boxId, level = lvl, 0)
                        }
                    }
                }
            }

            val homeScreenViewModel: HomeScreenViewModel = viewModel(
                factory = ViewModelProvider(context = LocalContext.current).factory
            )

            var hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                } else mutableStateOf(true)
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                }
            )

            fun requestNotificationPermission(): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                return true
            }

            IndexCardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        homeScreenViewModel = homeScreenViewModel,
                        startBoxId = boxIdPass,
                        startLevel = levelPass,
                        cancelAllNotifications = { cancelAllNotifications() },
                        hasNotificationPermission = hasNotificationPermission,
                        requestNotificationPermission = { requestNotificationPermission() },
                        scheduleNotification = { boxId, level, name, time ->
                            service.scheduleNotification(
                                boxId = boxId, level = level,
                                boxName = name, time = time
                            )
                        },
                    )
                }
            }
        }
    }
}

