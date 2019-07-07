package com.taransit.datetimepicker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_dialog.setOnClickListener {
            Toast.makeText(this, "This should show a dialog in the future", Toast.LENGTH_SHORT).show()
        }
    }
}
