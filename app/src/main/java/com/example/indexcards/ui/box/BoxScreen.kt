package com.example.indexcards.ui.box

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.box.dialogs.CardDialog
import com.example.indexcards.ui.box.dialogs.DeleteCardDialog
import com.example.indexcards.ui.box.dialogs.EditCardDialog
import com.example.indexcards.ui.box.dialogs.NewCardDialog
import com.example.indexcards.ui.box.dialogs.DeleteBoxDialog
import com.example.indexcards.ui.box.dialogs.NoCardsDialog
import com.example.indexcards.ui.box.dialogs.TagDialog
import com.example.indexcards.ui.home.Tutorial
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenSorting
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.home.TutorialMap
import com.example.indexcards.utils.home.TutorialState
import com.example.indexcards.utils.state.toBoxDetails
import com.example.indexcards.utils.state.toCardState
import com.example.indexcards.utils.notification.getTimeFromReminderSettings
import com.example.indexcards.utils.notification.getTimeIntervalFromReminderIntervals
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.recording.AndroidAudioRecorder
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.toColor
import com.example.indexcards.utils.state.toTagDetails
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    boxId: Long, /* Is also here for boxScreenViewModel to work */
    startLevel: Int = -1,
    tutorialGiven: Boolean = false,
    hasNotificationPermission: Boolean = false,
    hasRecordingPermission: Boolean = false,
    navigateToBoxesOverview: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false },
    requestRecordingPermission: () -> Boolean = { false },
    deleteAllMemos: (List<Card>) -> Unit = {},
    saveFile: (ByteArray, String) -> Unit = { _, _ -> },
    cancelNotification: (Int) -> Unit = {},
    scheduleNotification: (Int, String, Long, Long) -> Unit = { _, _, _, _ -> },
    boxScreenViewModel: BoxScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val applicationContext = LocalContext.current.applicationContext
    val context = LocalContext.current

    /** Navigation on the BoxScreen */
    val boxScreenState = boxScreenViewModel.boxScreenState

    /** uiStates of Box (fixed) and Card (dynamic) */
    val boxUiState = boxScreenViewModel.boxUiState
    val cardUiState = boxScreenViewModel.cardUiState
    val categoryUiState = boxScreenViewModel.categoryUiState
    val tagUiState = boxScreenViewModel.tagUiState
    val newCardId = boxScreenViewModel.newCardId
    val tagSelected by boxScreenViewModel.tagSelected.collectAsState()
    val levelSelected by boxScreenViewModel.levelSelected.collectAsState()
    val sortedBy by boxScreenViewModel.sortedBy.collectAsState()
    val searchTerm by boxScreenViewModel.searchTerm.collectAsState()
    val trainingCounts by boxScreenViewModel.trainingCounts.collectAsState()
    val trainingDirection by boxScreenViewModel.trainingDirection.collectAsState()
    val boxWithTags by boxScreenViewModel.uiBoxWithTags.collectAsState()
    val boxWithCategories by boxScreenViewModel.uiBoxWithCategories.collectAsState()
    val cardsWithTags by boxScreenViewModel.uiCardsWithTags.collectAsState()
    val cardWithTags by boxScreenViewModel.uiCardWithTags.collectAsState()
    val tagWithCards by boxScreenViewModel.uiTagWithCards.collectAsState()
    val currentCard by boxScreenViewModel.currentCard.collectAsState()
    val categoriesExpanded by boxScreenViewModel.categoriesExpanded.collectAsState()

    val globalReminders = boxScreenViewModel.globalReminders.collectAsState()
    val reminderIntervals = boxScreenViewModel.reminderIntervals.collectAsState()
    val reminderTime = boxScreenViewModel.reminderTime.collectAsState()

    var cardDialog by remember { mutableStateOf(false) }
    var noCardsDialog by remember { mutableStateOf(false) }
    var newCardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(true) }
    var tagDialog by remember { mutableStateOf(false) }
    var deleteBoxDialog by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var trainSelection by remember { mutableStateOf(false) }
    var trainCategory by remember { mutableLongStateOf(-1) }
    var isSelecting by remember { mutableStateOf(false) }
    var selectedCards by remember { mutableStateOf<List<Card>>(listOf()) }

    val doneCollecting = boxScreenViewModel.doneCollectingData
    val csvString = boxScreenViewModel.csvString

    /** Tutorial */
    var tutorial by remember { mutableStateOf(false) }
    var tutorialStep by remember { mutableIntStateOf(-1) }
    val tutorialState =
        TutorialMap.map.entries.firstOrNull { it.key == tutorialStep }?.value ?: TutorialState.ERROR

    val filteredCardWithTagList =
        cardsWithTags.cardWithTagList.filter {
            (it.card.word.lowercase(Locale.ROOT).contains(searchTerm.lowercase(Locale.ROOT)) ||
                    it.card.meaning.lowercase(Locale.ROOT)
                        .contains(searchTerm.lowercase(Locale.ROOT)) ||
                    searchTerm.isBlank()) &&
                    (it.card.level == levelSelected || levelSelected == -1) &&
                    (it.tags.contains(tagSelected) || tagSelected == emptyTag)
        }.sortedWith(
            when (sortedBy) {
                BoxScreenSorting.DATE_DESC -> {
                    compareBy<CardWithTags> { it.card.dateAdded }
                }

                BoxScreenSorting.DATE_ASC -> {
                    compareBy<CardWithTags> { it.card.dateAdded }.reversed()
                }

                BoxScreenSorting.LEVEL_ASC -> {
                    compareBy<CardWithTags> { it.card.level }
                }

                BoxScreenSorting.LEVEL_DESC -> {
                    compareBy<CardWithTags> { it.card.level }.reversed()
                }

                BoxScreenSorting.WORD_ASC -> {
                    compareBy<CardWithTags> { it.card.word }
                }

                BoxScreenSorting.WORD_DESC -> {
                    compareBy<CardWithTags> { it.card.word }.reversed()
                }
            }
        )

    var shuffledCardList: List<CardWithTags> by remember { mutableStateOf(listOf()) }

    val fileName = "${boxWithTags.box.name}.csv"

    /** Stuff for the voice memos */
    val recorder by lazy { AndroidAudioRecorder(applicationContext) }
    val player by lazy { AndroidAudioPlayer(applicationContext) }

    LaunchedEffect(key1 = doneCollecting) {
        if (doneCollecting) {
            saveFile(csvString.toByteArray(Charsets.UTF_8), fileName)
        }
    }

    LaunchedEffect(key1 = cardsWithTags.cardWithTagList.size) {
        boxScreenViewModel.setBiggestCardId()
    }

    LaunchedEffect(key1 = startLevel) {
        if (startLevel != -1) {
            boxScreenViewModel.setLevelSelected(startLevel)
            if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level = startLevel) != 0) {
                /** A bit hacky using this suspend function but without it the state is not
                 * updated yet and cardsWithTags is still empty */
                boxScreenViewModel.changeTrainingCounts(true)
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.TRAIN)
            } else {
                noCardsDialog = true
            }
        }
    }

    LaunchedEffect(key1 = tutorialGiven) {
        if (tutorialGiven) {
            tutorialStep =
                TutorialMap.map.entries.firstOrNull { it.value == TutorialState.ADD_CARD_INTRO }?.key
                    ?: -1
            tutorial = true
        }
    }

    fun setReminder(level: Int) {
        val time = getTimeFromReminderSettings(
            reminderIntervals = reminderIntervals.value,
            reminderTime = reminderTime.value,
            level = level
        )
        val period = getTimeIntervalFromReminderIntervals(
            reminderIntervals = reminderIntervals.value,
            level = level,
        )
        scheduleNotification(level, boxUiState.boxDetails.name, time, period)
    }

    fun setAllReminders() {
        boxScreenViewModel.viewModelScope.launch {
            for (level in 0..<NUMBER_OF_LEVELS) {
                if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level) != 0)
                    setReminder(level)
            }
            Toast.makeText(context, "Reminders have been set!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRemindersAfterTraining() {
        if (trainingCounts && boxWithTags.box.reminders) {
            boxScreenViewModel.viewModelScope.launch {
                val nextLevel = levelSelected + 1
                val previousLevel = levelSelected - 1

                if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(levelSelected) == 0) {
                    cancelNotification(levelSelected)
                }

                if (levelSelected != 4 &&
                    boxScreenViewModel.getNumberOfCardsOfLevelInBox(nextLevel) != 0
                ) {
                    setReminder(nextLevel)
                }

                if (levelSelected != 0 &&
                    boxScreenViewModel.getNumberOfCardsOfLevelInBox(previousLevel) != 0
                ) {
                    setReminder(previousLevel)
                }
            }
        }
    }

    fun showEditTagDialog(tag: Tag) {
        boxScreenViewModel.setColor(tag.color)
        boxScreenViewModel.setTagUiState(tag.toTagDetails())
        newTag = false; tagDialog = true
    }

    fun showNewTagDialog() {
        newTag = true; tagDialog = true
    }

    fun endTutorial() {
        tutorial = false
        tutorialStep = -1
    }

    BackHandler {
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                if (isSearching) {
                    isSearching = false
                    boxScreenViewModel.resetSearchTerm()
                } else {
                    navigateToBoxesOverview()
                }
            }

            BoxScreenState.EDIT -> {
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
            }

            BoxScreenState.TRAIN -> {
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
            }
        }
    }

    LaunchedEffect(key1 = boxScreenState) {
        if (boxScreenState == BoxScreenState.TRAIN) {
            shuffledCardList =
                if (trainCategory != (-1).toLong()) {
                    filteredCardWithTagList
                        .filter { it.card.categoryId == trainCategory }
                        .shuffled()
                } else {
                    if (trainSelection) {
                        filteredCardWithTagList.shuffled()
                    } else {
                        cardsWithTags.cardWithTagList.shuffled()
                    }
                }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxScreenTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                updateEditUiStatus = { boxScreenViewModel.updateBoxUiState(boxWithTags.box.toBoxDetails()) },
                changeBoxScreenState = { boxScreenViewModel.updateBoxScreenState(it) },
                boxScreenState = boxScreenState,
                thisBox = boxWithTags.box,
                cancelEdit = { boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW) },
                trainingCounts = trainingCounts,
                isSelecting = isSelecting,
                allCategoriesExpanded = (boxScreenViewModel.allCategoriesExpanded.collectAsState().value),
                changeTrainingCounts = { boxScreenViewModel.changeTrainingCounts() },
                changeTrainingDirection = { boxScreenViewModel.changeTrainingDirection() },
                changeTrainingDirectionToValue = { boxScreenViewModel.changeTrainingDirection(it) },
                exportBox = { boxScreenViewModel.collectCSVString() },
                showSearch = {
                    isSearching = true
                    for (category in boxWithCategories.categoryList) {
                        boxScreenViewModel.toggleCategoryExpanded(category.categoryId)
                    }
                    boxScreenViewModel.toggleCategoryExpanded((-1).toLong())
                },
                onSortBy = { boxScreenViewModel.setSortedBy(it) },
                setRemindersAfterTraining = { setRemindersAfterTraining() },
                setTrainSelection = { trainSelection = it },
                toggleAllCategories = { boxScreenViewModel.toggleAllCategoriesExpanded() },
                stopSelection = {
                    isSelecting = false
                    selectedCards = listOf()
                }
            )
        },

        floatingActionButton = {
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    Column {
                        if (filteredCardWithTagList.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    trainSelection = true
                                    boxScreenViewModel.changeTrainingDirection(true)
                                    boxScreenViewModel.updateBoxScreenState(BoxScreenState.TRAIN)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "train"
                                )
                            }

                            Spacer(modifier = Modifier.size(10.dp))
                        }

                        val boxModifier =
                            if (tutorial && tutorialState == TutorialState.ADD_CARD_INTRO) {
                                Modifier
                                    .clip(FloatingActionButtonDefaults.shape)
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .padding(6.dp)
                            } else {
                                Modifier
                            }
                        Box(
                            modifier = boxModifier
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    if (tutorial) {
                                        tutorialStep += 1
                                    }
                                    boxScreenViewModel.setBiggestCardId()
                                    newCardDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                }

                BoxScreenState.EDIT -> {
                    FloatingActionButton(
                        onClick = { deleteBoxDialog = true }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                BoxScreenState.TRAIN -> {}
            }
        }
    ) { innerPadding ->
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                BoxScreenBody(
                    modifier = modifier.padding(innerPadding),
                    levelSelected = levelSelected,
                    isSearching = isSearching,
                    searchText = searchTerm,
                    boxWithTags = boxWithTags,
                    boxWithCategories = boxWithCategories,
                    cardsWithTags = cardsWithTags,
                    tagWithCards = tagWithCards,
                    showCategories = boxWithCategories.box.categories,
                    categoriesExpanded = categoriesExpanded,
                    filteredCardWithTagList = filteredCardWithTagList,
                    showCardDialog = {
                        boxScreenViewModel.setCurrentCard(it)
                        cardDialog = true
                    },
                    showNewTagDialog = { showNewTagDialog() },
                    onTagLongClick = { showEditTagDialog(it) },
                    selectLevel = { boxScreenViewModel.setLevelSelected(it) },
                    setTagSortedBy = { boxScreenViewModel.setTagSelected(it) },
                    resetTagSortedBy = { boxScreenViewModel.resetTagSelected() },
                    onCloseSearch = {
                        isSearching = false
                        boxScreenViewModel.resetSearchTerm()
                    },
                    updateSearchText = { boxScreenViewModel.setSearchTerm(it) },
                    numberOfButtons = if (filteredCardWithTagList.isNotEmpty()) 2 else 1,
                    trainCategory = {
                        trainCategory = it
                        boxScreenViewModel.updateBoxScreenState(BoxScreenState.TRAIN)
                    },
                    toggleCategoryExpanded = { boxScreenViewModel.toggleCategoryExpanded(it) },
                    isSelecting = isSelecting,
                    selectedCards = selectedCards,
                    selectCard = {
                        selectedCards =
                            if (selectedCards.contains(it)) {
                                selectedCards.minus(it)
                            } else {
                                selectedCards.plus(it)
                            }
                    },
                    startSelection = {
                        if (!isSelecting) {
                            isSelecting = true
                        }
                    },
                )
            }

            BoxScreenState.EDIT -> {
                BoxScreenEditing(
                    modifier = modifier.padding(innerPadding),
                    boxUiState = boxUiState,
                    boxWithCategories = boxWithCategories,
                    categoryUiState = categoryUiState,
                    globalReminders = globalReminders.value,
                    onSave = {
                        boxScreenViewModel.saveBox()
                        boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
                    },
                    changeGlobalReminders = { boxScreenViewModel.changeGlobalReminders() },
                    hasNotificationPermission = hasNotificationPermission,
                    requestNotificationPermission = requestNotificationPermission,
                    updateBoxUiState = { boxScreenViewModel.updateBoxUiState(it) },
                    setAllReminders = { setAllReminders() },
                    updateCategoryUiState = { boxScreenViewModel.updateCategoryUiState(it) },
                    resetCategoryUiState = { boxScreenViewModel.resetCategoryUiState() },
                    saveCategory = { boxScreenViewModel.saveCategory() },
                    deleteCategory = { boxScreenViewModel.deleteCategory(it) }
                )
            }

            BoxScreenState.TRAIN -> {
                TrainingScreen(
                    modifier = modifier.padding(innerPadding),
                    trainingCounts = trainingCounts,
                    trainingDirection = trainingDirection,
                    cardList = shuffledCardList,
                    navigateToBoxScreen = {
                        boxScreenViewModel.resetLevelSelected()
                        boxScreenViewModel.resetTagSelected()
                        boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
                    },
                    onCardCorrect = { boxScreenViewModel.onCardCorrect(it) },
                    onCardIncorrect = { boxScreenViewModel.onCardIncorrect(it) },
                    setRemindersAfterTraining = { setRemindersAfterTraining() },
                )
            }
        }
    }

    if (tutorialGiven) {
        Tutorial(
            modifier = modifier,
            tutorialState = tutorialState,
            nextStep = { tutorialStep += 1 },
            stopTutorial = { endTutorial() },
        )
    }

    if (cardDialog) {
        CardDialog(
            audioPlayer = player,
            boxWithCategories = boxWithCategories,
            onDismiss = {
                cardDialog = false
                boxScreenViewModel.resetCardUiState()
            },
            cardWithTags = cardWithTags,
            showEditCardDialog = {
                boxScreenViewModel.setCardUiStateFromCurrentCard()
                editCardDialog = true
            },
            isEditing = editCardDialog,
            showDelete = { deleteCardDialog = true },
        )
    }

    if (newCardDialog) {
        NewCardDialog(
            cardUiState = cardUiState,
            boxWithTags = boxWithTags,
            boxWithCategories = boxWithCategories,
            tutorial = tutorial,
            tutorialState = tutorialState,
            cardId = newCardId,
            audioPlayer = player,
            audioRecorder = recorder,
            hasRecordingPermission = hasRecordingPermission,
            requestRecordingPermission = requestRecordingPermission,
            updateUiState = { boxScreenViewModel.updateCardState(cardDetails = it) },
            onDismiss = {
                newCardDialog = false
                boxScreenViewModel.resetCard()
            },
            saveCard = {
                newCardDialog = false
                boxScreenViewModel.saveCard(doReset = true)
            },
            onTagClick = {
                if (cardUiState.tagList.contains(it)) {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.minus(it))
                } else {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.plus(it))
                }
            },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog(it) },
            nextTutorialStep = { tutorialStep += 1 },
            endTutorial = { endTutorial() },
        )
    }

    if (editCardDialog) {
        EditCardDialog(
            boxWithTags = boxWithTags,
            boxWithCategories = boxWithCategories,
            cardWithTags = cardWithTags,
            cardUiState = cardUiState,
            tutorial = tutorial,
            tutorialState = tutorialState,
            audioPlayer = player,
            audioRecorder = recorder,
            hasRecordingPermission = hasRecordingPermission,
            requestRecordingPermission = requestRecordingPermission,
            updateUiState = { boxScreenViewModel.updateCardState(cardDetails = it) },
            onDismiss = {
                boxScreenViewModel.updateCardState(cardWithTags.toCardState(true))
                editCardDialog = false
            },
            onDeleteCard = { deleteCardDialog = true },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog(it) },
            saveCard = {
                editCardDialog = false
                boxScreenViewModel.saveCard(doReset = false)
            },
            clickOnTag = {
                if (cardUiState.tagList.contains(it)) {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.minus(it))
                } else {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.plus(it))
                }
            },
        )
    }

    if (deleteCardDialog) {
        DeleteCardDialog(
            currentCard = currentCard,
            onDismiss = {
                deleteCardDialog = false
            },
            deleteCard = {
                cardDialog = false
                editCardDialog = false
                deleteCardDialog = false
                if (currentCard.memoURI.isNotBlank()) {
                    currentCard.memoURI.toUri().path?.let { path ->
                        File(path).also { file ->
                            file.delete()
                        }
                    }
                }
                boxScreenViewModel.deleteCard(currentCard)
            }
        )
    }

    if (tagDialog) {
        TagDialog(
            modifier = Modifier,
            newTag = newTag,
            tagUiState = tagUiState,
            initialColor = boxScreenViewModel.colorUiState.toColor(),
            onDismiss = {
                tagDialog = false
                boxScreenViewModel.resetTagUiState()
            },
            updateUiState = { boxScreenViewModel.setTagUiState(it) },
            saveTag = {
                tagDialog = false
                boxScreenViewModel.saveNewTag(
                    addToCard = (editCardDialog || newCardDialog)
                )
            },
            updateTag = {
                tagDialog = false
                boxScreenViewModel.updateTag()
            },
            setColor = { boxScreenViewModel.setColor(it) },
            onDelete = {
                tagDialog = false
                boxScreenViewModel.deleteTag()
                boxScreenViewModel.resetTagUiState()
            }
        )
    }

    if (deleteBoxDialog) {
        DeleteBoxDialog(
            onDismiss = { deleteBoxDialog = false },
            onDelete = {
                navigateToBoxesOverview()
                deleteAllMemos(cardsWithTags.cardWithTagList.map { it.card })
                boxScreenViewModel.deleteBox(boxId = boxId)
            },
            boxToBeDeleted = boxWithTags.box
        )
    }

    if (noCardsDialog) {
        NoCardsDialog(
            onDismiss = {
                boxScreenViewModel.setLevelSelected(-1)
                noCardsDialog = false
            },
            level = levelSelected
        )
    }
}