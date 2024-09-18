package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.ui.elements.CustomDialog
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.IsLanguageCheckBox
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.home.TutorialState
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.toBoxDetails


@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    tutorial: Boolean,
    tutorialState: TutorialState,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Boolean = { false },
    onDismiss: () -> Unit = {},
    updateUiState: (BoxDetails) -> Unit = {},
    onSave: () -> Unit = {},
    nextTutorialStep: () -> Unit = {},
) {
    val reminders = boxUiState.boxDetails.reminders

    var isLanguage by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var collapseDialog by remember { mutableStateOf(true) }

    var validName by remember { mutableStateOf(true) }
    var validTopic by remember { mutableStateOf(true) }

    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
    val highlightModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(highlightColor)
        .padding(2.dp)

    CustomDialog(
        onDismissRequest = {
            if (collapseDialog) {
                if (!tutorial) {
                    onDismiss()
                }
            } else {
                collapseDialog = true
            }
        },
        title = stringResource(R.string.add_new_box),
        text = {
            Column(
                modifier = modifier
            ) {
                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_NAME) highlightModifier else Modifier,
                ) {
                    NameField(
                        boxUiState = boxUiState,
                        isError = !validName,
                        onValueChange = {
                            validName = true
                            updateUiState(boxUiState.boxDetails.copy(name = it))
                        }
                    )
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_TOPIC) highlightModifier else Modifier,
                ) {
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
                }


                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_CHECK_BOX) highlightModifier else Modifier,
                ) {
                    IsLanguageCheckBox(modifier = modifier, isLanguage = isLanguage) {
                        updateUiState(boxUiState.boxDetails.copy(topic = ""))
                        isLanguage = !isLanguage
                    }
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_DESCRIPTION) highlightModifier else Modifier,
                ) {
                    DescriptionField(
                        boxUiState = boxUiState,
                        onValueChange = { updateUiState(boxUiState.boxDetails.copy(description = it)) }
                    )
                }

                RequiredFieldsText()

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_REMINDER) highlightModifier else Modifier,
                ) {
                    RemindersSwitch(
                        modifier = modifier,
                        enabled = (reminders && hasNotificationPermission),
                        onCheckedChange = { updateUiState(boxUiState.boxDetails.copy(reminders = !reminders)) },
                        hasNotificationPermission = hasNotificationPermission,
                        requestNotificationPermission = requestNotificationPermission
                    )
                }
            }
        },

        confirmButton = {
            Box(
                modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE) highlightModifier else Modifier,
            ) {
                TextButton(
                    onClick = {
                        if (boxUiState.isValid) {
                            onSave()
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
            }
        },

        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },

        tutorial = tutorial,
        tutorialText = {
            when (tutorialState) {
                TutorialState.ADD_BOX_DIALOG -> {
                    Text(text = "In this dialog you can add all important information for your new box.")
                }

                TutorialState.ADD_BOX_DIALOG_NAME -> {
                    Text(text = "First, give your new box a name.")
                }

                TutorialState.ADD_BOX_DIALOG_TOPIC -> {
                    Text(text = "Choose the language that you want to learn.")
                }

                TutorialState.ADD_BOX_DIALOG_CHECK_BOX -> {
                    Text(text = "If your box is not for learning a language, you can disable the check box and type in the topic in the field above.")
                }

                TutorialState.ADD_BOX_DIALOG_DESCRIPTION -> {
                    Text(text = "Here you can add a description for your new box.")
                }

                TutorialState.ADD_BOX_DIALOG_REMINDER -> {
                    Text(text = "If you want to receive notifications about training reminders, switch them on here.")
                }

                TutorialState.ADD_BOX_DIALOG_SAVE -> {
                    Text(text = "Finally, save the new box you've just created.")
                }

                else -> {}
            }
        },

        tutorialConfirmButton = {
            if (tutorialState != TutorialState.ADD_BOX_DIALOG_SAVE) {
                TextButton(
                    onClick = nextTutorialStep
                ) {
                    Text(text = "Next")
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AddBoxDialogPreview() {
    AddBoxDialog(
        boxUiState = BoxState(
            emptyBox.copy(
                name = "Box1223",
                topic = "Birdwatching",
                description = "schrebeibung"
            ).toBoxDetails()
        ),
        tutorial = false,
        tutorialState = TutorialState.OFF
    )
}

@Preview(showBackground = true)
@Composable
fun AddBoxDialogNameTutorialPreview() {
    AddBoxDialog(
        boxUiState = BoxState(
            emptyBox.copy(
                name = "Box1223",
                topic = "Birdwatching",
                description = "schrebeibung"
            ).toBoxDetails()
        ),
        tutorial = true,
        tutorialState = TutorialState.ADD_BOX_DIALOG_TOPIC
    )
}