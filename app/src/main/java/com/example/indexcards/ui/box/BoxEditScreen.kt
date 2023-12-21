package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.AddBoxViewModel

@Composable
fun BoxEditScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: () -> Unit,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxEditTopBar(
                modifier = modifier,
                navigateToBoxScreen
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* TODO: Dialog to create new box */
                }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxEditBody(
            modifier = modifier
                .padding(innerPadding)
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
    addBoxViewModel: AddBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
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
            Text(text = "Editing a box")
        }
    )
}