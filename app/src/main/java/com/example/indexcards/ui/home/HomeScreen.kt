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
import com.example.indexcards.R
import com.example.indexcards.ui.box.AddBoxDialog
import com.example.indexcards.ui.box.DeleteBoxDialog
import com.example.indexcards.ui.elements.AboutAppDialog
import com.example.indexcards.utils.box.HomeScreenState
import com.example.indexcards.utils.box.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {}
) {
    val uiBoxList by homeScreenViewModel.uiBoxList.collectAsState()
    val homeScreenState = homeScreenViewModel.homeScreenState
    val currentBox by homeScreenViewModel.selectedBox.collectAsState()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val backAgainString = stringResource(id = R.string.back_twice_to_close)

    var addDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    var showAboutApp by remember { mutableStateOf(false) }
    var backPressedTime: Long = 0

    BackHandler {
        when (homeScreenState) {
            HomeScreenState.MAIN -> {
                if (addDialog) {
                    addDialog = false
                } else {
                    if (backPressedTime + 3000 > System.currentTimeMillis()) {
                        /* TODO: seems a bit hacky but works */
                        activity?.finish()
                    } else {
                        backPressedTime = System.currentTimeMillis()
                        Toast.makeText(context, backAgainString, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            HomeScreenState.SETTINGS -> {
                homeScreenViewModel.savePreferences()
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }

            HomeScreenState.STATISTICS -> {
                homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN)
            }
        }
    }

    Scaffold(
        modifier = modifier,

        topBar = {
            HomeScreenTopBar(
                homeScreenState = homeScreenState,
                goToMainScreen = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.MAIN) },
                goToSettings = {
                    homeScreenViewModel.setCurrentUiPreferences()
                    homeScreenViewModel.updateHomeScreenState(HomeScreenState.SETTINGS)
                },
                goToStatistics = { homeScreenViewModel.updateHomeScreenState(HomeScreenState.STATISTICS) },
                showAboutApp = { showAboutApp = true },
                saveSettings = { homeScreenViewModel.savePreferences() }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { addDialog = true },
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
                        deleteDialog = true
                    },
                    navigateToBoxScreen = navigateToBoxScreen,
                )
            }

            HomeScreenState.SETTINGS -> {
                SettingsScreen(
                    modifier = modifier.padding(innerPadding),
                    homeScreenViewModel = homeScreenViewModel
                )
            }

            HomeScreenState.STATISTICS -> {}
        }
    }

    if (addDialog) {
        AddBoxDialog(
            hideDialog = { addDialog = false },
            homeScreenViewModel = homeScreenViewModel,
            hasNotificationPermission = hasNotificationPermission,
            requestNotificationPermission = { requestNotificationPermission() }
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = {
                deleteDialog = false
                homeScreenViewModel.resetCurrentBox()
            },
            onDelete = {
                deleteDialog = false
                homeScreenViewModel.viewModelScope.launch {
                    homeScreenViewModel.deleteBox()
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
}
