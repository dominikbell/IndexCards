package com.example.indexcards.ui.box.dialogs

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.indexcards.R
import com.example.indexcards.data.LanguageData
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.box.TagList
import com.example.indexcards.ui.elements.MeaningField
import com.example.indexcards.ui.elements.NewTagButton
import com.example.indexcards.ui.elements.NotesField
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.WordField
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.emptyBox
import com.example.indexcards.utils.card.CardDetails
import com.example.indexcards.utils.card.CardState
import com.example.indexcards.utils.card.UiCardWithTags
import com.example.indexcards.utils.card.emptyCard
import com.example.indexcards.utils.card.toCardDetails
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.recording.AndroidAudioRecorder
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun NewCardDialog(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    boxWithTags: UiBoxWithTags,
    cardId: Long,
    audioPlayer: AndroidAudioPlayer,
    audioRecorder: AndroidAudioRecorder,
    hasRecordingPermission: Boolean = false,
    requestRecordingPermission: () -> Boolean = { false },
    updateUiState: (CardDetails) -> Unit = {},
    onDismiss: () -> Unit = {},
    saveCard: () -> Unit = {},
    onTagClick: (Tag) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
) {
    CardDialogBody(
        titleText = stringResource(id = R.string.add_new_card),
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        cardId = cardId,
        deleteButton = false,
        hasRecordingPermission = hasRecordingPermission,
        requestRecordingPermission = requestRecordingPermission,
        audioPlayer = audioPlayer,
        audioRecorder = audioRecorder,
        onDismiss = onDismiss,
        onSave = saveCard,
        updateUiState = updateUiState,
        onTagClick = onTagClick,
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = showEditTagDialog,
    )
}

@Preview
@Composable
fun NewCardDialogPreview() {
    NewCardDialog(
        cardUiState = CardState(
            cardDetails = emptyCard.copy(
                word = "New Card",
                meaning = "Bedeutung12",
                notes = "Notizen dazu",
            ).toCardDetails()
        ),
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Box123", topic = "English"),
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        cardId = -1,
        audioPlayer = AndroidAudioPlayer(LocalContext.current),
        audioRecorder = AndroidAudioRecorder(LocalContext.current),
    )
}

@Composable
fun EditCardDialog(
    modifier: Modifier = Modifier,
    boxWithTags: UiBoxWithTags,
    cardWithTags: UiCardWithTags,
    cardUiState: CardState,
    audioPlayer: AndroidAudioPlayer,
    audioRecorder: AndroidAudioRecorder,
    hasRecordingPermission: Boolean = false,
    requestRecordingPermission: () -> Boolean = { false },
    onDismiss: () -> Unit = {},
    onDeleteCard: () -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
    updateUiState: (CardDetails) -> Unit = {},
    saveCard: () -> Unit = {},
    clickOnTag: (Tag) -> Unit = {},
) {
    val titleText = stringResource(id = R.string.edit_card) + " " + cardWithTags.card.word

    CardDialogBody(
        titleText = titleText,
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        cardId = cardWithTags.card.cardId,
        deleteButton = true,
        audioPlayer = audioPlayer,
        audioRecorder = audioRecorder,
        hasRecordingPermission = hasRecordingPermission,
        requestRecordingPermission = requestRecordingPermission,
        onDismiss = onDismiss,
        onSave = saveCard,
        onDelete = onDeleteCard,
        updateUiState = updateUiState,
        onTagClick = clickOnTag,
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = { showEditTagDialog(it) },
    )
}

@Preview
@Composable
fun EditCardDialogPreview() {
    EditCardDialog(
        cardUiState = CardState(
            cardDetails = emptyCard.copy(
                word = "New Name",
                meaning = "Bedeutung12",
                notes = "Notizen dazu",
                memoURI = "notzero"
            ).toCardDetails()
        ),
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Box123"),
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        cardWithTags = UiCardWithTags(
            card = emptyCard.copy(word = "OldName")
        ),
        audioPlayer = AndroidAudioPlayer(LocalContext.current),
        audioRecorder = AndroidAudioRecorder(LocalContext.current),
    )
}

@Composable
fun CardDialogBody(
    modifier: Modifier = Modifier,
    titleText: String,
    cardUiState: CardState,
    boxWithTags: UiBoxWithTags,
    cardId: Long,
    deleteButton: Boolean,
    audioPlayer: AndroidAudioPlayer,
    audioRecorder: AndroidAudioRecorder,
    hasRecordingPermission: Boolean = false,
    requestRecordingPermission: () -> Boolean = { false },
    onDismiss: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
    updateUiState: (CardDetails) -> Unit = {},
    onTagClick: (Tag) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
) {
    val applicationContext = LocalContext.current.applicationContext

    val mmr = MediaMetadataRetriever()

    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    var audioFile: File? by remember { mutableStateOf(null) }

    var duration by remember { mutableLongStateOf(0) }

    LaunchedEffect(key1 = cardUiState.cardDetails.memoURI) {
        if (cardUiState.cardDetails.memoURI.isNotBlank()) {
            audioFile = cardUiState.cardDetails.memoURI.toUri().path?.let { File(it) }

            duration =
                audioFile?.let {
                    mmr.setDataSource(it.toUri().path)
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.let { durInt ->
                            Integer.parseInt(durInt).toLong()
                        }
                } ?: 0
        }
    }

    LaunchedEffect(key1 = isPlaying, key2 = duration) {
        if (isPlaying) {
            delay(duration)
            isPlaying = false
        }
    }

    fun onRecord() {
        if (hasRecordingPermission) {
            File(
                applicationContext.filesDir,
                "memo_temp.mp3"
            ).also {
                audioRecorder.start(it)
                audioFile = it
            }

            isRecording = true
        }
    }

    fun onStopRecording() {
        audioRecorder.stop()

        duration =
            audioFile?.let {
                mmr.setDataSource(it.toUri().path)
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.let { durInt ->
                    Integer.parseInt(durInt).toLong()
                }
            } ?: 0

        isRecording = false
    }

    fun onPlay() {
        audioFile?.let {
            audioPlayer.playFile(it)
            isPlaying = true
        }
    }

    fun onStopPlaying() {
        isPlaying = false
        audioPlayer.stop()
    }

    fun stopEverything() {
        if (isPlaying) {
            onStopPlaying()
        }
        if (isRecording) {
            onStopRecording()
        }
    }

    fun onClickDelete() {
        stopEverything()
        audioFile?.delete()
        audioFile = null
        updateUiState(cardUiState.cardDetails.copy(memoURI = ""))
    }

    fun onClickSave() {
        stopEverything()
        audioFile?.let { temp ->
            File(
                applicationContext.filesDir,
                "memo$cardId.mp3"
            ).also {
                if (it.exists()) {
                    it.delete()
                }
                temp.copyTo(it)
                updateUiState(cardUiState.cardDetails.copy(memoURI = it.toURI().toString()))
            }
        }
        onSave()
    }

    fun onCancel() {
        stopEverything()
        onDismiss()
    }

    AlertDialog(modifier = modifier,
        onDismissRequest = { onCancel() },
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier
            ) {
                WordField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(word = it)) }
                )

                MeaningField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(meaning = it)) },
                    isLanguage = (LanguageData.language.values.contains(boxWithTags.box.topic))
                )

                RequiredFieldsText()

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TagList(
                        modifier = modifier.weight(1f),
                        tagList = boxWithTags.tagList,
                        onClick = { onTagClick(it) },
                        onLongClick = { showEditTagDialog(it) },
                        selectedTags = cardUiState.tagList
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(start = 3.dp, end = 3.dp)
                    )

                    NewTagButton(
                        onClick = {
                            stopEverything()
                            showNewTagDialog()
                        },
                        short = true
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.memo) + ": ")

                    if (isRecording) {
                        IconButton(
                            onClick = { onStopRecording() }
                        ) { Icon(imageVector = Icons.Default.Stop, contentDescription = "stop") }
                    } else {
                        IconButton(
                            onClick = {
                                Log.d("cardId", cardId.toString())
                                if (!hasRecordingPermission) {
                                    val success = requestRecordingPermission()
                                    if (success) {
                                        onRecord()
                                    }
                                } else {
                                    onRecord()
                                }
                            }
                        ) { Icon(imageVector = Icons.Default.Mic, contentDescription = "record") }
                    }

                    if (!isRecording && (audioFile?.exists() == true)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VerticalDivider(Modifier.height(18.dp))

                            if (isPlaying) {
                                IconButton(
                                    onClick = { onStopPlaying() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Pause,
                                        contentDescription = "stop"
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { onPlay() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "play"
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { onClickDelete() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete"
                            )
                        }
                    }
                }

                NotesField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(notes = it)) }
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = { onClickSave() }
            ) {
                Text(text = stringResource(R.string.save))
            }
        },

        dismissButton = {
            Row {
                TextButton(
                    onClick = { onCancel() }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                if (deleteButton) {
                    TextButton(
                        onClick = { onDelete() }
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                }
            }
        }
    )
}