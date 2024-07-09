package com.example.indexcards.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.home.toReminderText
import com.example.indexcards.utils.home.toWord

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    hasNotificationPermission: Boolean,
    userName: String,
    globalReminders: Boolean,
    reminderIntervals: List<Pair<Int, String>>,
    openUserNameDialog: () -> Unit = {},
    openRemindersDialog: (Int) -> Unit = {},
    changeGlobalReminders: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false },
) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.username)
            )
            Text(
                modifier = Modifier.clickable {
                    openUserNameDialog()
                },
                text = userName
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.receive_reminders)
            )
            Switch(
                checked = (hasNotificationPermission && globalReminders),
                onCheckedChange = {
                    if (!hasNotificationPermission) {
                        val success = requestNotificationPermission()
                        if (success) {
                            changeGlobalReminders()
                        }
                    } else {
                        changeGlobalReminders()
                    }
                }
            )
        }

        HorizontalDivider()

        reminderIntervals.forEachIndexed { index, interval ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.reminders_for) + " "
                            + (index + 1).toWord() + " "
                            + stringResource(id = R.string.level) + ":"
                )

                Text(
                    modifier = Modifier.clickable {
                        openRemindersDialog(index)
                    },
                    text = interval.second.toReminderText(interval.first)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        modifier = Modifier,
        userName = "Herbert",
        globalReminders = false,
        reminderIntervals = DefaultPreferences.REMINDER_INTERVALS,
        hasNotificationPermission = true
    )
}