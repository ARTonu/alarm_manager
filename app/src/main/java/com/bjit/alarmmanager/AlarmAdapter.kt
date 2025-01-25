package com.bjit.alarmmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bjit.data.Alarm
import java.util.Date

class AlarmAdapter(
    private var alarms: List<Alarm>,
    private val onEdit: (Alarm) -> Unit,
    private val onDelete: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm, onEdit, onDelete)
    }

    override fun getItemCount(): Int = alarms.size

    fun updateAlarms(newAlarms: List<Alarm>) {
        alarms = newAlarms
        notifyDataSetChanged()
    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(alarm: Alarm, onEdit: (Alarm) -> Unit, onDelete: (Alarm) -> Unit) {
            timeTextView.text = Date(alarm.timeInMillis).toString()
            editButton.setOnClickListener { onEdit(alarm) }
            deleteButton.setOnClickListener { onDelete(alarm) }
        }
    }
}