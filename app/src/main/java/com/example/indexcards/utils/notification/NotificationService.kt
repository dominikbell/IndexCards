package com.example.indexcards.utils.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.indexcards.MainActivity
import com.example.indexcards.R

val NOTIFICATION_REQUEST_CODES = 1..4

object NotificationRequest {
    const val MAKE_REMINDER: Int = 1
    const val GO_TO_APP: Int = 2
    const val GO_TO_BOX: Int = 3
    const val GO_TO_TRAINING: Int = 4
}

class NotificationService(
    private val context: Context,
) {
    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun closeNotification(boxId: Long = -1, level: Int = -1, intentId: Int) {
        val cancelIntentId = getIntentId(boxId, level, intentId)
        manager.cancel(cancelIntentId)
    }

    fun showNotification(boxId: Long = -1, level: Int = -1, boxName: String = "") {
        val intentId = getIntentId(boxId, level, 0)

        /* TODO: very ugly code repetition */
        val toAppIntent = Intent(context, MainActivity::class.java)
            .putExtra("id", NotificationRequest.GO_TO_APP)
            .putExtra("boxId", boxId)
            .putExtra("level", level)
        val toBoxIntent = Intent(context, MainActivity::class.java)
            .putExtra("id", NotificationRequest.GO_TO_BOX)
            .putExtra("boxId", boxId)
            .putExtra("level", level)
        val toTrainingIntent = Intent(context, MainActivity::class.java)
            .putExtra("id", NotificationRequest.GO_TO_TRAINING)
            .putExtra("boxId", boxId)
            .putExtra("level", level)

        val toAppPendingIntent = PendingIntent.getActivity(
            context,
            getIntentId(boxId, level, NotificationRequest.GO_TO_APP),
            toAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val toBoxPendingIntent = PendingIntent.getActivity(
            context,
            getIntentId(boxId, level, NotificationRequest.GO_TO_BOX),
            toBoxIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val toTrainingPendingIntent = PendingIntent.getActivity(
            context,
            getIntentId(boxId, level, NotificationRequest.GO_TO_TRAINING),
            toTrainingIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.app_icon_notification)
            .setContentTitle("Training waits!")
            .setContentText("Cards of level ${level + 1} in box $boxName need to be trained")
            .setSilent(true)
            .setContentIntent(toAppPendingIntent)
            .addAction(
                R.drawable.app_icon_notification,
                "Train now",
                toTrainingPendingIntent
            )
            .addAction(
                R.drawable.app_icon_notification,
                "Go to box",
                toBoxPendingIntent
            )
            .setAutoCancel(true)
            .build()

        manager.notify(intentId, notification)
    }

    fun scheduleNotification(
        boxId: Long = -1,
        level: Int = -1,
        boxName: String = "",
        triggerTime: Long,
        repeatingTime: Long,
    ) {

        if (boxId != (-1).toLong() && level != -1) {
            val intent = Intent(context, NotificationReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                getIntentId(boxId, level, NotificationRequest.MAKE_REMINDER),
                intent
                    .putExtra("id", NotificationRequest.MAKE_REMINDER)
                    .putExtra("boxId", boxId)
                    .putExtra("level", level)
                    .putExtra("boxName", boxName),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                repeatingTime,
                pendingIntent
            )
        }
    }

    companion object {
        const val CHANNEL_ID = "train_box_channel"
    }
}

fun getIntentId(boxId: Long, level: Int, intentId: Int) =
    100 * boxId.toInt() + 10 * level + intentId
