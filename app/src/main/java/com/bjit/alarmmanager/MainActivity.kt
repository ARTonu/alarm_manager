package com.bjit.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bjit.data.Alarm
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btnSetAlarm: Button
    lateinit var timePicker: TimePicker
    lateinit var alarmRecyclerView: RecyclerView
    private val alarms = mutableListOf<Alarm>()
    private val alarmAdapter = AlarmAdapter(alarms, ::onEditAlarm, ::onDeleteAlarm)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Alarm App"
        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.buttonAlarm)
        alarmRecyclerView = findViewById(R.id.alarmRecyclerView)

        alarmRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmRecyclerView.adapter = alarmAdapter

        btnSetAlarm.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                timePicker.hour,
                timePicker.minute,
                0
            )
            val alarm = Alarm(alarms.size, calendar.timeInMillis)
            alarms.add(alarm)
            alarmAdapter.notifyItemInserted(alarms.size - 1)
            setAlarm(calendar.timeInMillis)
        }
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    private class MyAlarm : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Toast.makeText(context, "Alarm is fired", Toast.LENGTH_SHORT).show()
            Log.d("Alarm Bell", "Alarm just fired")
        }
    }

    // Add onEditAlarm and onDeleteAlarm methods
    private fun onEditAlarm(alarm: Alarm) {
        showTimePickerDialog(alarm)
    }

    private fun onDeleteAlarm(alarm: Alarm) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        alarms.remove(alarm)
        alarmAdapter.notifyItemRemoved(alarms.indexOf(alarm))
        Toast.makeText(this, "Alarm is deleted", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePickerDialog(alarm: Alarm) {
        val calendar = Calendar.getInstance().apply { timeInMillis = alarm.timeInMillis }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            updateAlarm(alarm, calendar.timeInMillis)
        }, hour, minute, true).show()
    }

    private fun updateAlarm(alarm: Alarm, newTimeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)

        // Cancel the existing alarm
        alarmManager.cancel(pendingIntent)

        // Update the alarm time
        alarm.timeInMillis = newTimeInMillis

        // Set the new alarm
        alarmManager.setRepeating(
            AlarmManager.RTC,
            newTimeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        // Notify the adapter about the change
        alarmAdapter.notifyItemChanged(alarms.indexOf(alarm))
        Toast.makeText(this, "Alarm is updated", Toast.LENGTH_SHORT).show()
    }
}