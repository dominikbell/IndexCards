package com.example.indexcards.ui.home

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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.box.BoxList
import com.example.indexcards.ui.box.BoxesOverviewTopBar
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.ui.box.AddBoxDialog
import com.example.indexcards.ui.box.DeleteBoxDialog
import com.example.indexcards.utils.box.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val homeScreenUiState by homeScreenViewModel.uiBoxList.collectAsState()

    var addDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    BackHandler {}

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
                    homeScreenViewModel.getBoxToBeDeleted(it)
                }
                deleteDialog = true
            },
            navigateToBoxScreen = navigateToBoxScreen,
        )
    }

    if (addDialog) {
        AddBoxDialog(
            hideDialog = { addDialog = false }
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = {
                deleteDialog = false
                homeScreenViewModel.resetBoxToBeDeleted()
            },
            onDelete = {
                deleteDialog = false
                homeScreenViewModel.viewModelScope.launch {
                    homeScreenViewModel.deleteBox(homeScreenViewModel.boxToBeDeleted.boxId)
                    homeScreenViewModel.resetBoxToBeDeleted()
                }
            },
            boxToBeDeleted = homeScreenViewModel.boxToBeDeleted
        )
    }
}
