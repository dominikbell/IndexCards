package com.example.indexcards.ui.box

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
import com.example.indexcards.data.LanguageData
import com.example.indexcards.ui.home.DescriptionField
import com.example.indexcards.ui.home.LanguageDropDownMenu
import com.example.indexcards.ui.home.NameField
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.ui.home.TopicField
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import kotlinx.coroutines.launch

@Composable
fun EditBoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    navigateToBoxesOverview: () -> Unit,
    boxId: Long,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val thisBox = editBoxViewModel.currentBox

    var deleteDialog by remember { mutableStateOf(false) }

    BackHandler { navigateToBoxScreen(thisBox.boxId) }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxEditTopBar(
                navigateToBoxScreen = {
                    navigateToBoxScreen(boxId)
                },
                boxName = thisBox.name
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    deleteDialog = true
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    ) { innerPadding ->
        BoxEditBody(
            modifier = modifier
                .padding(innerPadding),
            onSave = {
                navigateToBoxScreen(boxId)
                editBoxViewModel.viewModelScope.launch {
                    editBoxViewModel.saveBox()
                }
            },
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = {
                deleteDialog = false
            },
            onDelete = {
                navigateToBoxesOverview()
                editBoxViewModel.viewModelScope.launch {
                    editBoxViewModel.deleteBox(boxId)
                }
            },
            boxToBeDeleted = thisBox
        )
    }
}

@Composable
fun BoxEditBody(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxUiState = editBoxViewModel.boxUiState
    val isLanguage = (boxUiState.boxDetails.topic in LanguageData.language.values)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NameField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = { editBoxViewModel.updateUiState(boxUiState.boxDetails.copy(name = it)) }
        )
        if (isLanguage) {
            LanguageDropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { editBoxViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        } else {
            TopicField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { editBoxViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        }

        DescriptionField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = { editBoxViewModel.updateUiState(boxUiState.boxDetails.copy(description = it)) }
        )

        RequiredFieldsText()

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = {
                /* TODO: Only save valid entries -> BoxState.isValid */
                onSave()
            }
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxEditTopBar(
    navigateToBoxScreen: () -> Unit,
    boxName: String,
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
            Text(text = stringResource(R.string.editing_box) + " '$boxName'")
        }
    )
}