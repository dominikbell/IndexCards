package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.indexcards.R
import com.example.indexcards.ui.elements.PeriodSelect
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.home.UiReminderIntervals
import com.example.indexcards.utils.home.UiUserName
import kotlin.math.max

@Composable
fun UserNameDialog(
    modifier: Modifier = Modifier,
    uiUserName: UiUserName,
    onDismiss: () -> Unit = {},
    updateUiUserName: (String) -> Unit = {},
    applyChanges: () -> Unit = {},
) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.enter_name))
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.weight(1F),
                    text = stringResource(id = R.string.my_name) + ":"
                )

                TextField(
                    modifier = Modifier.weight(1F),
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(0.6F),
                            text = stringResource(id = R.string.username),
                        )
                    },
                    value = uiUserName.userName,
                    onValueChange = { updateUiUserName(it) }
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = applyChanges
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun EmptyUserNameDialogPreview() {
    UserNameDialog(
        uiUserName = UiUserName(userName = "")
    )
}

@Preview
@Composable
fun UserNameDialogPreview() {
    UserNameDialog(
        uiUserName = UiUserName(userName = "Herbert")
    )
}

@Composable
fun ReminderIntervalsDialog(
    modifier: Modifier = Modifier,
    currentLevel: Int,
    uiReminderIntervals: UiReminderIntervals,
    onDismiss: () -> Unit = {},
    updateUiReminderIntervals: (Int, String) -> Unit = { _, _ -> },
    applyChanges: () -> Unit = {},
) {
    val text: String =
        if (uiReminderIntervals.reminderIntervals[currentLevel].first == -1) {
            ""
        } else {
            uiReminderIntervals.reminderIntervals[currentLevel].first.toString()
        }

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.set_reminders) + " " + (currentLevel + 1).toString())
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.weight(0.1F),
                    value = text,
                    onValueChange = {
                        if (it.isBlank()) {
                            updateUiReminderIntervals(
                                -1,
                                uiReminderIntervals.reminderIntervals[currentLevel].second
                            )
                        } else {
                            if (it.isDigitsOnly()) {
                                updateUiReminderIntervals(
                                    it.toInt(),
                                    uiReminderIntervals.reminderIntervals[currentLevel].second
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                PeriodSelect(
                    selectedAmount = uiReminderIntervals.reminderIntervals[currentLevel].first,
                    selectedPeriod = uiReminderIntervals.reminderIntervals[currentLevel].second,
                    onClick = {
                        updateUiReminderIntervals(
                            uiReminderIntervals.reminderIntervals[currentLevel].first, it
                        )
                    }
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = applyChanges
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun SingularReminderDialogPreview() {
    ReminderIntervalsDialog(
        currentLevel = 2,
        uiReminderIntervals = UiReminderIntervals(
            reminderIntervals = DefaultPreferences.REMINDER_INTERVALS
        )
    )
}

@Preview
@Composable
fun PluralReminderDialogPreview() {
    ReminderIntervalsDialog(
        currentLevel = 1,
        uiReminderIntervals = UiReminderIntervals(
            reminderIntervals = DefaultPreferences.REMINDER_INTERVALS
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimeDialog(
    modifier: Modifier = Modifier,
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit = {},
    applyChanges: (Int, Int) -> Unit = { _, _ -> },
) {
    val timeState = rememberTimePickerState(
        initialHour = max(0, initialHour),
        initialMinute = max(0, initialMinute),
        is24Hour = true
    )

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.set_reminder_time)) },
        text = {

            TimePicker(
                state = timeState,
                layoutType = TimePickerLayoutType.Vertical
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }

                TextButton(
                    onClick = {
                        applyChanges(
                            DefaultPreferences.REMINDER_TIME.first,
                            DefaultPreferences.REMINDER_TIME.second,
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.do_not_use))
                }

                TextButton(
                    onClick = {
                        applyChanges(
                            timeState.hour,
                            timeState.minute
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    )
}

@Preview
@Composable
fun ReminderTimeDialogPreview() {
    ReminderTimeDialog(
        initialHour = 3,
        initialMinute = 2
    )
}

@Preview
@Composable
fun EmptyReminderTimeDialogPreview() {
    ReminderTimeDialog(
        initialHour = -1,
        initialMinute = -1
    )
}