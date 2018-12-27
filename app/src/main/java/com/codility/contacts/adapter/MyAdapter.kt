package com.codility.contacts.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.codility.contacts.R
import com.codility.contacts.listener.OnCallListener
import com.codility.contacts.model.Contact

/**
 * Created by Govind on 05/21/2018.
 */
class MyAdapter(private val contactList: ArrayList<Contact>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var onCallListener: OnCallListener<Contact>? = null

    fun setListener(onCallListener: OnCallListener<Contact>) {
        this.onCallListener = onCallListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bindItems(contact)
        holder.itemView.findViewById<ImageButton>(R.id.ibCall).setOnClickListener {
            if (onCallListener != null) {
                onCallListener!!.onCall(contact)
            }
        }
        holder.itemView.findViewById<ImageButton>(R.id.ibMessage).setOnClickListener {
            if (onCallListener != null) {
                onCallListener!!.onMessage(contact)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(contact: Contact) {
            val tvName = itemView.findViewById<TextView>(R.id.tvName)
            val tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)
            tvName.text = contact.name
            tvNumber.text = contact.number
        }
    }
}