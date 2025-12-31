package com.example.indexcards.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val boxId = intent.getLongExtra("boxId", -1)
        val level = intent.getIntExtra("level", -1)
        val boxName = intent.getStringExtra("boxName") ?: ""
        val service = NotificationService(context)

        if (intent.getIntExtra("id", -1) == NotificationRequest.MAKE_REMINDER_LATER) {
            service.closeNotification(
                boxId = boxId,
                level = level,
                intentId = NotificationRequest.MAKE_REMINDER,
            )
            service.scheduleNotificationOnce(
                boxId = boxId,
                level = level,
                boxName = boxName,
            )
        } else {
            if ((boxId != (-1).toLong()) && (level != -1)) {
                service.showNotification(
                    boxId = boxId,
                    level = level,
                    boxName = boxName
                )
            }
        }
    }
}