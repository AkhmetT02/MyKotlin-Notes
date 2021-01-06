package com.example.mykotlinnotes.Data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.loader.content.AsyncTaskLoader

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database: NoteDatabase = NoteDatabase.getInstance(application.applicationContext)
    var notes: LiveData<List<Note>> = database.getDao().getAllNotes()

    fun insertNote(note: Note){
        InsertNoteTask().execute(note)
    }
    fun deleteNote(note: Note){
        DeleteNoteTask().execute(note)
    }
    fun existsNote(title: String) : Boolean{
        return ExistsNoteTask().execute(title).get()
    }
    fun getNoteByTitle(title: String) : Note{
        return GetNoteByTitleTask().execute(title).get()
    }
    fun updateIsSuccess(title: String, isSuccess: Boolean){
        UpdateIsSuccessTask(title).execute(isSuccess)
    }

    private inner class InsertNoteTask : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            if (params != null && params.isNotEmpty()){
                params[0]?.let { database.getDao().insertNote(it) }
            }
            return null
        }
    }
    private inner class DeleteNoteTask : AsyncTask<Note, Void, Void>(){
        override fun doInBackground(vararg params: Note?): Void? {
            if (params != null && params.isNotEmpty()){
                params[0]?.let { database.getDao().deleteNote(it) }
            }
            return null
        }
    }
    private inner class ExistsNoteTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean? {
            if (params != null && params.isNotEmpty()){
                return params[0]?.let { database.getDao().exists(it) }
            }
            return null
        }
    }
    private inner class GetNoteByTitleTask : AsyncTask<String, Void, Note>(){
        override fun doInBackground(vararg params: String?): Note? {
            if (params != null && params.isNotEmpty()){
                return params[0]?.let { database.getDao().getNoteByTitle(it) }
            }
            return null
        }
    }
    private inner class UpdateIsSuccessTask(val title: String) : AsyncTask<Boolean, Void, Void>(){
        override fun doInBackground(vararg params: Boolean?): Void? {
            if (params != null && params.isNotEmpty()){
                params[0]?.let { database.getDao().updateIsSuccess(title, it) }
            }
            return null
        }
    }
}