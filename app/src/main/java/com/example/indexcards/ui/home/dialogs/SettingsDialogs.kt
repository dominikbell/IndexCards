package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import com.example.indexcards.utils.home.SettingsDetails
import com.example.indexcards.utils.home.UiSettings

@Composable
fun UserNameDialog(
    modifier: Modifier = Modifier,
    uiSettings: UiSettings,
    onDismiss: () -> Unit = {},
    updateUiState: (String) -> Unit = {},
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
                    value = uiSettings.settingsDetails.userName,
                    onValueChange = { updateUiState(it) }
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
        uiSettings = UiSettings(
            SettingsDetails(userName = "")
        )
    )
}

@Preview
@Composable
fun UserNameDialogPreview() {
    UserNameDialog(
        uiSettings = UiSettings(
            SettingsDetails(userName = "Herbert")
        )
    )
}

@Composable
fun ReminderDialog(
    modifier: Modifier = Modifier,
    currentLevel: Int,
    uiSettings: UiSettings,
    onDismiss: () -> Unit = {},
    updateUiState: (Int, String) -> Unit = { int, str -> },
    applyChanges: () -> Unit = {},
) {
    val text: String =
        if (uiSettings.settingsDetails.reminderIntervals[currentLevel].first == -1) {
            ""
        } else {
            uiSettings.settingsDetails.reminderIntervals[currentLevel].first.toString()
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
                            updateUiState(
                                -1,
                                uiSettings.settingsDetails.reminderIntervals[currentLevel].second
                            )
                        } else {
                            if (it.isDigitsOnly()) {
                                updateUiState(
                                    it.toInt(),
                                    uiSettings.settingsDetails.reminderIntervals[currentLevel].second
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                PeriodSelect(
                    selectedAmount = uiSettings.settingsDetails.reminderIntervals[currentLevel].first,
                    selectedPeriod = uiSettings.settingsDetails.reminderIntervals[currentLevel].second,
                    onClick = {
                        updateUiState(
                            uiSettings.settingsDetails.reminderIntervals[currentLevel].first, it
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
    ReminderDialog(
        currentLevel = 2,
        uiSettings = UiSettings()
    )
}

@Preview
@Composable
fun PluralReminderDialogPreview() {
    ReminderDialog(
        currentLevel = 1,
        uiSettings = UiSettings()
    )
}