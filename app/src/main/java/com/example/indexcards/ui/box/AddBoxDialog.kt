package com.example.indexcards.ui.box

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.home.DescriptionField
import com.example.indexcards.ui.home.IsLanguageRadioButton
import com.example.indexcards.ui.home.LanguageDropDownMenu
import com.example.indexcards.ui.home.NameField
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.ui.home.TopicField
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    )
) {
    val addBoxUiState = homeScreenViewModel.boxUiState

    fun onDismiss() {
        hideDialog()
        homeScreenViewModel.resetUiState()
    }

    var isLanguage by remember { mutableStateOf(true) }

    fun changeIsLanguage() {
        isLanguage = !isLanguage
    }

    /* TODO: doesn't work */
    BackHandler { onDismiss() }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
        title = {
            Text(
                text = "Add a new box",
            )
        },
        text = {
            Column(
                modifier = modifier
            ) {
                NameField(
                    boxUiState = addBoxUiState,
                    onValueChange = {
                        homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(name = it))
                    }
                )

                if (!isLanguage) {
                    TopicField(
                        boxUiState = addBoxUiState,
                        onValueChange = {
                            homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = it))
                        }
                    )
                } else {
                    LanguageDropDownMenu(
                        modifier = Modifier,
                        boxUiState = addBoxUiState,
                        onValueChange = {
                            homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = it))
                        }
                    )
                }

                DescriptionField(
                    boxUiState = addBoxUiState,
                    onValueChange = {
                        homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(description = it))
                    }
                )

                RequiredFieldsText()

                IsLanguageRadioButton(modifier = modifier, isLanguage = isLanguage) {
                    homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = ""))
                    changeIsLanguage()
                }
            }
        },

        confirmButton = {
            TextButton(onClick = {
                /* TODO: When text fields are empty, don't discard but make fields red */
                homeScreenViewModel.viewModelScope.launch {
                    homeScreenViewModel.saveBox()
                }
                onDismiss()
            }) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    )
}
