package com.bjit.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bjit.utils.AlarmManagerHelper
import com.bjit.data.Alarm
import com.bjit.receiver.AlarmReceiver
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btnSetAlarm: Button
    lateinit var timePicker: TimePicker
    lateinit var alarmRecyclerView: RecyclerView
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmManagerHelper: AlarmManagerHelper
    private val alarmAdapter = AlarmAdapter(mutableListOf(), ::onEditAlarm, ::onDeleteAlarm)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Alarm App"
        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.buttonAlarm)
        alarmRecyclerView = findViewById(R.id.alarmRecyclerView)

        alarmRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmRecyclerView.adapter = alarmAdapter
        alarmManagerHelper = AlarmManagerHelper(this)

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
            val alarm = Alarm(alarmAdapter.itemCount, calendar.timeInMillis)
            alarmViewModel.addAlarm(alarm)
            setAlarm(calendar.timeInMillis)
        }
        alarmViewModel.alarms.observe(this) { alarms ->
            alarmAdapter.updateAlarms(alarms)
        }
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    // Add onEditAlarm and onDeleteAlarm methods
    private fun onEditAlarm(alarm: Alarm) {
        showTimePickerDialog(alarm)
    }

    private fun onDeleteAlarm(alarm: Alarm) {
        alarmManagerHelper.deleteAlarm(alarm)
        alarmViewModel.deleteAlarm(alarm)
    }

    private fun showTimePickerDialog(alarm: Alarm) {
        val calendar = Calendar.getInstance().apply { timeInMillis = alarm.timeInMillis }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            alarmManagerHelper.updateAlarm(alarm, calendar.timeInMillis)
            alarmViewModel.updateAlarm(alarm, calendar.timeInMillis)
        }, hour, minute, true).show()
    }
}