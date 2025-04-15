package com.example.indexcards.utils.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.indexcards.MainActivity
import com.example.indexcards.R
import java.time.ZonedDateTime

val NOTIFICATION_REQUEST_CODES = 1..4

object NotificationRequest {
    const val MAKE_REMINDER: Int = 1
    const val GO_TO_BOX: Int = 2
    const val GO_TO_TRAINING: Int = 3
    const val REMIND_LATER: Int = 4
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
        val intentId = getIntentId(boxId, level, NotificationRequest.MAKE_REMINDER)

        val remindLaterIntent =
            Intent(context, NotificationReceiver::class.java)
                .putExtra("id", NotificationRequest.REMIND_LATER)
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

        val remindLaterPendingIntent = PendingIntent.getBroadcast(
            context,
            getIntentId(boxId, level, NotificationRequest.REMIND_LATER),
            remindLaterIntent,
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
            .setContentTitle(context.getString(R.string.training_waits))
            .setContentText(
                context.getString(R.string.cards_need_train1)
                        + " ${level + 1} "
                        + context.getString(R.string.cards_need_train2)
                        + " $boxName " + context.getString(R.string.cards_need_train3)
            )
            .setSilent(true)
            .setContentIntent(toTrainingPendingIntent)
            .addAction(
                R.drawable.app_icon_notification,
                context.getString(R.string.train_now),
                toTrainingPendingIntent,
            )
            .addAction(
                R.drawable.app_icon_notification,
                context.getString(R.string.go_to_box),
                toBoxPendingIntent,
            )
            .addAction(
                R.drawable.app_icon_notification,
                context.getString(R.string.remind_me_later),
                remindLaterPendingIntent,
            )
            .setAutoCancel(true)
            .build()

        manager.notify(intentId, notification)
    }

    fun scheduleNotificationOnce(
        boxId: Long = -1,
        level: Int = -1,
        boxName: String = "",
        triggerTime: Long = ZonedDateTime.now().plusHours(1).toInstant().toEpochMilli(),
    ) {
        val intent = Intent(context, NotificationReceiver::class.java)
            .putExtra("id", NotificationRequest.MAKE_REMINDER)
            .putExtra("boxId", boxId)
            .putExtra("level", level)
            .putExtra("boxName", boxName)
        val requestId = getIntentId(boxId, level, NotificationRequest.MAKE_REMINDER)

        if (boxId != (-1).toLong() && level != -1) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent,
            )
        }
    }

    fun scheduleNotificationRepeating(
        boxId: Long = -1,
        level: Int = -1,
        boxName: String = "",
        triggerTime: Long,
        repeatingTime: Long,
    ) {
        val intent = Intent(context, NotificationReceiver::class.java)
            .putExtra("id", NotificationRequest.MAKE_REMINDER)
            .putExtra("boxId", boxId)
            .putExtra("level", level)
            .putExtra("boxName", boxName)
        val requestId = getIntentId(boxId, level, NotificationRequest.MAKE_REMINDER)

        val alarmExists =
            PendingIntent.getBroadcast(
                context,
                requestId,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            ) != null

        if (!alarmExists) {
            if (boxId != (-1).toLong() && level != -1) {

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestId,
                    intent,
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
    }

    companion object {
        const val CHANNEL_ID = "train_box_channel"
    }
}

fun getIntentId(boxId: Long, level: Int, intentId: Int) =
    100 * boxId.toInt() + 10 * level + intentId
