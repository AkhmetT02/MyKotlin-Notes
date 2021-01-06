package com.example.mykotlinnotes.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinnotes.Data.Note
import com.example.mykotlinnotes.R
import com.example.mykotlinnotes.Screens.NoteDetailActivity
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var notes: List<Note> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = notes[position]
        holder.title.text = note.title
        holder.description.text = note.description
        holder.day.text = note.dayOfWeek
        holder.time.text = note.time
        when(note.importance){
            1 -> holder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            2 -> holder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
            3 -> holder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteDetailActivity::class.java)
            intent.putExtra("noteTitle", note.title)
            context.startActivity(intent)
        }
    }


    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.note_title_item
        val description: TextView = view.note_description_item
        val day: TextView = view.note_day_item
        val time: TextView = view.note_time_item
    }
}