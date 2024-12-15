package com.example.eventapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UsersAdapter(
    private val context: Context,
    private val users: List<User>,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter() {

    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_card, parent, false)

        val userName = view.findViewById<TextView>(R.id.userName)
        val userInfo = view.findViewById<TextView>(R.id.userInfo)

        val user = users[position]
        userName.text = user.login
        userInfo.text = user.userInfo ?: "No info"

        view.setOnClickListener {
            itemClickListener.onItemClick(user)
        }

        return view
    }
}