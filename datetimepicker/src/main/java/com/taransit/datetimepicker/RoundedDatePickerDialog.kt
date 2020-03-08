package com.taransit.datetimepicker

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle

class RoundedDatePickerDialog(
    context: Context,
    listener: OnDateSetListener,
    year: Int,
    month: Int,
    day: Int,
    private val cornerRadius: Float = 0f
) : DatePickerDialog(
    context,
    listener,
    year,
    month,
    day
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val backgroundDrawable = context.getDrawable(R.drawable.layout_background)
        (backgroundDrawable as GradientDrawable).cornerRadius = cornerRadius
        window?.setBackgroundDrawable(backgroundDrawable)
        super.onCreate(savedInstanceState)
    }
}