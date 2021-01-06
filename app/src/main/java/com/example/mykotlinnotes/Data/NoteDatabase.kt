package com.example.mykotlinnotes.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 4, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    companion object{
        private var database: NoteDatabase? = null
        private const val DB_NAME = "note.db"

        fun getInstance(context: Context): NoteDatabase{
            synchronized(this){
                var instance = database
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                    database = instance
                }
                return instance
            }
        }
    }

    abstract fun getDao(): NoteDao
}