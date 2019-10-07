package com.example.moviepreferences;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public Reviews storedStringToReviews(String value) {
        if (value == null) {
            return null;
        }
        List<String> list = Arrays.asList(value.split("\\s*,\\s*"));
        return new Reviews(list);
    }

    @TypeConverter
    public String reviewsToStoredString(Reviews r) {
        if (r == null) {
            return null;
        }
        StringBuilder value = new StringBuilder();
        for (String review : r.getReviews()) {
            value.append(review).append(",");
        }
        return value.toString();
    }
}
