package com.example.indexcards.ui.home.dialogs

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.IsLanguageCheckBox
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.box.BoxDetails
import com.example.indexcards.utils.box.BoxState
import com.example.indexcards.utils.box.emptyBox
import com.example.indexcards.utils.box.toBoxDetails

@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Boolean = { false },
    onDismiss: () -> Unit = {},
    updateUiState: (BoxDetails) -> Unit = {},
    onSave: () -> Unit = {},
) {
    val reminders = boxUiState.boxDetails.reminders

    var isLanguage by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var collapseDialog by remember { mutableStateOf(true) }

    var validName by remember { mutableStateOf(true) }
    var validTopic by remember { mutableStateOf(true) }

    fun changeIsLanguage() {
        isLanguage = !isLanguage
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            if (collapseDialog) {
                onDismiss()
            } else {
                collapseDialog = true
            }
        },
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
                    boxUiState = boxUiState,
                    isError = !validName,
                    onValueChange = {
                        validName = true
                        updateUiState(boxUiState.boxDetails.copy(name = it))
                    }
                )

                if (!isLanguage) {
                    TopicField(
                        boxUiState = boxUiState,
                        isError = !validTopic,
                        onValueChange = {
                            validTopic = true
                            updateUiState(boxUiState.boxDetails.copy(topic = it))
                        }
                    )
                } else {
                    LanguageDropDownMenu(
                        modifier = Modifier,
                        boxUiState = boxUiState,
                        expanded = expanded,
                        isError = !validTopic,
                        changeExpanded = {
                            expanded = !expanded
                            collapseDialog = false
                        },
                        onValueChange = {
                            validTopic = true
                            updateUiState(boxUiState.boxDetails.copy(topic = it))
                        },
                    )
                }

                IsLanguageCheckBox(modifier = modifier, isLanguage = isLanguage) {
                    updateUiState(boxUiState.boxDetails.copy(topic = ""))
                    changeIsLanguage()
                }

                DescriptionField(
                    boxUiState = boxUiState,
                    onValueChange = { updateUiState(boxUiState.boxDetails.copy(description = it)) }
                )

                RequiredFieldsText()

                RemindersSwitch(
                    modifier = modifier,
                    enabled = (reminders && hasNotificationPermission),
                    onCheckedChange = { updateUiState(boxUiState.boxDetails.copy(reminders = !reminders)) },
                    hasNotificationPermission = hasNotificationPermission,
                    requestNotificationPermission = requestNotificationPermission
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (boxUiState.isValid) {
                        onSave()
                        onDismiss()
                    } else {
                        if (!boxUiState.validName) {
                            validName = false
                        }
                        if (!boxUiState.validTopic) {
                            validTopic = false
                        }
                    }
                }
            ) {
                Text(text = stringResource(R.string.save))
            }
        },

        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun AddBoxDialogPreview() {
    AddBoxDialog(
        boxUiState = BoxState(
            emptyBox.copy(
                name = "Box123",
                topic = "Birdwatching",
                description = "schrebeibung"
            ).toBoxDetails()
        )
    )
}