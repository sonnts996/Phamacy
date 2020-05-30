package com.example.Pharmacy.support;

/**
 * convert (invert) date to string
 * support format dd-MM-yyyy, dd/MM/yyyy, dd\mm\yyyy
 * return only dd-MM-yyyy
 */
public class ConvertStringToDate {
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String format = "%02d-%02d-%04d";

    /**
     * convert date string format to day, month and year
     * support for string format dd-MM-yyyy, dd/MM/yyyy, dd\MM\yyyy, dd:MM:yyyy
     * @param dateString date string
     */
    public ConvertStringToDate(String dateString) {
        convert(dateString);
    }

    /**
     * convert day, month and year to string format dd-MM-yyyy
     * @param day day in month value
     * @param month month in year value
     * @param year year value
     */
    public ConvertStringToDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * convert date string format to day, month and year
     * support for string format dd-MM-yyyy, dd/MM/yyyy, dd\MM\yyyy, dd:MM:yyyy
     *
     * @param dateString date string
     */
    private void convert(String dateString) {
        String[] temp;
        if (dateString.length() == 10) {
            String s = dateString.replace("/", "-").replace("\\", "-").replace(":", "-");
            if (dateString.contains("-")) {
                temp = s.split("-");
                day = Integer.parseInt(temp[0]);
                month = Integer.parseInt(temp[1]);
                year = Integer.parseInt(temp[2]);
            } else {
                throw new RuntimeException("Not support format: " + dateString);
            }
        } else {
            throw new RuntimeException("Not support format: " + dateString);
        }
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public String getInvert() {
        return String.format(format, day, month, year);
    }

    public String getFormat() {
        return this.format;
    }
}
