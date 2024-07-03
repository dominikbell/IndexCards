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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.theme.IndexCardsTheme
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.notification.NotificationRequest
import com.example.indexcards.utils.notification.NotificationService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestId = intent.getIntExtra("id", -1)
        val boxId = intent.getLongExtra("boxId", -1)
        val level = intent.getIntExtra("level", -1)

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

            when (requestId) {
                NotificationRequest.GO_TO_APP -> { service.closeNotification(boxId, level) }
                NotificationRequest.GO_TO_BOX -> { service.closeNotification(boxId, level) }
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

            fun requestNotificationPermission() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            IndexCardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        homeScreenViewModel = homeScreenViewModel,
                        hasNotificationPermission = hasNotificationPermission,
                        requestNotificationPermission = { requestNotificationPermission() },
                        scheduleNotification = {
                            service.scheduleNotification(
                                boxId = 1, level = 0
                            )
                        },
                        startBoxId = boxId,
                        startLevel = level
                    )
                }
            }
        }
    }
}

