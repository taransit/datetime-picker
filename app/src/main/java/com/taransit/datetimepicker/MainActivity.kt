package com.taransit.datetimepicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.taransit.datetimepicker.dialog.DateTimePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DateTimePickerDialog.DateTimePickerDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_dialog.setOnClickListener {
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
                customTheme = R.style.CustomTimePickerTheme
            ).apply {
                setListener(this@MainActivity)
                show(ft, DateTimePickerDialog::class.java.simpleName)
            }
        }

        show_dialog.performClick()
    }

    override fun onDateTimeSet(calendar: Calendar, page: String?) {
        Log.d("Test", calendar.toString())
        Log.d("Test", "$page")
    }
}
