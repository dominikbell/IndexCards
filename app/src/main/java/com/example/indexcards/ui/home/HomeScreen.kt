package com.example.indexcards.ui.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Merge
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.ui.box.dialogs.DeleteBoxDialog
import com.example.indexcards.ui.home.dialogs.AboutAppDialog
import com.example.indexcards.ui.home.dialogs.AddBoxDialog
import com.example.indexcards.ui.home.dialogs.DeleteBoxesDialog
import com.example.indexcards.ui.home.dialogs.LoadingDialog
import com.example.indexcards.ui.home.dialogs.MergeBoxesDialog
import com.example.indexcards.ui.home.dialogs.ReminderIntervalsDialog
import com.example.indexcards.ui.home.dialogs.ReminderTimeDialog
import com.example.indexcards.ui.home.dialogs.UserNameDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.home.HomeScreenSorting
import com.example.indexcards.utils.home.HomeScreenState
import com.example.indexcards.utils.home.HomeScreenViewModel
import com.example.indexcards.utils.home.TutorialMap
import com.example.indexcards.utils.home.TutorialState
import com.example.indexcards.utils.notification.getTriggerTime
import com.example.indexcards.utils.notification.getTimeInterval
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Boolean = { false },
    deleteAllMemos: (List<Card>) -> Unit = {},
    importBox: () -> Unit = {},
    navigateToBoxScreen: (Long, Boolean) -> Unit = { _, _ -> },
    cancelAllNotifications: () -> Unit = {},
    scheduleNotification: (Long, Int, String, Long, Long) -> Unit = { _, _, _, _, _ -> },
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var backPressedTime: Long = 0

    val uiUserName = homeScreenViewModel.uiUserName
    val uiReminderIntervals = homeScreenViewModel.uiReminderIntervals
    val currentLevel = homeScreenViewModel.currentLevel
    val boxUiState = homeScreenViewModel.boxUiState
    val homeScreenState = homeScreenViewModel.homeScreenState

    val sortedBy by homeScreenViewModel.sortedBy.collectAsState()
    val userName by homeScreenViewModel.userName.collectAsState()
    val globalReminders by homeScreenViewModel.globalReminders.collectAsState()
    val reminderIntervals by homeScreenViewModel.reminderIntervals.collectAsState()
    val reminderTime by homeScreenViewModel.reminderTime.collectAsState()
    val uiBoxWithCards by homeScreenViewModel.boxWithCards.collectAsState()
    val uiBoxList by homeScreenViewModel.uiBoxList.collectAsState()
    val currentBox by homeScreenViewModel.currentBox.collectAsState()
    val importingInProcess by homeScreenViewModel.importingInProcess.collectAsState()
    val mergingInProcess by homeScreenViewModel.mergingInProcess.collectAsState()
    val backAgainString = stringResource(id = R.string.back_twice_to_close)

    var addBoxDialog by remember { mutableStateOf(false) }
    var deleteBoxDialog by remember { mutableStateOf(false) }
    var deleteBoxesDialog by remember { mutableStateOf(false) }
    var mergeBoxesDialog by remember { mutableStateOf(false) }
    var userNameDialog by remember { mutableStateOf(false) }
    var reminderIntervalsDialog by remember { mutableStateOf(false) }
    var reminderTimeDialog by remember { mutableStateOf(false) }
    var showAboutApp by remember { mutableStateOf(false) }
    var isSelecting by remember { mutableStateOf(false) }
    var selectedBoxes by remember { mutableStateOf<List<Box>>(listOf()) }
    var isLoading by remember { mutableStateOf(false) }

    /** Tutorial */
    var tutorial by remember { mutableStateOf(false) }
    var tutorialStep by remember { mutableIntStateOf(-1) }
    val tutorialState =
        TutorialMap.map.entries.firstOrNull { it.key == tutorialStep }?.value ?: TutorialState.ERROR

    BackHandler {
        when (homeScreenState) {
            HomeScreenState.MAIN -> {
                if (addBoxDialog) {
                    addBoxDialog = false
                } else {
                    if (isSelecting) {
                        isSelecting = false
                        selectedBoxes = listOf()
                    } else {
                        /* TODO: when coming from a box from a notification toast gets created but
                        *   pressing back again navigates back to the HomeScreenState.Main to itself */
                        if (backPressedTime + 3000 > System.currentTimeMillis()) {
                            /** seems a bit hacky but works */
                            activity?.finish()
                        } else {
                            backPressedTime = System.currentTimeMillis()
                            Toast.makeText(context, backAgainString, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            HomeScreenState.SETTINGS -> {
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }

            HomeScreenState.STATISTICS -> {
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }
        }
    }

    LaunchedEffect(key1 = importingInProcess, key2 = mergingInProcess) {
        isLoading = (importingInProcess || mergingInProcess)
    }

    fun setReminder(boxId: Long, boxName: String, level: Int) {
        val time = getTriggerTime(
            reminderIntervals = reminderIntervals,
            reminderTime = reminderTime,
            level = level,
        )
        val period = getTimeInterval(
            reminderIntervals = reminderIntervals,
            level = level,
        )
        scheduleNotification(boxId, level, boxName, time, period)
    }

    fun setAllReminders() {
        homeScreenViewModel.viewModelScope.launch {
            for (box in uiBoxList.boxList) {
                if (box.reminders) {
                    for (level in 0..<NUMBER_OF_LEVELS) {
                        if (homeScreenViewModel.getNumberOfCardsOfLevelInBox(box.boxId, level) != 0)
                            setReminder(
                                boxId = box.boxId,
                                boxName = box.name,
                                level = level
                            )
                    }
                }
            }
        }
    }

    val boxList =
        uiBoxList.boxList.sortedWith(
            when (sortedBy) {
                HomeScreenSorting.CREATED_DESC -> {
                    compareBy<Box> { it.dateAdded }
                }

                HomeScreenSorting.CREATED_ASC -> {
                    compareBy<Box> { it.dateAdded }.reversed()
                }

                HomeScreenSorting.NAME_ASC -> {
                    compareBy<Box> { it.name }
                }

                HomeScreenSorting.NAME_DESC -> {
                    compareBy<Box> { it.name }.reversed()
                }

                HomeScreenSorting.TOPIC -> {
                    compareBy<Box> { it.topic }
                }
            }
        )

    fun endTutorial() {
        tutorial = false
        tutorialStep = -1
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeScreenTopBar(
                homeScreenState = homeScreenState,
                isSelecting = isSelecting,
                tutorial = tutorial,
                goToMainScreen = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN) },
                goToSettings = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.SETTINGS) },
                goToStatistics = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.STATISTICS) },
                showAboutApp = { showAboutApp = true },
                importBox = importBox,
                onSortBy = { homeScreenViewModel.setSortedBy(it) },
                stopSelecting = {
                    isSelecting = false
                    selectedBoxes = listOf()
                },
                startTutorial = {
                    homeScreenViewModel.setSortedBy(HomeScreenSorting.CREATED_ASC)
                    tutorial = true
                    tutorialStep = 1
                },
                endTutorial = { endTutorial() }
            )
        },

        floatingActionButton = {
            if (homeScreenState is HomeScreenState.MAIN) {
                if (isSelecting) {
                    Column {
                        /** Deleting boxes */
                        if (selectedBoxes.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = {
                                    if (selectedBoxes.size == 1) {
                                        homeScreenViewModel.setCurrentBox(selectedBoxes.first())
                                        deleteBoxDialog = true
                                    } else {
                                        deleteBoxesDialog = true
                                    }
                                },
                                modifier = modifier
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }

                        /** Merging boxes */
                        if (selectedBoxes.size >= 2) {
                            FloatingActionButton(
                                onClick = { mergeBoxesDialog = true },
                                modifier = modifier
                                    .padding(top = 10.dp)
                                    .rotate(90F)
                            ) {
                                Icon(Icons.Default.Merge, contentDescription = "Merge")
                            }
                        }
                    }
                } else /** Adding a new box */ {
                    val boxModifier =
                        if (tutorial && tutorialState == TutorialState.ADD_BOX_INTRO) {
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
                            modifier = modifier,
                            onClick = {
                                if (tutorial) {
                                    tutorialStep += 1
                                }
                                homeScreenViewModel.resetCurrentBox()
                                addBoxDialog = true
                            },
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when (homeScreenState) {
            HomeScreenState.MAIN -> {
                BoxList(
                    modifier = modifier.padding(innerPadding),
                    boxList = boxList,
                    isSelecting = isSelecting,
                    selectedBoxes = selectedBoxes,
                    reminderIntervals = reminderIntervals,
                    navigateToBoxScreen = { navigateToBoxScreen(it, tutorial) },
                    startSelection = {
                        if (!isSelecting) {
                            isSelecting = true
                        }
                    },
                    selectBox = {
                        selectedBoxes =
                            if (selectedBoxes.contains(it)) {
                                selectedBoxes.minus(it)
                            } else {
                                selectedBoxes.plus(it)
                            }
                    },
                )
            }

            HomeScreenState.SETTINGS -> {
                SettingsScreen(
                    modifier = modifier.padding(innerPadding),
                    hasNotificationPermission = hasNotificationPermission,
                    userName = userName,
                    globalReminders = globalReminders,
                    reminderIntervals = reminderIntervals,
                    reminderTime = reminderTime,
                    openUserNameDialog = {
                        homeScreenViewModel.updateUiUserName(userName)
                        userNameDialog = true
                    },
                    openRemindersDialog = {
                        homeScreenViewModel.updateCurrentLevel(it)
                        homeScreenViewModel.updateUiReminderIntervals(reminderIntervals)
                        reminderIntervalsDialog = true
                    },
                    openRemindersTimeDialog = { reminderTimeDialog = true },
                    cancelAllNotifications = cancelAllNotifications,
                    requestNotificationPermission = requestNotificationPermission,
                    changeGlobalReminders = { homeScreenViewModel.changeGlobalReminders() },
                    setAllReminders = { setAllReminders() }
                )
            }

            HomeScreenState.STATISTICS -> {
                /* TODO: implement statistics screen */
            }
        }
    }

    if (isLoading) {
        LoadingDialog()
    }

    if (tutorial) {
        Tutorial(
            modifier = modifier,
            tutorialState = tutorialState,
            nextStep = { tutorialStep += 1 },
            stopTutorial = { endTutorial() },
        )
    }

    if (addBoxDialog) {
        AddBoxDialog(
            boxUiState = boxUiState,
            tutorial = tutorial,
            tutorialState = tutorialState,
            hasNotificationPermission = hasNotificationPermission,
            requestNotificationPermission = requestNotificationPermission,
            onDismiss = {
                addBoxDialog = false
                homeScreenViewModel.resetBoxUiState()
                if (tutorial) {
                    endTutorial()
                }
            },
            onSave = {
                addBoxDialog = false
                homeScreenViewModel.saveBox()
                homeScreenViewModel.resetBoxUiState()
                if (tutorial) {
                    tutorialStep += 1
                }
            },
            updateUiState = { homeScreenViewModel.updateBoxUiState(it) },
            nextTutorialStep = { tutorialStep += 1 }
        )
    }

    if (deleteBoxDialog) {
        DeleteBoxDialog(
            boxToBeDeleted = currentBox,
            onDismiss = {
                deleteBoxDialog = false
                homeScreenViewModel.resetCurrentBox()
            },
            onDelete = {
                homeScreenViewModel.viewModelScope.launch {
                    deleteAllMemos(uiBoxWithCards.cardList)
                    homeScreenViewModel.deleteBox(currentBox.boxId)
                    homeScreenViewModel.resetCurrentBox()
                }
                deleteBoxDialog = false
                isSelecting = false
                selectedBoxes = listOf()
            },
        )
    }

    if (deleteBoxesDialog) {
        DeleteBoxesDialog(
            boxesToBeDeleted = selectedBoxes,
            onDismiss = {
                isSelecting = false
                deleteBoxesDialog = false
                selectedBoxes = listOf()
            },
            onDelete = {
                homeScreenViewModel.viewModelScope.launch {
                    for (box in selectedBoxes) {
                        homeScreenViewModel.setCurrentBox(box)
                        delay(100)
                        deleteAllMemos(uiBoxWithCards.cardList)
                        homeScreenViewModel.deleteBox(currentBox.boxId)
                    }
                    homeScreenViewModel.resetCurrentBox()
                }
                deleteBoxesDialog = false
                isSelecting = false
                selectedBoxes = listOf()
            },
        )
    }

    if (mergeBoxesDialog) {
        MergeBoxesDialog(
            selectedBoxes = selectedBoxes,
            onDismiss = {
                mergeBoxesDialog = false
                isSelecting = false
                selectedBoxes = listOf()
            },
            onFinish = {
                    deleteOldBoxes,
                    newBoxName,
                    newDescription,
                    newTopic,
                    transferCards,
                    transferTags,
                    transferCategories,
                    transferMemos,
                    keepLevels,
                ->

                isSelecting = false
                mergeBoxesDialog = false
                homeScreenViewModel.mergeBoxes(
                    oldBoxes = selectedBoxes,
                    deleteOldBoxes = deleteOldBoxes,
                    newBoxName = newBoxName,
                    newDescription = newDescription,
                    newTopic = newTopic,
                    transferCards = transferCards,
                    transferTags = transferTags,
                    transferCategories = transferCategories,
                    transferMemos = transferMemos,
                    keepLevels = keepLevels,
                )
                selectedBoxes = listOf()
            }
        )
    }

    if (showAboutApp) {
        AboutAppDialog(
            modifier = modifier,
            onDismiss = { showAboutApp = false }
        )
    }

    if (userNameDialog) {
        UserNameDialog(
            uiUserName = uiUserName,
            onDismiss = {
                userNameDialog = false
                homeScreenViewModel.resetUiUserName()
            },
            updateUiUserName = { homeScreenViewModel.updateUiUserName(it) },
            applyChanges = {
                userNameDialog = false
                homeScreenViewModel.saveUserName(doReset = true)
            }
        )
    }

    if (reminderIntervalsDialog) {
        ReminderIntervalsDialog(
            currentLevel = currentLevel,
            uiReminderIntervals = uiReminderIntervals,
            onDismiss = {
                reminderIntervalsDialog = false
                homeScreenViewModel.resetUiReminderIntervals()
                homeScreenViewModel.resetCurrentLevel()
            },
            updateUiReminderIntervals = { int, str ->
                /** is a bit ugly but cannot do copy and replace one item in-place */
                val copy = uiReminderIntervals.reminderIntervals.toMutableList()
                copy[currentLevel] = Pair(int, str)

                homeScreenViewModel.updateUiReminderIntervals(intervals = copy)
            },
            applyChanges = {
                reminderIntervalsDialog = false
                homeScreenViewModel.saveReminderIntervals(doReset = true)
            }
        )
    }

    if (reminderTimeDialog) {
        ReminderTimeDialog(
            initialHour = reminderTime.first,
            initialMinute = reminderTime.second,
            onDismiss = {
                reminderTimeDialog = false
            },
            applyChanges = { hour, minute ->
                reminderTimeDialog = false
                homeScreenViewModel.saveReminderTime(hour, minute)
            }
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}