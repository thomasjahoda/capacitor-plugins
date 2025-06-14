package com.capacitorjs.plugins.localnotifications;

import android.text.format.DateUtils;
import com.getcapacitor.JSObject;
import java.text.ParseException;
import java.util.Date;

public class LocalNotificationSchedule {

    private Date at;
    private Boolean repeats;
    private String every;
    private Integer count;

    private DateMatch on;

    private Boolean whileIdle;

    private JSObject scheduleObj;

    public LocalNotificationSchedule(JSObject schedule) throws ParseException {
        this.scheduleObj = schedule;
        // Every specific unit of time (always constant)
        buildEveryElement(schedule);
        // Count of units of time from every to repeat on
        buildCountElement(schedule);
        // At specific moment of time (with repeating option)
        buildAtElement(schedule);
        // Build on - recurring times. For e.g. every 1st day of the month at 8:30.
        buildOnElement(schedule);

        // Schedule this notification to fire even if app is idled (Doze)
        this.whileIdle = schedule.getBoolean("allowWhileIdle", false);
    }

    public LocalNotificationSchedule() {}

    private void buildEveryElement(JSObject schedule) {
        // 'year'|'month'|'two-weeks'|'week'|'day'|'hour'|'minute'|'second';
        this.every = schedule.getString("every");
    }

    private void buildCountElement(JSObject schedule) {
        this.count = schedule.getInteger("count", 1);
    }

    private void buildAtElement(JSObject schedule) throws ParseException {
        this.repeats = schedule.getBool("repeats");
        String dateString = schedule.getString("at");
        if (dateString != null) {
            this.at = DateTimeUtil.parseJsDateTime(dateString);
        }
    }

    private void buildOnElement(JSObject schedule) {
        JSObject onJson = schedule.getJSObject("on");
        if (onJson != null) {
            this.on = new DateMatch();
            on.setYear(onJson.getInteger("year"));
            on.setMonth(onJson.getInteger("month"));
            on.setDay(onJson.getInteger("day"));
            on.setWeekday(onJson.getInteger("weekday"));
            on.setHour(onJson.getInteger("hour"));
            on.setMinute(onJson.getInteger("minute"));
            on.setSecond(onJson.getInteger("second"));
        }
    }

    public DateMatch getOn() {
        return on;
    }

    public JSObject getOnObj() {
        return this.scheduleObj.getJSObject("on");
    }

    public void setOn(DateMatch on) {
        this.on = on;
    }

    public Date getAt() {
        return at;
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public Boolean getRepeats() {
        return repeats;
    }

    public void setRepeats(Boolean repeats) {
        this.repeats = repeats;
    }

    public String getEvery() {
        return every;
    }

    public void setEvery(String every) {
        this.every = every;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean allowWhileIdle() {
        return this.whileIdle;
    }

    public boolean isRepeating() {
        return Boolean.TRUE.equals(this.repeats);
    }

    public boolean isRemovable() {
        if (every == null && on == null) {
            if (at != null) {
                return !isRepeating();
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Get constant long value representing specific interval of time (weeks, days etc.)
     */
    public Long getEveryInterval() {
        switch (every) {
            case "year":
                // This case is just approximation as not all years have the same number of days
                return count * DateUtils.WEEK_IN_MILLIS * 52;
            case "month":
                // This case is just approximation as months have different number of days
                return count * 30 * DateUtils.DAY_IN_MILLIS;
            case "two-weeks":
                return count * 2 * DateUtils.WEEK_IN_MILLIS;
            case "week":
                return count * DateUtils.WEEK_IN_MILLIS;
            case "day":
                return count * DateUtils.DAY_IN_MILLIS;
            case "hour":
                return count * DateUtils.HOUR_IN_MILLIS;
            case "minute":
                return count * DateUtils.MINUTE_IN_MILLIS;
            case "second":
                return count * DateUtils.SECOND_IN_MILLIS;
            default:
                return null;
        }
    }

    /**
     * Get next trigger time based on calendar and current time
     *
     * @param currentTime - current time that will be used to calculate next trigger
     * @return millisecond trigger
     */
    public Long getNextOnSchedule(Date currentTime) {
        return this.on.nextTrigger(currentTime);
    }
}
