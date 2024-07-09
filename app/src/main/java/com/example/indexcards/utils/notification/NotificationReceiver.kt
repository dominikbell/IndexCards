package com.example.indexcards.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.Q)
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