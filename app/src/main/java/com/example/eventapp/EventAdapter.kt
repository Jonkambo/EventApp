package com.example.eventapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.eventapp.Data.EventLocation

class EventAdapter(private val context: Context, private val events: List<EventLocation>) : BaseAdapter() {

    override fun getCount(): Int {
        return events.size
    }

    override fun getItem(position: Int): Any {
        return events[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.event_card, parent, false)

        val eventTitle = view.findViewById<TextView>(R.id.eventTitle)
        val eventDate = view.findViewById<TextView>(R.id.eventDate)

        val event = events[position]
        eventTitle.text = event.eventTitle
        eventDate.text = event.eventDate

        return view
    }
}