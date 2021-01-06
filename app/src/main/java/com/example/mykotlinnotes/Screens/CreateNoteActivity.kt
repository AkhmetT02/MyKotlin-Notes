package com.example.mykotlinnotes.Screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinnotes.Data.MainViewModel
import com.example.mykotlinnotes.Data.Note
import com.example.mykotlinnotes.Notification.NotificationReceiver
import com.example.mykotlinnotes.R
import kotlinx.android.synthetic.main.activity_create_note.*
import java.util.*

class CreateNoteActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var calendarNow: Calendar = Calendar.getInstance()

    private var hour: Int = 0
    private var minute: Int = 0
    private var dateFormat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        setTime()
        importance_group.check(0)
    }

    private fun setTime(){
        calendarNow.set(Calendar.SECOND, 0)
        calendarNow.set(Calendar.MILLISECOND, 0)
        //set hour
        numPickerHour.minValue = 0
        numPickerHour.maxValue = 11
        numPickerHour.setOnValueChangedListener { picker, oldVal, newVal ->
            hour = picker.value
            calendarNow.set(Calendar.HOUR_OF_DAY, hour)
        }

        //set minute
        numPickerMinute.minValue = 0
        numPickerMinute.maxValue = 59
        numPickerMinute.setOnValueChangedListener { picker, oldVal, newVal ->
            minute = picker.value
            calendarNow.set(Calendar.MINUTE, minute)
        }

        //set am or pm
        val df = arrayOf("AM", "PM")
        numPickerDF.minValue = 0
        numPickerDF.maxValue = df.size - 1
        numPickerDF.displayedValues = df
        numPickerDF.setOnValueChangedListener { picker, oldVal, newVal ->
            dateFormat = df[picker.value]
            if (dateFormat == "AM") {
                calendarNow.set(Calendar.AM_PM, Calendar.AM)
            } else {
                calendarNow.set(Calendar.AM_PM, Calendar.PM)
            }
        }
    }


    fun onCreateNoteClick(view: View){
        val title = title_et.text.toString()
        val description = description_et.text.toString()
        val day = days_of_week_spinner.selectedItem.toString()
        val radioId = importance_group.checkedRadioButtonId
        val radioBtn: RadioButton = findViewById(radioId)
        val importance: Int = radioBtn.text.toString().toInt()

        when(day){
            "Monday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            "Tuesday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
            "Wednesday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
            "Thursday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
            "Friday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
            "Saturday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            "Sunday" -> calendarNow.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }

        //if this note not in database
        if (!viewModel.existsNote(title)) {
            if (isFilled(title, description)) {
                //if all fields are filled create new note
                val time = calendarNow.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendarNow.get(Calendar.MINUTE).toString() + dateFormat
                val note = Note(title, description, day, importance, time, false)
                viewModel.insertNote(note)

                //if the specified time is longer than the current
                val calendar = Calendar.getInstance()
                if (calendarNow >= calendar) {
                    setAlarm(calendarNow.timeInMillis)
                }
                //start intent
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please, fill all lines...", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "A note with the same name already exists.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFilled(title: String, description: String) = title.isNotEmpty() && description.isNotEmpty()

    //deliver a notification at the specified time with broadcast receiver
    private fun setAlarm(timeInMillis: Long){
        val intent = Intent(baseContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(baseContext, 0, intent, 0)
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

}