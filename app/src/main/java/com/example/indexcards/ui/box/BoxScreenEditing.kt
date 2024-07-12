package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.isLanguage
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.box.BoxDetails
import com.example.indexcards.utils.box.BoxState
import com.example.indexcards.utils.box.emptyBox
import com.example.indexcards.utils.box.toBox
import com.example.indexcards.utils.box.toBoxDetails

@Composable
fun BoxScreenEditing(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    globalReminders: Boolean,
    hasNotificationPermission: Boolean,
    changeGlobalReminders: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false },
    onSave: () -> Unit = {},
    updateBoxUiState: (BoxDetails) -> Unit = {},
    setAllReminders: () -> Unit = {},
) {
    val isLanguage = boxUiState.boxDetails.toBox().isLanguage()
    val remindersEnabled = boxUiState.boxDetails.reminders

    fun onSwitchChanged() {
        if (!globalReminders) {
            changeGlobalReminders()
        }
        if (!remindersEnabled) {
            setAllReminders()
        }
        updateBoxUiState(boxUiState.boxDetails.copy(reminders = !remindersEnabled))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NameField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = { updateBoxUiState(boxUiState.boxDetails.copy(name = it)) }
        )
        if (isLanguage) {
            LanguageDropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { updateBoxUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        } else {
            TopicField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { updateBoxUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        }

        DescriptionField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = { updateBoxUiState(boxUiState.boxDetails.copy(description = it)) }
        )

        RequiredFieldsText()

        Spacer(modifier = Modifier.size(8.dp))

        RemindersSwitch(
            modifier = modifier,
            enabled = (remindersEnabled && hasNotificationPermission),
            onCheckedChange = {
                if (!hasNotificationPermission) {
                    val success = requestNotificationPermission()
                    if (success) {
                        onSwitchChanged()
                    }
                } else {
                    onSwitchChanged()

                }
            },
            hasNotificationPermission = hasNotificationPermission,
            requestNotificationPermission = requestNotificationPermission
        )

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

@Preview(showBackground = true)
@Composable
fun BoxScreenEditingPreview() {
    BoxScreenEditing(
        boxUiState = BoxState(
            emptyBox.copy(
                name = "Box1234",
                description = "This is a descreibung",
                topic = "Holz",
                reminders = true
            ).toBoxDetails()
        ),
        globalReminders = true,
        hasNotificationPermission = true
    )
}