package com.example.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.note.view.*

class NoteAdapter(var context: Context, var data: ArrayList<Note>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = LayoutInflater.from(context).inflate(R.layout.note, parent, false)

        var note = getItem(position) as Note
        view.tv_title_id.text = note?.title.toString()
        view.tv_date_id.text = note?.timestamp.toString()
        view.tv_note_id.text = note?.note.toString()

        return view
    }
}