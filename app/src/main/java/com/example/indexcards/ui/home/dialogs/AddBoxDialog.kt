package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.Color
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
    endTutorial: () -> Unit = {},
) {
    val reminders = boxUiState.boxDetails.reminders

    var isLanguage by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var collapseDialog by remember { mutableStateOf(true) }

    var validName by remember { mutableStateOf(true) }
    var validTopic by remember { mutableStateOf(true) }

    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2F)
    val highlightModifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(highlightColor)
        .padding(2.dp)
    val mutedColor = LocalContentColor.current.copy(alpha = 0.38F) // 38% is the android standard

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
                        },
                        isEnabled =
                            (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_NAME || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE),
                    )
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_TOPIC) highlightModifier else Modifier,
                ) {
                    val dropDownMenuIsEnabled =
                        (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_TOPIC || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE)

                    if (!isLanguage) {
                        TopicField(
                            boxUiState = boxUiState,
                            isError = !validTopic,
                            onValueChange = {
                                validTopic = true
                                updateUiState(boxUiState.boxDetails.copy(topic = it))
                            },
                            isEnabled = dropDownMenuIsEnabled,
                        )
                    } else {
                        LanguageDropDownMenu(
                            modifier = Modifier,
                            boxUiState = boxUiState,
                            expanded = expanded,
                            isError = !validTopic,
                            changeExpanded = {
                                if (dropDownMenuIsEnabled) {
                                    expanded = !expanded
                                    collapseDialog = false
                                }
                            },
                            onValueChange = {
                                validTopic = true
                                updateUiState(boxUiState.boxDetails.copy(topic = it))
                            },
                            isEnabled = dropDownMenuIsEnabled,
                        )
                    }
                }


                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_CHECK_BOX) highlightModifier else Modifier,
                ) {
                    val checkBoxIsEnabled =
                        (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_CHECK_BOX || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE)

                    IsLanguageCheckBox(
                        modifier = modifier,
                        isLanguage = isLanguage,
                        isEnabled = checkBoxIsEnabled,
                        textColor = if (checkBoxIsEnabled) Color.Unspecified else mutedColor,
                        changeIsLanguage = {
                            if (checkBoxIsEnabled) {
                                updateUiState(boxUiState.boxDetails.copy(topic = ""))
                                isLanguage = !isLanguage
                            }
                        }
                    )
                }

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_DESCRIPTION) highlightModifier else Modifier,
                ) {
                    DescriptionField(
                        boxUiState = boxUiState,
                        onValueChange = { updateUiState(boxUiState.boxDetails.copy(description = it)) },
                        isEnabled = (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_DESCRIPTION || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE),
                    )
                }

                RequiredFieldsText(
                    textColor = if (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE) Color.Unspecified else mutedColor
                )

                Box(
                    modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_REMINDER) highlightModifier else Modifier,
                ) {
                    val switchEnabled =
                        (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_REMINDER || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE)

                    RemindersSwitch(
                        modifier = modifier,
                        checked = (reminders && hasNotificationPermission),
                        hasNotificationPermission = hasNotificationPermission,
                        isEnabled = switchEnabled,
                        textColor = if (switchEnabled) Color.Unspecified else mutedColor,
                        onCheckedChange = {
                            if (switchEnabled) {
                                updateUiState(boxUiState.boxDetails.copy(reminders = !reminders))
                            }
                        },
                        requestNotificationPermission = requestNotificationPermission,
                    )
                }
            }
        },

        confirmButton = {
            val saveButtonEnabled =
                (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE)
            Box(
                modifier = if (tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE) highlightModifier else Modifier,
            ) {
                TextButton(
                    enabled = saveButtonEnabled,
                    onClick = {
                        if (saveButtonEnabled) {
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
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        },

        dismissButton = {
            val cancelButtonEnabled =
                (!tutorial || tutorialState == TutorialState.ADD_BOX_DIALOG_SAVE)

            TextButton(
                enabled = cancelButtonEnabled,
                onClick = {
                    if (tutorial) {
                        endTutorial()
                    }
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },

        tutorial = tutorial,
        tutorialText = {
            when (tutorialState) {
                TutorialState.ADD_BOX_DIALOG -> {
                    Text(text = stringResource(id = R.string.add_box_dialog))
                }

                TutorialState.ADD_BOX_DIALOG_NAME -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_name))
                }

                TutorialState.ADD_BOX_DIALOG_TOPIC -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_topic))
                }

                TutorialState.ADD_BOX_DIALOG_CHECK_BOX -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_check_box))
                }

                TutorialState.ADD_BOX_DIALOG_DESCRIPTION -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_description))
                }

                TutorialState.ADD_BOX_DIALOG_REMINDER -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_reminder))
                }

                TutorialState.ADD_BOX_DIALOG_SAVE -> {
                    Text(text = stringResource(id = R.string.add_box_dialog_save))
                }

                else -> {}
            }
        },

        tutorialConfirmButton = {
            if (tutorialState != TutorialState.ADD_BOX_DIALOG_SAVE) {
                TextButton(
                    onClick = nextTutorialStep
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        },

        tutorialDismissButton = {
            TextButton(
                onClick = endTutorial
            ) {
                Text(text = stringResource(R.string.end_tutorial))
            }
        },
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
        tutorialState = TutorialState.OFF,
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