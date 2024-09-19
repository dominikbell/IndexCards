package com.example.indexcards.ui.box.dialogs

import android.media.MediaMetadataRetriever
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.indexcards.R
import com.example.indexcards.data.Category
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.box.TagList
import com.example.indexcards.ui.elements.CategoriesDropDownMenu
import com.example.indexcards.ui.elements.CustomDialog
import com.example.indexcards.ui.elements.MeaningField
import com.example.indexcards.ui.elements.NewTagButton
import com.example.indexcards.ui.elements.NotesField
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.WordField
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.home.TutorialState
import com.example.indexcards.utils.state.CardDetails
import com.example.indexcards.utils.state.CardState
import com.example.indexcards.utils.state.UiCardWithTags
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.toCardDetails
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.recording.AndroidAudioRecorder
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.emptyCategory
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.isLanguage
import com.example.indexcards.utils.state.toBox
import kotlinx.coroutines.delay
import java.io.File


@Composable
fun NewCardDialog(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    boxWithTags: UiBoxWithTags,
    boxWithCategories: UiBoxWithCategories,
    tutorial: Boolean,
    tutorialState: TutorialState,
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
    nextTutorialStep: () -> Unit = {},
    endTutorial: () -> Unit = {},
) {
    CardDialogBody(
        titleText = stringResource(id = R.string.add_new_card),
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        boxWithCategories = boxWithCategories,
        tutorial = tutorial,
        tutorialState = tutorialState,
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
        nextTutorialStep = nextTutorialStep,
        endTutorial = endTutorial,
    )
}

@Preview
@Composable
fun NewCardDialogPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text"
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

    NewCardDialog(
        cardUiState = CardState(
            cardDetails = emptyCard.copy(
                word = "New Card",
                meaning = "Bedeutung12",
                notes = "Notizen dazu",
            ).toCardDetails()
        ),
        boxWithTags = UiBoxWithTags(
            box = box,
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        boxWithCategories = boxWithCategories,
        tutorial = false,
        tutorialState = TutorialState.OFF,
        cardId = -1,
        audioPlayer = AndroidAudioPlayer(LocalContext.current),
        audioRecorder = AndroidAudioRecorder(LocalContext.current),
    )
}

@Composable
fun EditCardDialog(
    modifier: Modifier = Modifier,
    boxWithTags: UiBoxWithTags,
    boxWithCategories: UiBoxWithCategories,
    cardWithTags: UiCardWithTags,
    cardUiState: CardState,
    tutorial: Boolean,
    tutorialState: TutorialState,
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
    nextTutorialStep: () -> Unit = {},
    endTutorial: () -> Unit = {},
) {
    val titleText = stringResource(id = R.string.edit_card) + " " + cardWithTags.card.word

    CardDialogBody(
        titleText = titleText,
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        boxWithCategories = boxWithCategories,
        tutorial = tutorial,
        tutorialState = tutorialState,
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
        nextTutorialStep = nextTutorialStep,
        endTutorial = endTutorial,
    )
}

@Preview
@Composable
fun EditCardDialogPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text"
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

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
            box = box,
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        boxWithCategories = boxWithCategories,
        tutorial = false,
        tutorialState = TutorialState.OFF,
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
    boxWithCategories: UiBoxWithCategories,
    tutorial: Boolean,
    tutorialState: TutorialState,
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
    nextTutorialStep: () -> Unit = {},
    endTutorial: () -> Unit = {},
) {
    val applicationContext = LocalContext.current.applicationContext
    var categoriesExpanded by remember { mutableStateOf(false) }
    var categoryMenuOpened by remember { mutableStateOf(false) }
    val isLanguageBox = boxWithTags.box.isLanguage()

    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
    val highlightModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(highlightColor)
        .padding(2.dp)

    /** stuff for audio memos*/
    val mmr = MediaMetadataRetriever()
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var audioFile: File? by remember { mutableStateOf(null) }
    var duration by remember { mutableLongStateOf(0) }

    /** for only accepting valid card edits */
    var validWord by remember { mutableStateOf(true) }
    var validMeaning by remember { mutableStateOf(true) }

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

    // kinda hacky but necessary because a DismissRequest calls onExpandedChanged of
    // the DropDownMenu first, then onDismissRequest of the AlertDialog
    LaunchedEffect(key1 = categoriesExpanded) {
        if (!categoriesExpanded) {
            delay(100)
            categoryMenuOpened = false
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

    CustomDialog(
        modifier = modifier,
        onDismissRequest = {
            if (categoryMenuOpened) {
                categoryMenuOpened = false
            } else {
                if (!tutorial) {
                    onCancel()
                }
            }
        },
        title = titleText,
        text = {
            Column(
                modifier = modifier
            ) {
                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_WORD) highlightModifier else Modifier,
                ) {
                    WordField(
                        cardUiState = cardUiState,
                        isError = !validWord,
                        isLanguage = isLanguageBox,
                        onValueChange = {
                            validWord = true
                            updateUiState(cardUiState.cardDetails.copy(word = it))
                        }
                    )
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_MEANING) highlightModifier else Modifier,
                ) {
                    MeaningField(
                        cardUiState = cardUiState,
                        isError = !validMeaning,
                        onValueChange = {
                            validMeaning = true
                            updateUiState(cardUiState.cardDetails.copy(meaning = it))
                        },
                        isLanguage = isLanguageBox,
                    )
                }

                RequiredFieldsText()

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_CATEGORY) highlightModifier else Modifier,
                ) {
                    CategoriesDropDownMenu(
                        currentCategory = boxWithCategories.categoryList
                            .find { it.categoryId == cardUiState.cardDetails.categoryId }
                            ?: emptyCategory,
                        boxWithCategories = boxWithCategories,
                        expanded = categoriesExpanded,
                        changeExpanded = {
                            categoryMenuOpened = true
                            categoriesExpanded = !categoriesExpanded
                        },
                        onSelectCategory = { updateUiState(cardUiState.cardDetails.copy(categoryId = it.categoryId)) },
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                /** TagList */
                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_TAG_LIST) highlightModifier else Modifier,
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TagList(
                            modifier = modifier.weight(1f),
                            tagList = boxWithTags.tagList,
                            onClick = { onTagClick(it) },
                            onLongClick = { showEditTagDialog(it) },
                            selectedTags = cardUiState.tagList,
                            onBoxScreen = false,
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
                }

                /** Voice memo */
                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_MEMO) highlightModifier else Modifier,
                ) {
                    Row(
                        modifier = modifier.wrapContentHeight().fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(text = stringResource(id = R.string.memo) + ": ")

                        if (isRecording) {
                            IconButton(
                                onClick = { onStopRecording() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "stop"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    if (!hasRecordingPermission) {
                                        val success = requestRecordingPermission()
                                        if (success) {
                                            onRecord()
                                        }
                                    } else {
                                        onRecord()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "record"
                                )
                            }
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
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_NOTES) highlightModifier else Modifier,
                ) {
                    NotesField(
                        cardUiState = cardUiState,
                        onValueChange = { updateUiState(cardUiState.cardDetails.copy(notes = it)) }
                    )
                }
            }
        },

        confirmButton = {
            Box(
                modifier = if (tutorialState == TutorialState.ADD_CARD_DIALOG_SAVE) highlightModifier else Modifier,
            ) {
                TextButton(
                    onClick = {
                        if (cardUiState.isValid) {
                            onClickSave()
                        } else {
                            if (!cardUiState.validWord) {
                                validWord = false
                            }
                            if (!cardUiState.validMeaning) {
                                validMeaning = false
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        },

        dismissButton = {
            Row {
                TextButton(
                    onClick = {
                        if (tutorial) {
                            endTutorial()
                        }
                        onCancel()
                    }
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
        },

        tutorial = tutorial,
        tutorialText = {
            when (tutorialState) {
                TutorialState.ADD_CARD_DIALOG -> {
                    Text(text = stringResource(id = R.string.add_card_dialog))
                }

                TutorialState.ADD_CARD_DIALOG_WORD -> {
                    if (isLanguageBox) {
                        Text(text = stringResource(id = R.string.add_card_dialog_word))
                    } else {
                        Text(text = stringResource(id = R.string.add_card_dialog_front))
                    }
                }

                TutorialState.ADD_CARD_DIALOG_MEANING -> {
                    if (isLanguageBox) {
                        Text(text = stringResource(id = R.string.add_card_dialog_meaning))
                    } else {
                        Text(text = stringResource(id = R.string.add_card_dialog_back))
                    }
                }

                TutorialState.ADD_CARD_DIALOG_CATEGORY -> {
                    Text(text = stringResource(id = R.string.add_card_dialog_category))
                }

                TutorialState.ADD_CARD_DIALOG_TAG_LIST -> {
                    Text(text = stringResource(id = R.string.add_card_dialog_tag_list))
                }

                TutorialState.ADD_CARD_DIALOG_MEMO -> {
                    Text(text = stringResource(id = R.string.add_card_dialog_memo))
                }

                TutorialState.ADD_CARD_DIALOG_NOTES -> {
                    Text(text = stringResource(id = R.string.add_card_dialog_notes))
                }

                TutorialState.ADD_CARD_DIALOG_SAVE -> {
                    Text(text = stringResource(id = R.string.add_card_dialog_save))
                }

                else -> {}
            }
        },

        tutorialConfirmButton = {
            if (tutorialState != TutorialState.ADD_CARD_DIALOG_SAVE) {
                TextButton(
                    onClick = nextTutorialStep
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    )
}