package com.example.indexcards.ui.home

import android.app.Activity
import android.util.Log
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
import com.example.indexcards.ui.box.BoxesOverviewTopBar
import com.example.indexcards.ui.box.AddBoxDialog
import com.example.indexcards.ui.box.DeleteBoxDialog
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.notification.getTimeInTheFuture
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {},
    scheduleNotification: (Long) -> Unit = {}
) {
    val homeScreenUiState by homeScreenViewModel.uiBoxList.collectAsState()
    val currentBox = homeScreenViewModel.selectedBox.collectAsState()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val backAgainString = stringResource(id = R.string.back_twice_to_close)

    var addDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    var backPressedTime: Long = 0

    BackHandler {
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

    Scaffold(
        modifier = modifier,

        topBar = { BoxesOverviewTopBar() },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { addDialog = true },
                modifier = modifier
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxList(
            modifier = modifier
                .padding(innerPadding),
            boxList = homeScreenUiState.boxList,
            onDelete = {
                homeScreenViewModel.viewModelScope.launch {
                    homeScreenViewModel.setCurrentBox(it)
                }
                deleteDialog = true
            },
            navigateToBoxScreen = navigateToBoxScreen,
        )
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
            boxToBeDeleted = currentBox.value
        )
    }
}
