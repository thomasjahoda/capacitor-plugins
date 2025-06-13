package com.capacitorjs.plugins.localnotifications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {
    public static String JS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static Date parseJsDateTime(String dateTimeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(JS_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(dateTimeString);
    }
}
