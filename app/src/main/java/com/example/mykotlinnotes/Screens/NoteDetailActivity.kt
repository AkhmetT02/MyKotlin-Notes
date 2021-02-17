package com.example.mykotlinnotes.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinnotes.Data.MainViewModel
import com.example.mykotlinnotes.Data.Note
import com.example.mykotlinnotes.R
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val intent = intent
        if (intent.hasExtra("noteTitle")){
            CoroutineScope(Dispatchers.Default).launch {
                note = viewModel.getNoteByTitle(intent.getStringExtra("noteTitle")!!)

                detail_title.text = note.title
                detail_description.text = note.description
                detail_time.text = note.time
                detail_day_of_week.text = note.dayOfWeek
                when (note.importance) {
                    1 -> detail_importance.text = "High"
                    2 -> detail_importance.text = "Medium"
                    3 -> detail_importance.text = "Low"
                }
            }
        }
        if (note.isSuccess){
            setSuccess()
        }
    }

    fun onPerformedClick(view: View){
        note.isSuccess = true
        viewModel.updateIsSuccess(note.title, note.isSuccess)
        setSuccess()
    }
    private fun setSuccess(){
        performed_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
        performed_btn.isClickable = false
        performed_btn.text = "Performed"
    }
}