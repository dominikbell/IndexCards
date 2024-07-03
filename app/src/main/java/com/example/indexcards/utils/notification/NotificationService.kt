package com.example.indexcards.utils.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.indexcards.MainActivity
import com.example.indexcards.R
import java.time.LocalDateTime
import java.time.ZoneOffset

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

    fun closeNotification(boxId: Long = -1, level: Int = -1) {
        val intentId = getIntentId(boxId, level)
        manager.cancel(intentId)
    }

    fun showNotification(boxId: Long = -1, level: Int = -1) {
        val intentId = getIntentId(boxId, level)
        val activityIntent = Intent(context, MainActivity::class.java)

        val goToAppIntent = PendingIntent.getActivity(
            context,
            intentId,
            activityIntent
                .putExtra("id", NotificationRequest.GO_TO_APP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val goToBoxIntent = PendingIntent.getActivity(
            context,
            intentId,
            activityIntent
                .putExtra("id", NotificationRequest.GO_TO_BOX)
                .putExtra("boxId", boxId),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val goToTrainingIntent = PendingIntent.getActivity(
            context,
            intentId,
            activityIntent
                .putExtra("id", NotificationRequest.GO_TO_TRAINING)
                .putExtra("boxId", boxId)
                .putExtra("level", level),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("Training waits!")
            .setContentText("Cards of level ${level + 1} of box $boxId need to be trained")
            .setSilent(true)
            .setContentIntent(goToAppIntent)
            .addAction(
                R.drawable.app_icon,
                "Train now",
                goToTrainingIntent
            )
            .addAction(
                R.drawable.app_icon,
                "Go to box",
                goToBoxIntent
            )
            .setAutoCancel(true)
            .build()

        manager.notify(intentId, notification)
    }

    fun scheduleNotification(boxId: Long = -1, level: Int = -1) {

        val intentId = getIntentId(boxId, level)
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            intentId,
            intent
                .putExtra("id", NotificationRequest.MAKE_REMINDER)
                .putExtra("boxId", boxId)
                .putExtra("level", level),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = getTime()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }


    private fun getTime(): Long {
        return (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 1000)
    }

    companion object {
        const val CHANNEL_ID = "train_box_channel"
    }
}

fun getIntentId(boxId: Long, level: Int) = 10 * boxId.toInt() + level
