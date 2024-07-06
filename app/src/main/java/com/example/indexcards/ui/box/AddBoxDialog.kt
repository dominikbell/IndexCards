package com.example.indexcards.ui.box

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
import com.example.indexcards.ui.home.DescriptionField
import com.example.indexcards.ui.home.IsLanguageCheckBox
import com.example.indexcards.ui.home.LanguageDropDownMenu
import com.example.indexcards.ui.home.NameField
import com.example.indexcards.ui.home.RemindersSwitch
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.ui.home.TopicField
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {}
) {
    val addBoxUiState = homeScreenViewModel.boxUiState
    val isEnabled = addBoxUiState.boxDetails.reminders

    fun onDismiss() {
        hideDialog()
        homeScreenViewModel.resetUiState()
    }

    var isLanguage by remember { mutableStateOf(true) }

    fun changeIsLanguage() {
        isLanguage = !isLanguage
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.add_new_box),
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

                IsLanguageCheckBox(modifier = modifier, isLanguage = isLanguage) {
                    homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = ""))
                    changeIsLanguage()
                }

                DescriptionField(
                    boxUiState = addBoxUiState,
                    onValueChange = {
                        homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(description = it))
                    }
                )

                RequiredFieldsText()

                RemindersSwitch(
                    modifier = modifier,
                    enabled = (isEnabled && hasNotificationPermission),
                    onCheckedChange = {
                        homeScreenViewModel.updateUiState(addBoxUiState.boxDetails.copy(reminders = !isEnabled))
                    },
                    hasNotificationPermission = hasNotificationPermission,
                    requestNotificationPermission = { requestNotificationPermission() }
                )
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
                Text(text = stringResource(R.string.save))
            }
        },

        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun AddBoxDialogPreview() {
    AddBoxDialog(
        hideDialog = { },
        homeScreenViewModel = viewModel(
            factory = ViewModelProvider(context = LocalContext.current).factory
        )
    )
}