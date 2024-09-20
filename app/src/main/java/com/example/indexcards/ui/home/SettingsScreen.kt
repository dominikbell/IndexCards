package com.example.indexcards.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.home.toAtLeast2DigitString
import com.example.indexcards.utils.home.toReminderText
import com.example.indexcards.utils.home.toWord

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    hasNotificationPermission: Boolean,
    userName: String,
    globalReminders: Boolean,
    reminderIntervals: List<Pair<Int, String>>,
    reminderTime: Pair<Int, Int>,
    openUserNameDialog: () -> Unit = {},
    openRemindersDialog: (Int) -> Unit = {},
    openRemindersTimeDialog: () -> Unit = {},
    changeGlobalReminders: () -> Unit = {},
    cancelAllNotifications: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false },
    setAllReminders: () -> Unit = {},
) {
    val context = LocalContext.current
    val remindersSetText = stringResource(id = R.string.global_reminders_set)
    val remindersCancelledText = stringResource(id = R.string.global_reminders_cancelled)

    val reminderTimeText =
        if (reminderTime.first == -1) {
            stringResource(id = R.string.not_set)
        } else {
            reminderTime.first.toAtLeast2DigitString() + ":" + reminderTime.second.toAtLeast2DigitString()
        }

    fun enableGlobalReminders() {
        if (!globalReminders) {
            setAllReminders()
            Toast.makeText(context, remindersSetText, Toast.LENGTH_SHORT).show()
        } else {
            cancelAllNotifications()
            Toast.makeText(context, remindersCancelledText, Toast.LENGTH_SHORT).show()
        }
        changeGlobalReminders()
    }

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
                            enableGlobalReminders()
                        }
                    } else {
                        enableGlobalReminders()
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.default_notification_time))

            Text(
                modifier = Modifier.clickable {
                    openRemindersTimeDialog()
                },
                text = reminderTimeText,
            )
        }

        HorizontalDivider()

        Text(text = stringResource(id = R.string.reminders_for) + " ..")

        reminderIntervals.forEachIndexed { index, interval ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ".. " + stringResource(R.string.level_pronoun) + " " +
                            (index + 1).toWord() + " "
                            + stringResource(id = R.string.level) + ":"
                )

                Text(
                    modifier = Modifier.clickable {
                        openRemindersDialog(index)
                    },
                    text = " " + interval.second.toReminderText(interval.first) + " ",
                    textDecoration = TextDecoration.Underline,
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
        reminderTime = DefaultPreferences.REMINDER_TIME,
        reminderIntervals = DefaultPreferences.REMINDER_INTERVALS,
        hasNotificationPermission = true
    )
}