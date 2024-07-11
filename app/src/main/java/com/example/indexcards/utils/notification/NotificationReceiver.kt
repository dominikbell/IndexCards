package com.example.indexcards.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val boxId = intent.getLongExtra("boxId", -1)
        val level = intent.getIntExtra("level", -1)

        if ((boxId != (-1).toLong()) && (level != -1)) {
            val service = NotificationService(context)

            service.showNotification(
                boxId = boxId,
                level = level,
            )
        }
    }
}