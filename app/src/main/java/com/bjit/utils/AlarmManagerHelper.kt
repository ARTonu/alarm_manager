package com.bjit.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bjit.data.Alarm
import com.bjit.receiver.AlarmReceiver

class AlarmManagerHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarm.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    fun updateAlarm(alarm: Alarm, newTimeInMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        alarm.timeInMillis = newTimeInMillis
        setAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Alarm is deleted", Toast.LENGTH_SHORT).show()
    }
}