package br.com.woobe.notepic.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by willian alfeu on 17/01/2017.
 */

public class TimeUtils {
    public static long getTimeMillis() {
        Calendar calendar = new GregorianCalendar();
        return calendar.getTimeInMillis();
    }

    public static String fillTime(String s) {
        if (s.length() < 2) s = "0" + s;
        return s;
    }

    public static String fillTime(int s) {
        return fillTime(String.valueOf(s));
    }

}
