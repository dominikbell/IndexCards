package com.example.indexcards

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.Card
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.ui.theme.IndexCardsTheme
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.home.HomeScreenViewModel
import com.example.indexcards.utils.notification.NOTIFICATION_REQUEST_CODES
import com.example.indexcards.utils.notification.NotificationRequest
import com.example.indexcards.utils.notification.NotificationService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

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
            var doneReading by remember { mutableStateOf(false) }
            var csvBytes = ByteArray(0)
            var fileString by remember { mutableStateOf("") }
            var isCSVFile by remember { mutableStateOf(false) }
            val mustBeCSVToast = stringResource(id = R.string.must_be_csv)

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

            var hasRecordingPermission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }

            val notificationLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                }
            )

            val recordingLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasRecordingPermission = isGranted
                }
            )

            fun requestNotificationPermission(): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                return true
            }

            fun requestRecordingPermission(): Boolean {
                recordingLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return true
            }

            val createFileLauncher = rememberLauncherForActivityResult(
                contract = CreateDocument(mimeType = "document/csv"),
                onResult = { uri ->
                    uri?.let {
                        contentResolver.openOutputStream(uri).use {
                            it?.write(csvBytes)
                        }
                    }
                }
            )

            val readFileLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri ->
                    doneReading = false
                    uri?.let {
                        contentResolver.query(uri, null, null, null, null)
                            ?.use { cursor ->
                                val fileNameIndex =
                                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                cursor.moveToFirst()
                                val fileName = cursor.getString(fileNameIndex)
                                isCSVFile = fileName.split(".").last() == "csv"
                            }
                        contentResolver.openInputStream(uri).use { inputStream ->
                            inputStream?.let {
                                fileString = ""
                                inputStream.bufferedReader(Charsets.UTF_8).forEachLine {
                                    fileString += it
                                    fileString += "\n"
                                }
                                doneReading = true
                            }
                        }
                    }
                }
            )

            fun saveFile(file: ByteArray, name: String) {
                csvBytes = file
                createFileLauncher.launch(name)
            }

            fun importBox() {
                readFileLauncher.launch(arrayOf("*/*"))
            }

            LaunchedEffect(key1 = doneReading) {
                if (doneReading) {
                    if (isCSVFile) {
                        homeScreenViewModel.importBox(fileString)
                        isCSVFile = false
                    } else {
                        Toast.makeText(context, mustBeCSVToast, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            fun deleteAllMemos(cards: List<Card>) {
                cards.forEach {
                    val audioFile = File(
                        applicationContext.filesDir,
                        "memo${it.cardId}.mp3"
                    )
                    if (audioFile.exists()) {
                        audioFile.delete()
                    }
                }
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
                        hasNotificationPermission = hasNotificationPermission,
                        hasRecordingPermission = hasRecordingPermission,
                        saveFile = { file, name -> saveFile(file, name) },
                        importBox = { importBox() },
                        requestNotificationPermission = { requestNotificationPermission() },
                        requestRecordingPermission = { requestRecordingPermission() },
                        deleteAllMemos = { deleteAllMemos(it) },
                        cancelAllNotifications = { cancelAllNotifications() },
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