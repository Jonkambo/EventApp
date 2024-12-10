package com.example.eventapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class EventAdapter(private val context: Context, private val events: List<EventLocation>, private val itemClickListener: OnItemClickListener) : BaseAdapter() {

    override fun getCount(): Int {
        return events.size
    }

    override fun getItem(position: Int): Any {
        return events[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        fun onItemClick(eventLocation: EventLocation)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.event_card, parent, false)

        val eventTitle = view.findViewById<TextView>(R.id.eventTitle)
        val eventDate = view.findViewById<TextView>(R.id.eventDate)

        val event = events[position]
        eventTitle.text = event.eventTitle
        eventDate.text = event.eventDate

        view.setOnClickListener {
            itemClickListener.onItemClick(event)
        }

        return view
    }
}