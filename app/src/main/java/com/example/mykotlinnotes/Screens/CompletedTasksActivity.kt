package com.example.mykotlinnotes.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinnotes.Adapter.NoteAdapter
import com.example.mykotlinnotes.Data.MainViewModel
import com.example.mykotlinnotes.Data.Note
import com.example.mykotlinnotes.R
import kotlinx.android.synthetic.main.activity_completed_tasks.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.main_layout

class CompletedTasksActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_tasks)

        adapter = NoteAdapter(this)

        viewModel.notes.observe(this, Observer {
            val notesList = it.filter { it.isSuccess }
            adapter.notes = notesList
        } )

        recycler_completed_notes.layoutManager = LinearLayoutManager(this)
        recycler_completed_notes.adapter = adapter

        //TouchHelpers for delete note
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeNote(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_completed_notes)
        //
    }

    fun onDrawerClick(view: View){
        MainActivity.openDrawer(main_layout)
    }
    fun onNotesClick(view: View){
        MainActivity.redirectActivity(this, MainActivity::class.java)
    }
    fun onCompletedTaskClick(view: View){
        recreate()
    }
    fun onLogoutClick(view: View){
        MainActivity.logout(this)
    }

    fun removeNote(position: Int) {
        val note: Note = adapter.notes[position]
        viewModel.deleteNote(note)
    }
}