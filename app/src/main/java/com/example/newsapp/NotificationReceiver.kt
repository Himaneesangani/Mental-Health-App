package com.example.newsapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver()
 {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intervalMillis =  100000  // 3 hours in milliseconds
        val startTimeMillis = System.currentTimeMillis() + intervalMillis
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTimeMillis,
            intervalMillis.toLong(),
            pendingIntent
        )

        // Create notification channel (for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "mood_reminder",
                "Mood Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create notification
        val notificationBuilder = NotificationCompat.Builder(context, "mood_reminder")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("How's your mood?")
            .setContentText("Select one of the following moods:")
            .setAutoCancel(true)
            .addAction(getMoodAction(context, "happy", "😄 Happy"))
            .addAction(getMoodAction(context, "sad", "😔 Sad"))
            .addAction(getMoodAction(context, "neutral", "😐 Neutral"))
            .addAction(getMoodAction(context, "angry", "😠 Angry"))

        notificationManager.notify(1000, notificationBuilder.build())
    }

    private fun getMoodAction(context: Context, mood: String, label: String): NotificationCompat.Action {
        val intent = Intent(context, MoodSelectionService::class.java).apply {
            putExtra("mood", mood)
        }
        val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Action.Builder(0, label, pendingIntent).build()
    }
}