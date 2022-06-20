package com.example.youtravel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class TimelineItemModel implements Comparable<TimelineItemModel>{
    String imageUrl, imageLocation,imageDate, imageRating;

    public TimelineItemModel(String imageUrl, String imageLocation, String imageDate, String imageRating) {
        this.imageUrl = imageUrl;
        this.imageLocation = imageLocation;
        this.imageDate = imageDate;
        this.imageRating = imageRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getImageDate() {
        return imageDate;
    }

    public String getImageRating() {
        return imageRating;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(TimelineItemModel u) {
        if (getImageDate() == null || u.getImageDate() == null) {
            return 0;
        }
        return LocalDate.parse(getImageDate()).compareTo(LocalDate.parse(u.getImageDate()));
    }

}
