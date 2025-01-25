package com.bjit.alarmmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bjit.data.Alarm

class AlarmViewModel : ViewModel() {

    private val _alarms = MutableLiveData<MutableList<Alarm>>(mutableListOf())
    val alarms: LiveData<MutableList<Alarm>> get() = _alarms

    fun addAlarm(alarm: Alarm) {
        _alarms.value?.add(alarm)
        _alarms.value = _alarms.value
    }

    fun updateAlarm(alarm: Alarm, newTimeInMillis: Long) {
        alarm.timeInMillis = newTimeInMillis
        _alarms.value = _alarms.value
    }

    fun deleteAlarm(alarm: Alarm) {
        _alarms.value?.remove(alarm)
        _alarms.value = _alarms.value
    }
}