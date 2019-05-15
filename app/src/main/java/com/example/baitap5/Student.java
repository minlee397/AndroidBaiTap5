package com.example.baitap5;

import java.time.LocalDate;
import java.time.LocalTime;

public class Student {
    private String mMSSV;
    private String mImages;
    private String mFirstName;
    private String mLastName;
    private LocalDate mUpdatedDay;
    private LocalTime mUpdatedTime;

    public Student() {

    }

    public Student(String mMSSV, String mImages, String mFirstName, String mLastName, LocalDate mUpdatedDay, LocalTime mUpdatedTime) {
        this.mMSSV = mMSSV;
        this.mImages = mImages;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mUpdatedDay = mUpdatedDay;
        this.mUpdatedTime = mUpdatedTime;
    }

    public String getmMSSV() {
        return mMSSV;
    }

    public void setmMSSV(String mMSSV) {
        this.mMSSV = mMSSV;
    }

    public String getmImages() {
        return mImages;
    }

    public void setmImages(String mImages) {
        this.mImages = mImages;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public LocalDate getmUpdatedDay() {
        return mUpdatedDay;
    }

    public void setmUpdatedDay(LocalDate mUpdatedDay) {
        this.mUpdatedDay = mUpdatedDay;
    }

    public LocalTime getmUpdatedTime() {
        return mUpdatedTime;
    }

    public void setmUpdatedTime(LocalTime mUpdatedTime) {
        this.mUpdatedTime = mUpdatedTime;
    }
}
