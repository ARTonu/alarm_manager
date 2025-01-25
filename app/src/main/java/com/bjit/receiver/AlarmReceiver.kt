package com.bjit.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.bjit.alarmmanager.R

class AlarmReceiver : BroadcastReceiver() {
    var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Alarm Bell", "Alarm just fired")
        // Show a toast message when the alarm is triggered
        Toast.makeText(context, "Repeating Alarm Triggered!", Toast.LENGTH_LONG).show();

        // Play alarm sound
        playAlarmSound(context);
    }

    private fun playAlarmSound(context: Context) {
        // Load the alarm sound from the res/raw folder
        val alarmSound = Uri.parse("android.resource://${context.packageName}/${R.raw.alarm_tone}")

        // Create and play an alarm tone
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, alarmSound)
        }

        // Check if the MediaPlayer is not already playing
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }

        // Release MediaPlayer resources after playing the sound
        mediaPlayer?.setOnCompletionListener { mp ->
            mp.release()
            mediaPlayer = null
        }
    }
}