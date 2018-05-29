package kr.co.sketchlab.gtcall.shlib.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shp on 16. 4. 27..
 */
public class ShDateTime {
    public static Calendar parseDateTimeString(String dateString) {
        if(dateString == null || dateString.equals("") || dateString.equals("null")) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar serverTime = Calendar.getInstance();
        try {
            serverTime.setTime(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return serverTime;
    }
    public static Calendar parseDateTimeString(String dateString, TimeZone tz) {
        if(dateString == null || dateString.equals("") || dateString.equals("null")) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(tz);

        Calendar serverTime = Calendar.getInstance();
        try {
            serverTime.setTime(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return serverTime;
    }
    public static Calendar parseDateString(String dateString) {
        if(dateString == null || dateString.equals("") || dateString.equals("null")) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar serverTime = Calendar.getInstance();
        try {
            serverTime.setTime(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return serverTime;
    }
    public static String dateToShortDate(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
        return dateFormat.format(c.getTime());
    }
    public static String dateToLongDateTimeStr(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(c.getTime());
    }
    public static String dateToLongDateTimeStr(Calendar c, TimeZone tz) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(tz);
        return dateFormat.format(c.getTime());
    }
    public static String dateToLongDateTimeStr2(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(c.getTime());
    }
    public static String dateToLongDate(Calendar c) {
        if(c == null) {
            return "null";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(c.getTime());
    }
    public static String dateToShortTime(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(c.getTime());
    }
    public static String dateToDateString(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(c.getTime());
    }
    public static String toReadableDateTime(String datetime) {
        Calendar c = ShDateTime.parseDateTimeString(datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a K시 mm분");
        return dateFormat.format(c.getTime());
    }
    public static String toReadableDateTimeShort(String datetime) {
        Calendar c = ShDateTime.parseDateTimeString(datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d a K:mm");
        return dateFormat.format(c.getTime());
    }
    public static String toReadableDateTimeShortWithWeekDay(String datetime) {
        Calendar c = ShDateTime.parseDateTimeString(datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d(E) a K:mm");
        return dateFormat.format(c.getTime());
    }
    public static String toReadableDate(Calendar c, String format) {
        Date d = new Date(c.getTimeInMillis());
        return toReadableDate(d, format);
    }

    public static String toYMDE(String datetime) {
        Calendar c = ShDateTime.parseDateTimeString(datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d E");
        return dateFormat.format(c.getTime());
    }
    public static String toAHM(String datetime) {
        Calendar c = ShDateTime.parseDateTimeString(datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("a K:mm");
        return dateFormat.format(c.getTime());
    }
    public static String toAHM(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("a K:mm");
        return dateFormat.format(c.getTime());
    }

    public static String toReadableDate(Date d, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(d.getTime());
        return formattedDate;
    }
    public static String toYMDStr(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        return dateFormat.format(c.getTime());
    }
    public static String secToMinSecStr(long sec) {
        if(sec < 0) {
            return "0초";
        }
        long min = sec / 60;
        sec = sec - (min * 60);
        String str = "";
        if (min != 0) {
            str = Long.toString(min) + "분 ";
        }
        if(sec != 0 || min == 0) {
            str += Long.toString(sec) + "초 ";
        }
        return str;
    }
    public static String secToMinSecStr2(long sec) {
        if(sec < 0) {
            return "00:00";
        }
        long min = sec / 60;
        sec = sec - (min * 60);
        String str = "";
        str = Long.toString(min) + ":";
        if(str.length() <= 2) {
            str = "0" + str;
        }
        String strSec = Long.toString(sec) + "";
        if(strSec.length() < 2) {
            strSec = "0" + strSec;
        }
        return str + strSec;
    }
    public static String minToHourMinStr(long min) {
        if(min < 0) {
            return "0분";
        }
        long hour = (long) (min / 60);
        min = min % 60;
        String str = "";
        if(hour != 0) {
            str = Long.toString(hour) + "시간 ";
        }
        if(min != 0 || hour == 0) {
            str += Long.toString(min) + "분 ";
        }
        return str;
    }

    /**
     * 초를 hh:mm:ss 로
     * @param sec
     * @return
     */
    public static String secToHourMinSecStr(long sec) {
        int h = ((int) (sec / 3600));
        sec -= h*3600;
        int min = (int) (sec / 60);
        sec -= min*60;

        return String.format("%02d:%02d:%02d", h, min, sec);
    }

    public static String secToHourMinSecStr2(long sec) {
        int h = ((int) (sec / 3600));
        sec -= h*3600;
        int min = (int) (sec / 60);
        sec -= min*60;

        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, min, sec);
        } else {
            return String.format("%02d:%02d", min, sec);
        }
    }


    public static long days_diff(Calendar d1, Calendar d2) {
        if(d1 == null || d2 == null) {
            return -999;
        }
        d1.set(Calendar.HOUR_OF_DAY, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);

        d2.set(Calendar.HOUR_OF_DAY, 0);
        d2.set(Calendar.MINUTE, 0);
        d2.set(Calendar.SECOND, 0);
        d2.set(Calendar.MILLISECOND, 0);

        long diff = d1.getTimeInMillis() - d2.getTimeInMillis();
        diff /= (24*3600*1000);
        return diff;
    }

    public static long min_diff(Calendar d1, Calendar d2) {
        long diff = d1.getTimeInMillis() - d2.getTimeInMillis();
        diff /= (1000*60);
        return diff;
    }
}
