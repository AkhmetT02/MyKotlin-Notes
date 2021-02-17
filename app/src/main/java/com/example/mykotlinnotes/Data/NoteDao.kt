package com.example.mykotlinnotes.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT EXISTS (SELECT 1 FROM note WHERE title = :title)")
    fun exists(title: String) : Boolean

    @Query("SELECT * FROM note WHERE title = :title")
    fun getNoteByTitle(title: String) : Note

    @Query("UPDATE note SET isSuccess = :isSuccess WHERE title = :title")
    fun updateIsSuccess(title: String, isSuccess: Boolean)
}