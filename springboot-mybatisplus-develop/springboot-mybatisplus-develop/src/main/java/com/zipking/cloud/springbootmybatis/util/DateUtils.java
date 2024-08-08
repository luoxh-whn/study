package com.zipking.cloud.springbootmybatis.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date getDate(String date){
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try{
            result = dateFormat1.parse(date);
        }catch(Exception e){

        }
        return result;
    }

    public static void getPreMonthFirstDay(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        System.out.println(sd.format(calendar.getTime()));
    }

    public static void getCurrentMonthLastDay(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, cal.getActualMaximum(5));
        System.out.println(sd.format(cal.getTime()));
    }
}
