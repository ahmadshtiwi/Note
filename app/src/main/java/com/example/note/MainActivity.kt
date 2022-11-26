package com.example.note

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_dilog.view.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    lateinit var mRef: DatabaseReference
    var data: ArrayList<Note>? = null
    lateinit var myNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.header_id)
        setActionBar(toolbar)
        title = ""


        connectDatabase()
        data = ArrayList()

        btn_show_dialog_id.setOnClickListener()
        {
            showDialog()
        }

        grid_view_note_id.onItemClickListener = OnItemClickListener { parent, view, position, id ->

            myNote = data?.get(position)!!

            val gotToShowContent = Intent(this, ShowContent::class.java)

            gotToShowContent.putExtra("position_key", position)
            gotToShowContent.putExtra("title_key", myNote.title)
            gotToShowContent.putExtra("note_key", myNote.note)
            startActivity(gotToShowContent)
        }

        registerForContextMenu(grid_view_note_id)
        grid_view_note_id.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->

                myNote = data?.get(position)!!
                false
            }

    }
// inflate menu (create)
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        val menuInflater = menuInflater.inflate(R.menu.list, menu)
    }

// on select delete in context menu will remove data in fire base
    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.delete_item_id) {
            mRef.child(myNote.id!!).removeValue()
        }
        return true
    }


    override fun onStart() {                      // start activity
        super.onStart()
// when change any date in fir base will change here
        mRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data?.clear()
                for (d in snapshot.children) {
                    val note = d.getValue(Note::class.java)
                    data?.add(note!!)
                }
                val noteAdapter = NoteAdapter(applicationContext, data!!)
                grid_view_note_id.adapter = noteAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun connectDatabase() {
        val database = FirebaseDatabase.getInstance()
        mRef = database.getReference("Notes")

    }

/*
build custom alert dialog
using button in alert dialog
test if any field is empty will show message
get key ( id ) from database
set value in class note ( id  , title , note , time -> via method )
store data in object
create child and set key=id  and set object inside child
 */
    private fun showDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.note_dilog, null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()
        view.btn_add_note_id.setOnClickListener()
        {

            val title = view.et_title_id.text.toString()
            val note = view.et_note_id.text.toString()

            if (title.isEmpty())
                Toast.makeText(this, "Please Set Title", Toast.LENGTH_SHORT).show()
            else if (note.isEmpty())
                Toast.makeText(this, "Please Set Note", Toast.LENGTH_SHORT).show()
            else {
                val id = mRef.push().key.toString()
                val objNote = Note(id, title, note, getCurrentDate())
                mRef.child(id).setValue(objNote)
                alertDialog.dismiss()
            }
        }
    }

    // get Data (day , hours , minute , and (am,pm)

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("EEEE hh:mm a")

        return format.format(calendar.time)

    }


}