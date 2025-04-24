package models;

import models.enums.Season;

public class Date {
    int hour;
    int minute;
    String day;
    Season season;
    int year;
    int month;
    int dayOfMonth;

    public Date(int hour, int minute, String day, Season season, int year, int month, int dayOfMonth) {
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.season = season;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
