package com.example.indexcards.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.ui.home.dialogs.AddBoxDialog
import com.example.indexcards.ui.box.dialogs.DeleteBoxDialog
import com.example.indexcards.ui.home.dialogs.AboutAppDialog
import com.example.indexcards.ui.home.dialogs.ReminderDialog
import com.example.indexcards.ui.home.dialogs.UserNameDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.home.HomeScreenState
import com.example.indexcards.utils.home.HomeScreenViewModel
import com.example.indexcards.utils.notification.getTimeFromReminderIntervals
import com.example.indexcards.utils.notification.getTimeInTheFuture
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Boolean = { false },
    navigateToBoxScreen: (Long) -> Unit = {},
    scheduleNotification: (Long, Int, String, Long) -> Unit = { _, _, _, _ -> },
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val uiSettings = homeScreenViewModel.uiSettings
    val currentLevel = homeScreenViewModel.currentLevel
    val boxUiState = homeScreenViewModel.boxUiState
    val homeScreenState = homeScreenViewModel.homeScreenState
    val userName = homeScreenViewModel.userName.collectAsState()
    val globalReminders = homeScreenViewModel.globalReminders.collectAsState()
    val reminderIntervals = homeScreenViewModel.reminderIntervals.collectAsState()
    val uiBoxList by homeScreenViewModel.uiBoxList.collectAsState()
    val currentBox by homeScreenViewModel.currentBox.collectAsState()
    val backAgainString = stringResource(id = R.string.back_twice_to_close)

    var addBoxDialog by remember { mutableStateOf(false) }
    var deleteBoxDialog by remember { mutableStateOf(false) }
    var editUserNameDialog by remember { mutableStateOf(false) }
    var editRemindersDialog by remember { mutableStateOf(false) }
    var showAboutApp by remember { mutableStateOf(false) }
    var backPressedTime: Long = 0

    BackHandler {
        when (homeScreenState) {
            HomeScreenState.MAIN -> {
                if (addBoxDialog) {
                    addBoxDialog = false
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

            HomeScreenState.SETTINGS -> {
                homeScreenViewModel.savePreferences(doReset = true)
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }

            HomeScreenState.STATISTICS -> {
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }
        }
    }

    fun setReminder(boxId: Long, boxName: String, level: Int) {
        val time = getTimeFromReminderIntervals(
            reminderIntervals = reminderIntervals.value,
            level = level
        )
        scheduleNotification(boxId, level, boxName, time)
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

    Scaffold(
        modifier = modifier,

        topBar = {
            HomeScreenTopBar(
                homeScreenState = homeScreenState,
                goToMainScreen = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN) },
                goToSettings = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.SETTINGS) },
                goToStatistics = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.STATISTICS) },
                showAboutApp = { showAboutApp = true },
                saveSettings = { homeScreenViewModel.savePreferences(doReset = true) }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { addBoxDialog = true },
                modifier = modifier
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        when (homeScreenState) {
            HomeScreenState.MAIN -> {
                BoxList(
                    modifier = modifier
                        .padding(innerPadding),
                    boxList = uiBoxList.boxList,
                    onDelete = {
                        homeScreenViewModel.viewModelScope.launch {
                            homeScreenViewModel.setCurrentBox(it)
                        }
                        deleteBoxDialog = true
                    },
                    navigateToBoxScreen = navigateToBoxScreen,
                )
            }

            HomeScreenState.SETTINGS -> {
                SettingsScreen(
                    modifier = modifier.padding(innerPadding),
                    hasNotificationPermission = hasNotificationPermission,
                    userName = userName.value,
                    globalReminders = globalReminders.value,
                    reminderIntervals = reminderIntervals.value,
                    openUserNameDialog = {
                        homeScreenViewModel.updateUiSettings(
                            uiSettings.settingsDetails.copy(
                                userName = userName.value,
                                globalReminders = globalReminders.value,
                                reminderIntervals = reminderIntervals.value
                            )
                        )
                        editUserNameDialog = true
                    },
                    openRemindersDialog = {
                        homeScreenViewModel.updateCurrentLevel(it)
                        homeScreenViewModel.updateUiSettings(
                            uiSettings.settingsDetails.copy(
                                userName = userName.value,
                                globalReminders = globalReminders.value,
                                reminderIntervals = reminderIntervals.value
                            )
                        )
                        editRemindersDialog = true
                    },
                    requestNotificationPermission = requestNotificationPermission,
                    changeGlobalReminders = {
                        homeScreenViewModel.updateUiSettings(
                            uiSettings.settingsDetails.copy(
                                userName = userName.value,
                                globalReminders = !globalReminders.value,
                                reminderIntervals = reminderIntervals.value
                            )
                        )
                        homeScreenViewModel.savePreferences(doReset = true)
                    },
                    setAllReminders = { setAllReminders() }
                )
            }

            HomeScreenState.STATISTICS -> {}
        }
    }

    if (addBoxDialog) {
        AddBoxDialog(
            boxUiState = boxUiState,
            hasNotificationPermission = hasNotificationPermission,
            requestNotificationPermission = requestNotificationPermission,
            onDismiss = {
                addBoxDialog = false
                homeScreenViewModel.resetBoxUiState()
            },
            onSave = { homeScreenViewModel.saveBox() },
            updateUiState = { homeScreenViewModel.updateBoxUiState(it) }
        )
    }

    if (deleteBoxDialog) {
        DeleteBoxDialog(
            onDismiss = {
                deleteBoxDialog = false
                homeScreenViewModel.resetCurrentBox()
            },
            onDelete = {
                deleteBoxDialog = false
                homeScreenViewModel.viewModelScope.launch {
                    homeScreenViewModel.deleteBox(currentBox.boxId)
                    homeScreenViewModel.resetCurrentBox()
                }
            },
            boxToBeDeleted = currentBox
        )
    }

    if (showAboutApp) {
        AboutAppDialog(
            modifier = modifier,
            onDismiss = { showAboutApp = false }
        )
    }

    if (editUserNameDialog) {
        UserNameDialog(
            uiSettings = uiSettings,
            onDismiss = {
                editUserNameDialog = false
                homeScreenViewModel.resetUiSettings()
            },
            updateUiState = {
                homeScreenViewModel.updateUiSettings(
                    uiSettings.settingsDetails.copy(userName = it)
                )
            },
            applyChanges = {
                editUserNameDialog = false
                homeScreenViewModel.savePreferences(doReset = true)
            }
        )
    }

    if (editRemindersDialog) {
        ReminderDialog(
            currentLevel = currentLevel,
            onDismiss = {
                editRemindersDialog = false
                homeScreenViewModel.resetUiSettings()
                homeScreenViewModel.resetCurrentLevel()
            },
            uiSettings = uiSettings,
            updateUiState = { int, str ->
                /* TODO: is a bit ugly but cannot do copy and replace one item */
                val copy = uiSettings.settingsDetails.reminderIntervals.toMutableList()
                copy[currentLevel] = Pair(int, str)
                homeScreenViewModel.updateUiSettings(
                    uiSettings.settingsDetails.copy(
                        reminderIntervals = copy
                    )
                )
            },
            applyChanges = {
                editRemindersDialog = false
                homeScreenViewModel.savePreferences(doReset = true)
            }
        )
    }
}
