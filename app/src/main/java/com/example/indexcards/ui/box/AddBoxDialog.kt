package com.example.indexcards.ui.box

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.indexcards.R
import com.example.indexcards.ui.home.DescriptionField
import com.example.indexcards.ui.home.IsLanguageRadioButton
import com.example.indexcards.ui.home.LanguageDropDownMenu
import com.example.indexcards.ui.home.NameField
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.ui.home.TopicField
import com.example.indexcards.utils.box.HomeScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
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

                IsLanguageRadioButton(modifier = modifier, isLanguage = isLanguage) {
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

                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.reminders))

                    Switch(
                        checked = true,
                        onCheckedChange = {}
                    )
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
