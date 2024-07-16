package com.example.indexcards.ui.box.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.recording.AndroidAudioRecorder
import java.io.File
import java.io.FileOutputStream

@Composable
fun RecordingDialog(
    modifier: Modifier = Modifier,
    cardId: Long,
    onDismiss: () -> Unit = {},
    audioPlayer: AndroidAudioPlayer,
    audioRecorder: AndroidAudioRecorder,
    saveMemo: (String) -> Unit = {},
) {
    val applicationContext = LocalContext.current.applicationContext

    var audioFile: File? = null

    fun writeFile(cardId: Long = 0): String {
        val recordedFile = File(applicationContext.filesDir, "memo$cardId.mp3")

        FileOutputStream(recordedFile).use {
            if (audioFile != null) {
                it.write(audioFile!!.readBytes())
            }
        }
        return recordedFile.toURI().toString()
    }

    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Create a new recording")
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (audioFile == null) {
                    IconButton(
                        onClick = {
                            File(applicationContext.filesDir, "audio_temp.mp3").also {
                                audioRecorder.start(it)
                                audioFile = it
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Record"
                        )
                    }

                    IconButton(
                        onClick = { audioRecorder.stop() }
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop")
                    }
                } else {
                    IconButton(
                        onClick = {
                            audioPlayer.playFile(audioFile ?: return@IconButton)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "PLay")
                    }

                    IconButton(
                        onClick = {
                            audioPlayer.stop()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "StopPlaying")
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }

                TextButton(
                    onClick = {  }
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }

                TextButton(
                    onClick = {
                        val resultURI = writeFile(cardId = cardId)
                        saveMemo(resultURI)
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    )
}

@Preview
@Composable
fun RecordingDialogPreview() {
    RecordingDialog(
        cardId = -1,
        audioPlayer = AndroidAudioPlayer(LocalContext.current),
        audioRecorder = AndroidAudioRecorder(LocalContext.current),
    )
}