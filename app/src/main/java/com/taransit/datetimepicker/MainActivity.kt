package com.taransit.datetimepicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.taransit.datetimepicker.dialog.DateTimePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DateTimePickerDialog.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDialog.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag(DateTimePickerDialog::class.java.simpleName)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            DateTimePickerDialog(
                cornerRadius = 20f,
                initialDate = Calendar.getInstance().apply { set(2015, 9, 28, 4, 20) },
                pages = listOf("Departure", "Arrival"),
                initialPagePosition = 1,
                customTheme = R.style.CustomTimePickerTheme
            ).apply {
                setListener(this@MainActivity)
                show(ft, DateTimePickerDialog::class.java.simpleName)
            }
        }

        showDialog.performClick()
    }

    override fun onDateTimeSet(calendar: Calendar, page: Int) {
        Log.d("Test", calendar.toString())
        Log.d("Test", "$page")
    }
}
