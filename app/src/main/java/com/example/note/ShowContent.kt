package com.example.note

import android.icu.text.Transliterator.Position
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_content.*
import java.text.SimpleDateFormat

class ShowContent : AppCompatActivity() {

    lateinit var mRef: DatabaseReference
    var data: ArrayList<Note>? = null
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_content)

        var bundle = intent.extras
        var title = bundle?.getString("title_key")
        var note = bundle?.getString("note_key")
        position = bundle?.getInt("position_key")!!

        data = ArrayList()

        connectDatabase()

        show_tiltle_id.setText(title)
        show_note_id.setText(note)

    }

    override fun onStart() {
        super.onStart()

        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data?.clear()
                for (d in snapshot!!.children) {
                    var note = d.getValue(Note::class.java)
                    data?.add(note!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        btn_update_id.setOnClickListener()
        {
            val myNote = data?.get(position)!!

            val childRef = mRef?.child(myNote.id!!)

            val title = show_tiltle_id.text.toString()
            val note = show_note_id.text.toString()

            val afterUpdate = Note(myNote.id!!, title, note, getCurrentDate())
            childRef?.setValue(afterUpdate)
        }

    }


    private fun connectDatabase() {
        var database = FirebaseDatabase.getInstance()
        mRef = database.getReference("Notes")

    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("EEEE hh:mm a")

        return format.format(calendar.time)

    }
}