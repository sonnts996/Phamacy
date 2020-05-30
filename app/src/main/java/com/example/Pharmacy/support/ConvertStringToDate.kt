package com.example.Pharmacy.support

import android.annotation.SuppressLint
import kotlin.text.*

/**
 * convert (invert) date to string
 * support format dd-MM-yyyy, dd/MM/yyyy, dd\mm\yyyy
 * return only dd-MM-yyyy
 */
class ConvertStringToDate {
    var day = 0
        private set
    var month = 0
        private set
    var year = 0
        private set
    private val format = "%02d-%02d-%04d"

    val invert: String
        @SuppressLint("DefaultLocale")
        get() = format.format(day, month, year)

    /**
     * convert date string format to day, month and year
     * support for string format dd-MM-yyyy, dd/MM/yyyy, dd\MM\yyyy, dd:MM:yyyy
     * @param dateString date string
     */
    constructor(dateString: String) {
        convert(dateString)
    }

    /**
     * convert day, month and year to string format dd-MM-yyyy
     * @param day day in month value
     * @param month month in year value
     * @param year year value
     */
    constructor(day: Int, month: Int, year: Int) {
        this.day = day
        this.month = month
        this.year = year
    }

    /**
     * convert date string format to day, month and year
     * support for string format dd-MM-yyyy, dd/MM/yyyy, dd\MM\yyyy, dd:MM:yyyy
     *
     * @param dateString date string
     */
    private fun convert(dateString: String) {
        val temp: Array<String>
        if (dateString.length == 10) {
            val s = dateString.replace("/", "-").replace("\\", "-").replace(":", "-")
            if (dateString.contains("-")) {
                temp = s.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                day = Integer.parseInt(temp[0])
                month = Integer.parseInt(temp[1])
                year = Integer.parseInt(temp[2])
            } else {
                throw RuntimeException("Not support format: $dateString")
            }
        } else {
            throw RuntimeException("Not support format: $dateString")
        }
    }
}
