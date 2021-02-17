package com.example.mykotlinnotes.Data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.loader.content.AsyncTaskLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database: NoteDatabase = NoteDatabase.getInstance(application.applicationContext)

    suspend fun getNotes() = withContext(Dispatchers.Default){
        database.getDao().getAllNotes()
    }



    fun insertNote(note: Note) = CoroutineScope(Dispatchers.Default).launch {
        database.getDao().insertNote(note = note)
    }
    fun deleteNote(note: Note) = CoroutineScope(Dispatchers.Default).launch {
        database.getDao().deleteNote(note = note)
    }
    suspend fun existsNote(title: String) : Boolean = withContext(Dispatchers.Default){
        database.getDao().exists(title = title)
    }
    suspend fun getNoteByTitle(title: String) : Note = withContext(Dispatchers.Default){
        database.getDao().getNoteByTitle(title = title)
    }
    fun updateIsSuccess(title: String, isSuccess: Boolean) = CoroutineScope(Dispatchers.Default).launch {
        database.getDao().updateIsSuccess(title = title, isSuccess = isSuccess)
    }


}