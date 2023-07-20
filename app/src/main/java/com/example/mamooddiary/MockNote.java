package com.example.mamooddiary;

public class MockNote {
    private int day;
    private int month;
    private int year;
    private String message;
    private String mood;

    public MockNote(int day, int month, int year, String message, String mood) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.message = message;
        this.mood = mood;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
