package com.mizani.news_compose.utils

import com.mizani.news_compose.utils.DateFormatter.convertDateTo
import com.mizani.news_compose.utils.DateFormatter.convertToReadable
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    val DEFAULT_READABLE_FORMAT = "EEE, dd MMM yyyy HH:mm"

    fun Date.convertToReadable(pattern: String = DEFAULT_READABLE_FORMAT): String {
        return try {
            SimpleDateFormat(pattern).format(this)
        } catch (e: Exception) {
            ""
        }
    }
    fun String.convertDateTo(
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        outputPattern: String = DEFAULT_READABLE_FORMAT
    ): String {
        return try {
            SimpleDateFormat(inputPattern).parse(this).convertToReadable()
        } catch (e: Exception) {
            ""
        }
    }

    fun Date?.toEndDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    fun Date?.toStartDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun setDate(year: Int, month: Int, dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return calendar.time
    }

}