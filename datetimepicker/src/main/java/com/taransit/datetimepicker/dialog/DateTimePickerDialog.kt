package com.taransit.datetimepicker.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.taransit.datetimepicker.*
import com.taransit.datetimepicker.helper.CalendarHelper
import kotlinx.android.synthetic.main.dialog_date_time_picker.*
import kotlinx.android.synthetic.main.dialog_date_time_picker.view.*
import java.util.*
import kotlin.math.abs

class DateTimePickerDialog(
    private val cornerRadius: Float = 0f,
    private val initialDate: Calendar = Calendar.getInstance()
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    class Builder {
        companion object {
            const val RADIUS = "radius"
            const val DATE = "date"
        }

        private var builderArguments = Bundle()

        fun setRoundedCorners(radius: Float): Builder {
            builderArguments.putFloat(RADIUS, radius)
            return this
        }

        fun setInitialTimeAndDate(date: Calendar = Calendar.getInstance()): Builder {
            builderArguments.putSerializable(DATE, date)
            return this
        }

        fun build(): DateTimePickerDialog {
            DateTimePickerDialog().apply {
                arguments = builderArguments
                return this
            }
        }
    }

    private var listener: DateTimePickerDialogListener? = null
    private var dateAdapter: DateAdapter? = null

    fun setListener(listener: DateTimePickerDialogListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_date_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cornerRadius = arguments?.getFloat(Builder.RADIUS) ?: this.cornerRadius
        val initialDate = arguments?.getSerializable(Builder.DATE) ?: this.initialDate

        (view.layoutParent.background as GradientDrawable).cornerRadius = cornerRadius
        val viewPagerAdapter = ViewPagerAdapter(requireContext())
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.TABS[position]
        }.attach()

        timePicker.setIs24HourView(true)

        dateAdapter = DateAdapter(object : DateAdapter.OnDateClickListener {
            override fun onDateClicked(date: Calendar) {
                val context = context ?: return
                RoundedDatePickerDialog(context,
                        this@DateTimePickerDialog,
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH),
                        cornerRadius
                ).show()
            }
        })
        dates.adapter = dateAdapter

        dates.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                addItemIfShowingBounds(position)
            }

            override fun onPageSelected(position: Int) {
                addItemIfShowingBounds(position)
                if (position == 0) {
                    dates.post { dates.setCurrentItem(1, false) }
                }
            }

            fun addItemIfShowingBounds(position: Int) {
                val adapter = dateAdapter ?: return
                if (position == 0 || position == adapter.dates.size - 1) {
                    adapter.addItem(position)
                }
            }
        })

        dates.setPageTransformer { page, position ->
            val absPos = abs(position)
            page.apply {
                val scale = if (absPos > 1) 0F else 1 - absPos
                scaleX = scale
                scaleY = scale
            }
        }

        back.setOnClickListener { dates.currentItem = dates.currentItem - 1 }
        forward.setOnClickListener { dates.currentItem = dates.currentItem + 1 }

        showToday.setOnClickListener { showTime() }
        showTime(initialDate as Calendar)

        cancel.setOnClickListener { dismissAllowingStateLoss() }
        confirm.setOnClickListener {
            val calendar = dateAdapter?.getItem(dates.currentItem)
            if (calendar == null) {
                dismissAllowingStateLoss()
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
                @Suppress("DEPRECATION")
                calendar.set(Calendar.MINUTE, timePicker.currentMinute)
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
            }

            listener?.onDateTimeSet(calendar, Direction.from(viewPager.currentItem))
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val background = dialog.window?.decorView?.background as? InsetDrawable
        background?.alpha = 0
        dialog.window?.setBackgroundDrawable(background)
        return dialog
    }

    private fun showTime(date: Calendar = Calendar.getInstance()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            timePicker.currentHour = date.get(Calendar.HOUR_OF_DAY)
            @Suppress("DEPRECATION")
            timePicker.currentMinute = date.get(Calendar.MINUTE)
        } else {
            timePicker.hour = date.get(Calendar.HOUR_OF_DAY)
            timePicker.minute = date.get(Calendar.MINUTE)
        }

        setDate(date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        CalendarHelper.createDayStartCalendar(year, month, dayOfMonth)
                .let(::setDate)
    }

    private fun setDate(day: Calendar = Calendar.getInstance()) {
        val adapter = dateAdapter ?: return

        val position = adapter.getPositionOf(day)
        if (position < 0) {
            val newDatePosition = adapter.invalidateAroundDay(day)
            dates.setCurrentItem(newDatePosition, false)
        } else {
            dates.setCurrentItem(position, false)
        }
    }

    interface DateTimePickerDialogListener {
        fun onDateTimeSet(calendar: Calendar, direction: Direction)
    }
}