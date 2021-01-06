package com.example.mykotlinnotes.Data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var description: String,
    var dayOfWeek: String,
    var importance: Int,
    var time: String,
    var isSuccess: Boolean
){

    @Ignore constructor(title: String, description: String, dayOfWeek: String, importance: Int, time: String, isSuccess: Boolean): this(0, title, description, dayOfWeek, importance, time, isSuccess){
        this.title = title
        this.description = description
        this.dayOfWeek = dayOfWeek
        this.importance = importance
        this.time = time
        this.isSuccess = isSuccess
    }
}