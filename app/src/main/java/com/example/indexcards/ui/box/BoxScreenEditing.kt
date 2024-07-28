package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.isLanguage
import com.example.indexcards.utils.state.toBox
import com.example.indexcards.utils.state.toBoxDetails
import java.time.LocalDateTime
import java.time.ZoneOffset

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

    var expanded by remember { mutableStateOf(false) }
    var validName by remember { mutableStateOf(true) }
    var validTopic by remember { mutableStateOf(true) }


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
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NameField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                isError = !validName,
                onValueChange = {
                    validName = true
                    updateBoxUiState(boxUiState.boxDetails.copy(name = it))
                }
            )
            if (isLanguage) {
                LanguageDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    boxUiState = boxUiState,
                    expanded = expanded,
                    changeExpanded = { expanded = !expanded },
                    isError = !validTopic,
                    onValueChange = {
                        validTopic = true
                        updateBoxUiState(boxUiState.boxDetails.copy(topic = it))
                    }
                )
            } else {
                TopicField(
                    modifier = Modifier.fillMaxWidth(),
                    boxUiState = boxUiState,
                    isError = !validTopic,
                    onValueChange = {
                        validTopic = true
                        updateBoxUiState(boxUiState.boxDetails.copy(topic = it))
                    }
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

        if (boxUiState.boxDetails.dateAdded != (-1).toLong()) {
            val date =
                LocalDateTime.ofEpochSecond(boxUiState.boxDetails.dateAdded, 0, ZoneOffset.UTC)
            val day = date.dayOfMonth
            val month = date.month
            val year = date.year
            val hour = date.hour
            val minute = date.minute

            Text(
                modifier = Modifier.padding(
                    bottom = (2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                ),
                text = stringResource(id = R.string.box_created) +
                        ": $day. $month $year "
                        + stringResource(id = R.string.at)
                        + " $hour:$minute."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxScreenEditingPreview() {
    BoxScreenEditing(
        modifier = Modifier.height(600.dp),
        boxUiState = BoxState(
            emptyBox.copy(
                name = "Box1234",
                description = "This is a descreibung",
                topic = "Holz",
                reminders = true,
                dateAdded = 1321390423
            ).toBoxDetails()
        ),
        globalReminders = true,
        hasNotificationPermission = true
    )
}