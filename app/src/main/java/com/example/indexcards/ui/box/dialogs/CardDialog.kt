package com.example.indexcards.ui.box.dialogs

import android.media.MediaMetadataRetriever
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.ui.box.TagList
import com.example.indexcards.utils.state.UiCardWithTags
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.state.emptyTag
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun CardDialog(
    modifier: Modifier = Modifier,
    cardWithTags: UiCardWithTags,
    isEditing: Boolean,
    audioPlayer: AndroidAudioPlayer,
    onDismiss: () -> Unit = {},
    showEditCardDialog: () -> Unit = {},
    showDelete: (Card) -> Unit = {},
) {
    var isPlaying by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0) }
    var audioFile: File? by remember { mutableStateOf(null) }
    val mmr = MediaMetadataRetriever()

    LaunchedEffect(key1 = cardWithTags.card.memoURI) {
        if (cardWithTags.card.memoURI.isNotBlank()) {
            audioFile = cardWithTags.card.memoURI.toUri().path?.let { File(it) }

            duration =
                audioFile?.let {
                    mmr.setDataSource(it.toUri().path)
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.let { durInt ->
                            Integer.parseInt(durInt).toLong()
                        }
                } ?: 0
        } else {
            audioFile = null
            duration = 0
        }
    }

    LaunchedEffect(key1 = isPlaying) {
        if (isPlaying) {
            delay(duration)
            isPlaying = false
        }
    }

    fun playAudio() {
        audioFile?.let {
            audioPlayer.playFile(it)
            isPlaying = true
        }
    }

    fun stopPlaying() {
        isPlaying = false
        audioPlayer.stop()
    }

    Dialog(
        onDismissRequest = {
            if (!isEditing) {
                if (isPlaying) {
                    stopPlaying()
                }
                onDismiss()
            }
        }
    ) {
        Surface(
            modifier = modifier
                .height(300.dp),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SelectionContainer(
                            modifier = modifier.weight(1f),
                        ) {
                            Text(
                                text = cardWithTags.card.word,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        IconButton(
                            onClick = {
                                stopPlaying()
                                showEditCardDialog()
                            }
                        ) {
                            Icon(
                                Icons.Default.Create,
                                modifier = Modifier.size(MaterialTheme.typography.titleLarge.fontSize.value.dp),
                                contentDescription = "Edit",
                            )
                        }
                    }

                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SelectionContainer {
                            Text(
                                modifier = modifier.fillMaxWidth(),
                                text = cardWithTags.card.meaning,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }

                        Spacer(modifier = modifier.size(8.dp))

                        TagList(
                            tagList = cardWithTags.tagList,
                            onClick = {},
                            onLongClick = {},
                            selectedTags = cardWithTags.tagList
                        )

                        Spacer(modifier = modifier.size(4.dp))

                        if (audioFile?.exists() == true) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = stringResource(id = R.string.memo) + ": ")

                                if (isPlaying) {
                                    IconButton(
                                        onClick = { stopPlaying() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Pause,
                                            contentDescription = "pause"
                                        )
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            playAudio()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "play"
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = stringResource(id = R.string.no_memo),
                                fontStyle = FontStyle.Italic,
                            )
                        }


                        Spacer(modifier = modifier.size(4.dp))

                        if (cardWithTags.card.notes.isNotBlank()) {
                            Row {
                                Text(
                                    text = stringResource(R.string.notes) + ": ",
                                    fontStyle = FontStyle.Italic
                                )
                                Text(text = cardWithTags.card.notes)
                            }
                        }
                    }
                }

                IconButton(
                    modifier = modifier
                        .align(Alignment.End),
                    onClick = {
                        stopPlaying()
                        showDelete(cardWithTags.card)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardDialogPreview() {
    CardDialog(
        cardWithTags = UiCardWithTags(
            card = emptyCard.copy(
                word = "Kartonage",
                meaning = "huch, upsi",
                notes = "Diese Karte erklärt dir genau was schiefgelaufen ist als du damals die falsche Milch gekauft hast",
                memoURI = "not0"
            ),
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "nächster Tag"),
                emptyTag.copy(tagId = 3, text = "übermorgen"),
            )
        ),
        isEditing = false,
        audioPlayer = AndroidAudioPlayer(LocalContext.current)
    )
}