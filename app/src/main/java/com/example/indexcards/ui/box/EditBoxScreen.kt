package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import com.example.indexcards.utils.box.toBox
import kotlinx.coroutines.launch

@Composable
fun EditBoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    navigateToBoxesOverview: () -> Unit,
    boxId: Long,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    var deleteDialog by remember { mutableStateOf(false) }
    val boxUiState = editBoxViewModel.boxUiState
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxEditTopBar(
                navigateToBoxScreen = { navigateToBoxScreen(boxId) },
                thisBox = boxUiState.boxDetails.toBox()
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
                .padding(innerPadding),
            editBoxViewModel = editBoxViewModel,
            onSave = {
                coroutineScope.launch {
                    editBoxViewModel.saveEdit()
                }
                navigateToBoxScreen(boxId)
            }
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
                    editBoxViewModel.deleteBox(boxUiState.boxDetails.id)
                }
            },
            boxToBeDeleted = boxUiState.boxDetails.toBox()
        )
    }
}

@Composable
fun BoxEditBody(
    modifier: Modifier = Modifier,
    editBoxViewModel: EditBoxViewModel,
    onSave: () -> Unit,
) {
    val newBoxState = editBoxViewModel.newBoxState
    val isLanguage = (newBoxState.boxDetails.topic in LanguageData.language.values)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NameField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = newBoxState,
            onValueChange = { editBoxViewModel.updateNewBoxState(newBoxState.boxDetails.copy(name = it)) }
        )
        if (isLanguage) {
            LanguageDropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = newBoxState,
                onValueChange = {
                    editBoxViewModel.updateNewBoxState(
                        newBoxState.boxDetails.copy(
                            topic = it
                        )
                    )
                }
            )
        } else {
            TopicField(
                modifier = modifier.fillMaxWidth(),
                boxUiState = newBoxState,
                onValueChange = {
                    editBoxViewModel.updateNewBoxState(
                        newBoxState.boxDetails.copy(
                            topic = it
                        )
                    )
                }
            )
        }

        DescriptionField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = newBoxState,
            onValueChange = {
                editBoxViewModel.updateNewBoxState(
                    newBoxState.boxDetails.copy(
                        description = it
                    )
                )
            }
        )

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = {
                /* TODO: Only save valid entries -> BoxState.isValid */
                onSave()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxEditTopBar(
    navigateToBoxScreen: () -> Unit,
    thisBox: Box,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
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