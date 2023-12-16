package com.example.indexcards.ui.home

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.box.BoxList
import com.example.indexcards.ui.box.BoxesOverviewTopBar
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.AddBoxDialog
import com.example.indexcards.utils.box.DeleteBoxDialog
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToBoxScreen: () -> Unit,
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val homeScreenUiState by homeScreenViewModel.homeUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var addDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxesOverviewTopBar {
                navigateToBoxScreen()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addDialog = true
                },
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
            showDelete = { deleteDialog = true }
        )
    }

    if (addDialog) {
        AddBoxDialog(
            onDismiss = { addDialog = false }
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = {
                coroutineScope.launch {
                    homeScreenViewModel.boxToBeDeleted = null
                    deleteDialog = false
                }
            },
            deleteBox = {
                coroutineScope.launch {
                    homeScreenViewModel.deleteBox()
                    deleteDialog = false
                }
            }
        )
    }
}