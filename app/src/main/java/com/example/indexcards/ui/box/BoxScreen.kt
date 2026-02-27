package com.example.indexcards.ui.box

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.box.dialogs.CardDialog
import com.example.indexcards.ui.box.dialogs.CardsToCategoryDialog
import com.example.indexcards.ui.box.dialogs.DeleteCardDialog
import com.example.indexcards.ui.box.dialogs.EditCardDialog
import com.example.indexcards.ui.box.dialogs.NewCardDialog
import com.example.indexcards.ui.box.dialogs.DeleteBoxDialog
import com.example.indexcards.ui.box.dialogs.DeleteCardsDialog
import com.example.indexcards.ui.box.dialogs.NoCardsDialog
import com.example.indexcards.ui.box.dialogs.TagDialog
import com.example.indexcards.ui.box.dialogs.TagsToCardsDialog
import com.example.indexcards.ui.home.dialogs.TutorialDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenSorting
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.home.TutorialMap
import com.example.indexcards.utils.home.TutorialState
import com.example.indexcards.utils.state.toBoxDetails
import com.example.indexcards.utils.state.toCardState
import com.example.indexcards.utils.notification.getTriggerTime
import com.example.indexcards.utils.notification.getTimeInterval
import com.example.indexcards.utils.recording.AndroidAudioPlayer
import com.example.indexcards.utils.recording.AndroidAudioRecorder
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.toColor
import com.example.indexcards.utils.state.toTagDetails
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
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
    scheduleNotification: (Int, String, Long, Long) -> Unit = { _, _, _, _ -> }, /* lvl, name, trigger, repeat */
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

    val globalReminders by boxScreenViewModel.globalReminders.collectAsState()
    val reminderIntervals by boxScreenViewModel.reminderIntervals.collectAsState()
    val reminderTime by boxScreenViewModel.reminderTime.collectAsState()

    var cardDialog by remember { mutableStateOf(false) }
    var noCardsDialog by remember { mutableStateOf(false) }
    var newCardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var deleteCardsDialog by remember { mutableStateOf(false) }
    var addTagsToCardsDialog by remember { mutableStateOf(false) }
    var addCardsToCategoryDialog by remember { mutableStateOf(false) }
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
    val greyBackground = listOf(
        TutorialState.ADD_CARD_INTRO, TutorialState.END_OF_TUTORIAL,
    ).contains(tutorialState)

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

    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

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
        val time = getTriggerTime(
            reminderIntervals = reminderIntervals,
            reminderTime = reminderTime,
            level = level
        )
        val period = getTimeInterval(
            reminderIntervals = reminderIntervals,
            level = level,
        )
        scheduleNotification(level, boxWithTags.box.name, time, period)
    }

    val remindersSetText = stringResource(id = R.string.reminders_set)
    fun setAllReminders() {
        boxScreenViewModel.viewModelScope.launch {
            for (level in 0..<NUMBER_OF_LEVELS) {
                if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level) != 0)
                    setReminder(level)
            }
            Toast.makeText(context, remindersSetText, Toast.LENGTH_SHORT).show()
        }
    }

    fun setRemindersAfterTraining() {
        boxScreenViewModel.viewModelScope.launch {
            /** If a level had been selected (e.g. when training from notification) */
            if (trainingCounts && boxWithTags.box.reminders) {
                val currentTime = ZonedDateTime.now().toInstant().toEpochMilli()
                if (levelSelected != -1) {
                    val nextLevel = levelSelected + 1
                    val previousLevel = levelSelected - 1

                    /* Cancel the repeating notifications if no card is left at this level */
                    if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(levelSelected) == 0) {
                        cancelNotification(levelSelected)
                    }

                    /* Set reminders for next level (if it has cards and current one is not the last) */
                    if (levelSelected != (NUMBER_OF_LEVELS - 1) &&
                        boxScreenViewModel.getNumberOfCardsOfLevelInBox(nextLevel) != 0
                    ) {
                        setReminder(nextLevel)
                    }

                    /* Set reminders for previous level (if it has cards and current one is not the first) */
                    if (levelSelected != 0 &&
                        boxScreenViewModel.getNumberOfCardsOfLevelInBox(previousLevel) != 0
                    ) {
                        setReminder(previousLevel)
                    }

                    boxScreenViewModel.setLastTrainedTime(levelSelected, currentTime)
                } else {
                    /** If no level had been selected (e.g. when training just like this) */
                    for (level in shuffledCardList.map { it.card.level }.toSet()) {
                        val nextLevel = level + 1
                        val previousLevel = level - 1

                        /* Cancel the repeating notifications if no card is left at this level */
                        if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level) == 0) {
                            cancelNotification(level)
                        }

                        /* Set reminders for next level (if it has cards and current one is not the last) */
                        if (level != (NUMBER_OF_LEVELS - 1) &&
                            boxScreenViewModel.getNumberOfCardsOfLevelInBox(nextLevel) != 0
                        ) {
                            setReminder(nextLevel)
                        }

                        /* Set reminders for previous level (if it has cards and current one is not the first) */
                        if (level != 0 &&
                            boxScreenViewModel.getNumberOfCardsOfLevelInBox(previousLevel) != 0
                        ) {
                            setReminder(previousLevel)
                        }

                        boxScreenViewModel.setLastTrainedTime(level, currentTime)
                    }
                }
            }

            boxScreenViewModel.resetLevelSelected()
            boxScreenViewModel.resetTagSelected()
            boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
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
                boxWithTags = boxWithTags,
                cancelEdit = { boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW) },
                trainingCounts = trainingCounts,
                isSelecting = isSelecting,
                allCategoriesCollapsed = (boxScreenViewModel.allCategoriesCollapsed.collectAsState().value),
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
                },
                showTagsToCardsDialog = {
                    addTagsToCardsDialog = true
                },
                showCardsToCategoryDialog = {
                    addCardsToCategoryDialog = true
                },
            )

            if (greyBackground) {
                Box(
                    modifier = modifier
                        .height(TopAppBarDefaults.TopAppBarExpandedHeight.value.dp + statusBarHeight)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {})
                        .background(color = Color.Black.copy(alpha = 0.6F)),
                )
            }
        },

        floatingActionButton = {
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    if (!isSelecting) {
                        Column {
                            if (filteredCardWithTagList.isNotEmpty()) {
                                FloatingActionButton(
                                    onClick = {
                                        isSelecting = false
                                        selectedCards = listOf()
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

                            FloatingActionButton(
                                onClick = {
                                    if (tutorial) {
                                        tutorialStep += 1
                                    }
                                    boxScreenViewModel.setBiggestCardId()
                                    isSelecting = false
                                    selectedCards = listOf()
                                    newCardDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    } else {
                        FloatingActionButton(
                            onClick = {
                                deleteCardsDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete"
                            )
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
                    categoriesExpanded = categoriesExpanded,
                    filteredCardWithTagList = filteredCardWithTagList,
                    reminderIntervals = reminderIntervals,
                    reminderTime = reminderTime,
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
                        isSelecting = false
                        selectedCards = listOf()
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

                if (greyBackground) {
                    Box(
                        modifier = modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = {})
                            .background(color = Color.Black.copy(alpha = 0.6F)),
                    )
                }
            }

            BoxScreenState.EDIT -> {
                BoxScreenEditing(
                    modifier = modifier.padding(innerPadding),
                    boxUiState = boxUiState,
                    boxWithCategories = boxWithCategories,
                    categoryUiState = categoryUiState,
                    globalReminders = globalReminders,
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
                    onCardCorrect = { boxScreenViewModel.onCardCorrect(it) },
                    onCardIncorrect = { boxScreenViewModel.onCardIncorrect(it) },
                    finishTraining = { setRemindersAfterTraining() },
                )
            }
        }
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
            categoryUiState = categoryUiState,
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
                if (tutorial) {
                    tutorialStep += 1
                }
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
            endTutorial = {
                newCardDialog = false
                endTutorial()
                boxScreenViewModel.resetCategoryUiState()
            },
            updateCategoryUiState = { boxScreenViewModel.updateCategoryUiState(it) },
            resetCategoryUiState = { boxScreenViewModel.resetCategoryUiState() },
            saveCategory = { boxScreenViewModel.saveCategory() },
        )
    }

    if (editCardDialog) {
        EditCardDialog(
            boxWithTags = boxWithTags,
            boxWithCategories = boxWithCategories,
            categoryUiState = categoryUiState,
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
            updateCategoryUiState = { boxScreenViewModel.updateCategoryUiState(it) },
            resetCategoryUiState = { boxScreenViewModel.resetCategoryUiState() },
            saveCategory = { boxScreenViewModel.saveCategory() },
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
                boxScreenViewModel.deleteCard(currentCard)
            }
        )
    }

    if (deleteCardsDialog) {
        DeleteCardsDialog(
            modifier = Modifier,
            cardList = selectedCards,
            onDismiss = { deleteCardsDialog = false },
            deleteCards = {
                deleteCardsDialog = false
                isSelecting = false
                boxScreenViewModel.viewModelScope.launch {
                    for (card in selectedCards) {
                        boxScreenViewModel.deleteCard(card)
                    }
                    selectedCards = listOf()
                }
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

    if (addTagsToCardsDialog) {
        var givenTags = cardsWithTags.cardWithTagList.filter {
            selectedCards.contains(it.card)
        }.map {
            it.tags
        }.fold(boxWithTags.tagList) { acc, next ->
            acc.intersect(next).toList()
        }

        TagsToCardsDialog(
            modifier = Modifier,
            boxWithTags = boxWithTags,
            cardList = selectedCards,
            givenTags = givenTags,
            onDismiss = {
                addTagsToCardsDialog = false
            },
            onSave = { selectedTags, deselectedTags ->
                boxScreenViewModel.viewModelScope.launch {
                    for (tag in selectedTags) {
                        for (card in selectedCards) {
                            boxScreenViewModel.addTagToCard(tagId = tag.tagId, cardId = card.cardId)
                        }
                    }
                    for (tag in deselectedTags) {
                        for (card in selectedCards) {
                            boxScreenViewModel.removeTagFromCard(
                                tagId = tag.tagId,
                                cardId = card.cardId
                            )
                        }
                    }
                    isSelecting = false
                    selectedCards = listOf()
                }
            },
            showEditTagDialog = { showEditTagDialog(it) },
        )
    }

    if (addCardsToCategoryDialog) {
        CardsToCategoryDialog(
            modifier = Modifier,
            boxWithCategories = boxWithCategories,
            categoryUiState = categoryUiState,
            cardList = selectedCards,
            onDismiss = {
                addCardsToCategoryDialog = false
            },
            onSave = { category ->
                boxScreenViewModel.viewModelScope.launch {
                    for (card in selectedCards) {
                        boxScreenViewModel.saveCardToCategory(card = card, category = category)
                    }
                    selectedCards = listOf()
                    isSelecting = false
                }
            },
            updateCategoryUiState = { boxScreenViewModel.updateCategoryUiState(it) },
            resetCategoryUiState = { boxScreenViewModel.resetCategoryUiState() },
            saveCategory = { boxScreenViewModel.saveCategory() },
        )
    }

    if (tutorial) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TutorialDialog(
                tutorialState = tutorialState,
                nextStep = { tutorialStep += 1 },
                stopTutorial = { endTutorial() },
            )
        }
    }

    if (deleteBoxDialog) {
        DeleteBoxDialog(
            onDismiss = { deleteBoxDialog = false },
            onDelete = {
                navigateToBoxesOverview()
                boxScreenViewModel.viewModelScope.launch {
                    deleteAllMemos(cardsWithTags.cardWithTagList.map { it.card })
                    boxScreenViewModel.deleteBox(boxId = boxId)
                }
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