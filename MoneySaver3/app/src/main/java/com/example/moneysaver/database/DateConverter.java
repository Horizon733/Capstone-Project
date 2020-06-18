package com.example.moneysaver.database;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }
    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    static DateFormat df = new SimpleDateFormat("MMM dd, yyyy"); //for example "yyyy-MM-dd HH:mm:ss"
    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                Log.e("Date formatted",""+df.parse(value));
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

}
