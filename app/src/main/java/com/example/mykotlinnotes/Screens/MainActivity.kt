package com.example.mykotlinnotes.Screens

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
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
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.main_navigation_drawer.*
import kotlinx.android.synthetic.main.main_navigation_drawer.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        var notesAsList: ArrayList<Note>

        recycler_notes.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(this)
        recycler_notes.adapter = adapter

        val notes = viewModel.notes
        notes.observe(this, Observer {
            notesAsList = it as ArrayList<Note>
            adapter.notes = notesAsList.filter { !it.isSuccess }
            if (it.isEmpty()){
                not_tasks_tv.visibility = View.VISIBLE
            } else{
                not_tasks_tv.visibility = View.INVISIBLE
            }
        })

        //remove note when swiped
        val itemTouchHelperCallBack = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeNote(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(recycler_notes)

    }

    fun onAddNoteClick(view: View){
        val intent = Intent(this, CreateNoteActivity::class.java)
        startActivity(intent)
    }

    //remove note
    fun removeNote(position: Int) {
        val note: Note = adapter.notes[position]
        viewModel.deleteNote(note)
    }


    //create notification channel
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("taskNotify", "NotesNotification", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notes notification"

            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    fun onDrawerClick(view: View){
        openDrawer(main_layout)
    }
    fun onNotesClick(view: View){
        recreate()
    }
    fun onCompletedTaskClick(view: View){
        redirectActivity(this, CompletedTasksActivity::class.java)
    }
    fun onLogoutClick(view: View){
        logout(this)
    }

    override fun onPause() {
        super.onPause()
        closeDrawer(main_layout)
    }

    /*
     * Static functions for drawer
     */
    companion object {
        fun openDrawer(drawer: DrawerLayout) {
            drawer.openDrawer(GravityCompat.START)
        }

        fun closeDrawer(drawer: DrawerLayout) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            }
        }

        fun redirectActivity(activity: Activity, secondActivity: Class<*>) {
            val intent = Intent(activity, secondActivity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.finish()
        }

        fun logout(activity: Activity) {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle("Logout")
            alert.setMessage("Close the application?")

            alert.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                activity.finishAffinity()
                exitProcess(0)
            })
            alert.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            alert.show()
        }
    }
}