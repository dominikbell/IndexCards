package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.AddBoxViewModel
import kotlinx.coroutines.launch

@Composable
fun EditBoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    navigateToBoxesOverview: () -> Unit,
    boxId: Long,
    boxEditBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    var deleteDialog by remember { mutableStateOf(false) }
    val boxUiState = boxEditBoxViewModel.boxUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxEditTopBar(
                modifier = modifier,
                navigateToBoxScreen = { navigateToBoxScreen(boxId) },
                thisBox = boxUiState.value.box
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* TODO: Dialog to delete this box */
                    deleteDialog = true
                }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    ) { innerPadding ->
        BoxEditBody(
            modifier = modifier
                .padding(innerPadding)
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = {
                deleteDialog = false
            },
            deleteBox = {
                coroutineScope.launch {
                    deleteDialog = false
                    navigateToBoxesOverview()
                    boxEditBoxViewModel.deleteBox()
                }
            },
            boxToBeDeleted = boxUiState.value.box
        )
    }
}

@Composable
fun BoxEditBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Here will be the body")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxEditTopBar(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: () -> Unit,
    thisBox: Box,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = navigateToBoxScreen
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow"
                )
            }
        },
        title = {
            Text(text = "Editing box ${thisBox.name}")
        }
    )
}