package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.utils.AppViewModelProvider

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    navigateToEditBoxScreen: (Long) -> Unit,
    boxId: Long,
    boxEditBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxUiState = boxEditBoxViewModel.boxUiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                navigateToEditBoxScreen = { navigateToEditBoxScreen(boxId) },
                thisBox = boxUiState.value.box
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* TODO: Dialog to create new card */
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxScreenBody(
            modifier = modifier
                .padding(innerPadding),
            thisBox = boxUiState.value.box
        )
    }
}

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    thisBox: Box
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Description: ${thisBox.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        /* TODO: add CardList here */
        Text(
            text = "Here will be an list of all the cards in this box",
            textAlign = TextAlign.Start,
        )
    }
}