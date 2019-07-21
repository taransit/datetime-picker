package com.taransit.datetimepicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taransit.datetimepicker.helper.CalendarHelper.createDayStartCalendar
import kotlinx.android.synthetic.main.item_date.view.*
import java.text.DateFormat
import java.util.*

class DateAdapter(val listener: OnDateClickListener) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    companion object {
        const val DAYS_SPREAD = 40
    }
    val dates = mutableListOf<Calendar>()

    init {
        Calendar.getInstance()
            .let(::createDayStartCalendar)
            .let(::invalidateAroundDay)
    }

    /**
     * Invalidates data and sets the date in arguments as the middle one
     * @return Position of the requested date
     */
    fun invalidateAroundDay(centerDay: Calendar): Int {
        val day = centerDay.clone() as Calendar
        dates.clear()
        day.add(Calendar.DAY_OF_YEAR, -DAYS_SPREAD/2)
        for (i in (0..DAYS_SPREAD)) {
            dates.add(day.clone() as Calendar)
            day.add(Calendar.DAY_OF_YEAR, 1)
        }

        notifyDataSetChanged()

        return getPositionOf(centerDay)
    }

    fun getPositionOf(year: Int, month: Int, dayOfMonth: Int): Int {
        return Calendar.getInstance()
            .apply { set(year, month, dayOfMonth) }
            .let(::createDayStartCalendar)
            .let(::getPositionOf)
    }

    fun getPositionOf(day: Calendar = Calendar.getInstance()): Int {
        return dates.indexOfFirst {
            it.get(Calendar.ERA) == day.get(Calendar.ERA) &&
                    it.get(Calendar.YEAR) == day.get(Calendar.YEAR) &&
                    it.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)
        }
    }

    fun addItem(currentPosition: Int) {
        if (currentPosition == 0) {
            val dayBeforeFirst = dates[0].clone() as Calendar
            dayBeforeFirst.add(Calendar.DAY_OF_YEAR, -1)
            dates.add(0, dayBeforeFirst)
            notifyItemInserted(0)
        } else if (currentPosition == dates.size - 1) {
            val dayAfterLast = dates[dates.size - 1].clone() as Calendar
            dayAfterLast.add(Calendar.DAY_OF_YEAR, 1)
            dates.add(dayAfterLast)
            notifyItemInserted(dates.size - 1)
        }
    }

    fun getItem(position: Int): Calendar? = dates.getOrNull(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_date, parent, false)
            .let(::DateViewHolder)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates.getOrNull(position) ?: return
        holder.bind(date)
        holder.itemView.setOnClickListener { listener.onDateClicked(date) }
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(date: Calendar) {
            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
            val text = dateFormat.format(date.time)
            itemView.date.text = text
        }
    }

    interface OnDateClickListener {
        fun onDateClicked(date: Calendar)
    }
}