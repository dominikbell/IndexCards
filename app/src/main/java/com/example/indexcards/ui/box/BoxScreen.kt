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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import com.example.indexcards.utils.box.toBox
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    navigateToEditBoxScreen: (Long) -> Unit,
    boxId: Long,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxUiState = editBoxViewModel.boxUiState

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                navigateToEditBoxScreen = { navigateToEditBoxScreen(boxId) },
                thisBox = boxUiState.boxDetails.toBox()
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
            thisBox = boxUiState.boxDetails.toBox()
        )
    }
}

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    thisBox: Box,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val numberOfCards = editBoxViewModel.numberOfCards.collectAsState()

    Column(
        modifier = modifier
            .padding(6.dp)
    ) {
        Text(
            text = "Description: ${thisBox.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = "Number of Cards in this box: ${numberOfCards.value}"
        )

        /* TODO: add CardList here */
        Text(
            text = "Here will be an list of all the cards in this box",
            textAlign = TextAlign.Start,
        )
    }
}