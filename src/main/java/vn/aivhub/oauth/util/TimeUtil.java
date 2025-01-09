package vn.aivhub.oauth.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;


@Log4j2
public class TimeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TimeUtil.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat PARSER_TO_NEW_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final Long ONE_HOURS_MILI_SECOND_TIME = 60 * 60 * 1000L;

    public static String getCurrentTime() {
        return PARSER_TO_NEW_FORMAT.format(new Date(System.currentTimeMillis()));
    }

    public static String getTimeCreateFile() {
        return new SimpleDateFormat("yyyyMMdd")
                .format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentYearMonthDateHour() {
        return new SimpleDateFormat("yyyyMMddHH")
                .format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentDateTimeFromTime(Long time) {
        return PARSER_TO_NEW_FORMAT.format(new Date(time));
    }

    public static long getTimeLongBeforeXMinute(Integer minute) {
        return System.currentTimeMillis() - minute * 60 * 1000;
    }

    public static Long getMilliseconds() {
        return System.currentTimeMillis();
    }

    public static long getCurrentSecondTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getSecondTime(Long time) {
        if ((time + "").length() > 12)
            return time / 1000;
        return time;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static String getDateStr(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(time));
    }

    public static String getDateStr(LocalDateTime localDateTime) {
        return localDateTime.format(ofPattern("yyyy-MM-dd"));
    }

    public static String getHourDateStr(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
        return simpleDateFormat.format(new Date(time));
    }

    public static Long getTimeFromHourDate(String hourDate) {
        final String[] split = hourDate.split("-");
        return now()
                .withYear(Integer.parseInt(split[0]))
                .withMonth(Integer.parseInt(split[1]))
                .withDayOfMonth(Integer.parseInt(split[2]))
                .withHour(Integer.parseInt(split[3]))
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static String generateDateFormatFromLong(Long millisecond, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(millisecond));
    }


    public static String getStringDate(Long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"/*, new Locale("vi","VN")*/);
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static Long getTimeDiff(Long timestamp) {
        return System.currentTimeMillis() / 1000 - timestamp;
    }

    public static Long convertMiniSecondToSecond(Long time) {
        return time / 1000;
    }

    public static String secondToDateStr(Long timeInSecond) {
        return miliSecondToDateStr(timeInSecond * 1000);
    }

    public static String miliSecondToDateStr(Long timeInMilliSecond) {
        Date date = new Date(timeInMilliSecond);
        return PARSER_TO_NEW_FORMAT.format(date);
    }

    public static String miliSecondToDateTimeStr(Long timeInMilliSecond) {
        Date date = new Date(timeInMilliSecond);
        return format.format(date);
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(date);
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getCurrentDay() {
        return LocalDate.now().getDayOfWeek().name();
    }

    public static String getNameOfDay(Long time) {
        LocalDate date =
                Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.getDayOfWeek().name();
    }

    public static String convertTimeForSendLog(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        return format.format(date);
    }

    public static Timestamp getPreOneMinute(long time) {
        return new Timestamp(time * 1000 - 1000 * 60);
    }

    public static Timestamp minusTime(Timestamp time, int amount, TemporalUnit unit) {
        return Timestamp.valueOf(time.toLocalDateTime().minus(amount, unit));
    }


    public static Date preWeekDate(int nunPreWeek) {
        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        calendar.add(Calendar.WEEK_OF_MONTH, -nunPreWeek);

        // Use the date formatter to produce a formatted date string
        return calendar.getTime();
    }

    private static Date preMonthDate(int nunPreMonth) {
        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        calendar.add(Calendar.MONTH, -nunPreMonth);

        // Use the date formatter to produce a formatted date string
        return calendar.getTime();

    }

    public static Long getFirstTimeOfThisWeek() {
        return getFistDayOfWeek(0).getTime();
    }

    public static Long getFirstTimeOfWeek(int numPreWeek) {
        return getFistDayOfWeek(numPreWeek).getTime();
    }

    private static Date getFistDayOfWeek(int nunPreWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(preWeekDate(nunPreWeek));
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Long getCurrentDateStartDay() {
        return now()
                .withHour(0)
                .withMinute(0)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getCurrentDateEndDay() {
        return now()
                .withHour(23)
                .withMinute(59)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getSecondLongTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS);
    }

    public static Long getSecondTimeToNow(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS) -
                localDateTime.atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS);
    }

    public static Timestamp getNowInTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Long getLastTimeOfMonth(int nunPreMonth) {
        return getLastDayOfMonth(nunPreMonth).getTime();
    }

    private static Date getLastDayOfMonth(int nunPreMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(preMonthDate(nunPreMonth));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(sdf.format(calendar.getTime()));
        return calendar.getTime();
    }

    public static Long calculateTimeWaitingCDN(long sizeBytes) {
        long min = 50;

        long timeWaiting = sizeBytes >> 17;

        return timeWaiting > min ? timeWaiting : min;
    }

    public static String getNowInString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"/*, new Locale("vi","VN")*/);
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }


    public static Long hourToMillisseconds(int hour) {
        return (long) (hour * 60 * 60 * 1000);
    }

    public static Long dayToMillisseconds(int day) {
        return ((long) day * 24 * 60 * 60 * 1000);
    }


    public static Timestamp getPreWeek() {
        return new Timestamp(getMilliseconds() - 7 * 24 * 60 * 60 * 1000);
    }

    public static Field<Object> unixTimestamp(Field field) {
        String fieldName = field.toString();
        return DSL.field("unix_timestamp(" + fieldName.replaceAll("\"", "`") + ")");
    }

    public static long getStartTimeOfWeek(Integer week, Integer year) {
        // Get calendar set to current date and time
        final long time = now()
                .withYear(year != null ? year : now().getYear())
                .withHour(0)
                .withMinute(0)
                .with(firstDayOfYear())
                .plusWeeks(week - 1)
                .getLong(INSTANT_SECONDS) * 1000;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime().getTime();
    }

    public static int getStartDayOfWeek() {
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String format = new SimpleDateFormat("dd").format(c.getTime().getTime());
        return Integer.parseInt(format);
    }

    public static int getStartMonthOfWeek() {
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String format = new SimpleDateFormat("MM").format(c.getTime().getTime());
        return Integer.parseInt(format);
    }

    public static int getEndDayOfWeek() {
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String format = new SimpleDateFormat("dd").format(c.getTime().getTime());
        return Integer.parseInt(format);
    }

    public static int getEndMonthOfWeek() {
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String format = new SimpleDateFormat("MM").format(c.getTime().getTime());
        return Integer.parseInt(format);
    }

    public static long getStartTimeOfWeek(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String format = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime().getTime());
        final String[] list = format.split("-");
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(Integer.parseInt(list[2]))
                .withMonth(Integer.parseInt(list[1]))
                .withDayOfMonth(Integer.parseInt(list[0]))
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static ZonedDateTime getStartTimeOfMondayZDT() {
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String format = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime().getTime());
        final String[] list = format.split("-");
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(Integer.parseInt(list[2]))
                .withMonth(Integer.parseInt(list[1]))
                .withDayOfMonth(Integer.parseInt(list[0]));
    }

    public static long getStartTimeOfDayFromTime(Long time) {
        String format = new SimpleDateFormat("dd-MM-yyyy").format(time);
        final String[] list = format.split("-");
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(Integer.parseInt(list[2]))
                .withMonth(Integer.parseInt(list[1]))
                .withDayOfMonth(Integer.parseInt(list[0]))
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getEndTimeOfDayFromTime(Long time) {
        String format = new SimpleDateFormat("dd-MM-yyyy").format(time);
        final String[] list = format.split("-");
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withYear(Integer.parseInt(list[2]))
                .withMonth(Integer.parseInt(list[1]))
                .withDayOfMonth(Integer.parseInt(list[0]))
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getStartTimeOfMonth(Long time) {
        String format = new SimpleDateFormat("MM-yyyy").format(time);
        final String[] list = format.split("-");
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(Integer.parseInt(list[1]))
                .withMonth(Integer.parseInt(list[0]))
                .withDayOfMonth(1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getStartTimeOfMonth() {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(getThisYear())
                .withMonth(getThisMonth())
                .withDayOfMonth(1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getStartTimeOfYear(Long time) {
        String year = new SimpleDateFormat("yyyy").format(time);
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withYear(Integer.parseInt(year))
                .withMonth(1)
                .withDayOfMonth(1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getYearByWeek(Integer week, Integer year) {
        return now()
                .withYear(year != null ? year : now().getYear())
                .withHour(0)
                .withMinute(0)
                .with(firstDayOfYear())
                .plusWeeks(week - 1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getCurrentLongBeforeXHour(Integer hour) {
        return now().minusHours(hour).getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getEndTimeOfWeek(Integer week, Integer year) {

        // Get calendar set to current date and time
        final long time = now()
                .withYear(year != null ? year : now().getYear())
                .withHour(0)
                .withMinute(0)
                .with(firstDayOfYear())
                .plusWeeks(week - 1)
                .getLong(INSTANT_SECONDS) * 1000;

        return getEndTimeOfWeek(time);

    }

    public static long getFirstDayOfWeek(Integer week, Integer year) {
        return now()
                .withYear(year != null ? year : now().getYear())
                .with(firstDayOfYear())
                .plusWeeks(week)
                .minusDays(2) // Hiện tại tuần bị lệch 2 ngày, report tuần từ t2 -> chủ nhật nên phải trừ đi 1 ngày
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long minuteToLong(Integer minute) {
        return (long) minute * 60 * 1000;
    }

    public static Long secondToMilliseconds(Integer second) {
        return (long) second * 1000;
    }

    public static Timestamp stringToTimeStamp(String value) {
        try {
            return new Timestamp(format.parse(value).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Timestamp stringToTimeStamp(String value, String format) {
        try {
            SimpleDateFormat fm = new SimpleDateFormat(format);
            return new Timestamp(fm.parse(value).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Timestamp stringYMDToTimeStamp(String value) {
        try {
            return new Timestamp(sdf.parse(value).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String longTimeToYYMMDD(Long time) {
        try {
            return sdf.format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }

    public static String longTimeToYYMMDD(LocalDateTime localDateTime) {
        try {
            return localDateTime.format(ofPattern(sdf.toPattern()));
        } catch (Exception e) {
            return "";
        }
    }

    public static String longTimeToDateMonth(Long time) {
        try {
            return new SimpleDateFormat("dd-MM").format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }

    public static Timestamp incrementTimeWithDays(Timestamp time, Long numDays) {
        LocalDateTime localDateTime = time.toLocalDateTime().plusDays(numDays);
        LocalDateTime roundFloor = localDateTime.truncatedTo(ChronoUnit.DAYS);
        return Timestamp.valueOf(roundFloor);
    }

    public static LocalDate getLastDayOfMonth(String date) {
        LocalDate convertedDate = LocalDate.parse(date, ofPattern("yyyy-MM-dd"));
        convertedDate = convertedDate.withDayOfMonth(
                convertedDate.getMonth().length(convertedDate.isLeapYear()));
        return convertedDate;
    }

    public static String getStartOfPreviousDate() {
        return sdf.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) + " 00:00:00";
    }

    public static String getStartOfPreviousDateIgnoreHour() {
        return sdf.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
    }

    public static String getEndOfPreviousDate() {
        return sdf.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) + " 23:59:59";
    }

    public static Long dateStringToLong(String date) {
        try {
            Date d = format.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            LOG.warn(e.getMessage(), e);
        }
        return null;
    }

    // function theo format date

    public static Long dateStringToLong(String date, String formatDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatDate);
            Date d = format.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            LOG.warn(e.getMessage(), e);
            return null;
        }
    }

    public static String longTimeToString(Long time, String formatDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatDate);
            return format.format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }

    public static Long getSecondBeforeXDay(int day) {
        return now()
                .minusDays(day)
                .getLong(INSTANT_SECONDS);
    }

    public static Long getSecondBeforeXStartDay(int day) {
        return now()
                .withHour(0)
                .withMinute(0)
                .minusDays(day)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getLongTimeBeforeXHours(int hours) {
        return now()
                .minusHours(hours)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getLongTimeBeforeOneWeek() {
        return now()
                .minusWeeks(1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getLongTimeBeforeXDay(int day) {
        return now()
                .minusDays(day)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getLongTimeBeforeXWeek(int week) {
        return now()
                .minusWeeks(week)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getTimeBeforeOneMonths() {
        return getTimeBeforeXMonths(1);
    }

    public static Long getTimeBeforeXMonths(int numMonth) {
        return now()
                .minusMonths(numMonth)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static boolean checkTimeBetween(Timestamp from, Timestamp to, Long timeCompare) {
        return timeCompare >= from.getTime() && timeCompare <= to.getTime();
    }

    /**
     * start day of week 00:00 monday
     *
     * @return
     */
    public static Integer getWeekOfYear() {
        int week = Integer.parseInt(new SimpleDateFormat("w").format(new Date()));

        /*if sunday -> old week*/
        Calendar cal = Calendar.getInstance();
        return getWeek(week, cal);
    }

    public static Integer getWeekOfYearFromTime(Long time) {
        int week = Integer.parseInt(new SimpleDateFormat("w").format(new Date(time)));

        /*if sunday -> old week*/
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return getWeek(week, cal);
    }

    public static Integer normalWeek(Integer week) {
        if (week == null || week < 1 || week > 52) {
            return getWeekBeforeXWeek(1);
        }
        return week;
    }

    public static Integer normalYearByWeek(Integer year) {
        if (year == null || year < 1000) {
            return getYearBeforeXWeek(1);
        }
        return year;
    }

    public static int getWeekBeforeXWeek(int xWeek) {
        final Long time = getLongTimeBeforeXWeek(xWeek);
        int week = Integer.parseInt(new SimpleDateFormat("w").format(new Date(time)));
        /*if sunday -> old week*/
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return getWeek(week, cal);
    }

    private static int getWeek(int week, Calendar cal) {
        boolean sunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        if (sunday) {
            if (week == 1) {
                week = 53;
            }
            week = week - 1;
        }
        return week;
    }

    public static int getCurrentYearByWeek() {
        // Get calendar set to current date and time
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, 6);
        DateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(c.getTime()));
    }

    public static int getCurrentYearByWeekFromTime(Long time) {
        // Get calendar set to current date and time
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, 6);
        DateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(c.getTime()));
    }

    public static int getYearBeforeXWeek(Integer numWeekBefore) {
        // Get calendar set to current date and time
        final Long time = getLongTimeBeforeXWeek(numWeekBefore);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, 6);
        DateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(c.getTime()));
    }

    public static int getYearBefore(Integer numYearBefore) {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) - numYearBefore;
    }

    public static long getStartTimeBeforeXWeek(Integer numWeekBefore) {
        // Get calendar set to current date and time
        final long time = now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .minusWeeks(numWeekBefore)
                .getLong(INSTANT_SECONDS) * 1000;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime().getTime();
    }

    public static long getStartTimeBeforeXMonth(Integer numMonthBefore) {
        // Get calendar set to current date and time
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withDayOfMonth(1)
                .minusMonths(numMonthBefore)
                .getLong(INSTANT_SECONDS) * 1000;
    }

//    public static long getEndTimeBeforeXMonth(Integer numMonthBefore) {
//        // Get calendar set to current date and time
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(time);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int year = cal.get(Calendar.YEAR);
//        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        return now()
//                .withHour(23)
//                .withMinute(59)
//                .withSecond(59)
//                .minusMonths(numMonthBefore)
//                .getLong(INSTANT_SECONDS) * 1000;
//    }

    public static long getStartTimeBeforeXDay(Integer numDayBefore) {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .minusDays(numDayBefore)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static LocalDateTime getStartTimeBeforeXDayLDT(Integer numDayBefore) {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .minusDays(numDayBefore)
                .toLocalDateTime();
    }

    public static LocalDateTime getStartTimeOfDayLDT() {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toLocalDateTime();
    }

    public static LocalDateTime getEndTimeOfDayLDT() {
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(99)
                .toLocalDateTime();
    }

    public static LocalDateTime getEndTimeBeforeXDayLDT(Integer numDayBefore) {
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(99)
                .minusDays(numDayBefore)
                .toLocalDateTime();
    }

    public static LocalDateTime getTimeBeforeXDayLDT(Integer numDayBefore) {
        return now()
                .minusDays(numDayBefore)
                .toLocalDateTime();
    }

    public static long getStartTimeBeforeOneMonth() {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .minusMonths(1)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getStartTimeBeforeXDayFromTime(Long time, Integer numDayBefore) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        return zdt
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .minusDays(numDayBefore)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getStartTimeBeforeXMonthFromTime(Long time, Integer numMonthBefore) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        return zdt
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withDayOfMonth(1)
                .minusMonths(numMonthBefore)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getEndTimeBeforeXWeek(Integer numWeekBefore) {
        // Get calendar set to current date and time
        final long time = now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .minusWeeks(numWeekBefore)
                .getLong(INSTANT_SECONDS) * 1000;
        return getEndTimeOfWeek(time);
    }

    public static long getEndTimeBeforeXDay(Integer numMonthBefore) {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .minusMonths(numMonthBefore)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    private static long getEndTimeOfWeek(Long time) {
        Calendar c = Calendar.getInstance();
        if (time != null) c.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE, 6);
        return c.getTime().getTime();
    }

    public static Boolean isCurrentWeek(Integer week, Integer year) {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.WEEK_OF_YEAR) == week && getCurrentYearByWeek() == year;
    }

    public static Boolean isCurrentYear(Integer year) {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) == year;
    }

    // Index của chủ nhật là 1
    public static Boolean isWeekend() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static Pair<Long, Long> initStartAndEndOfYear(Integer year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date start = cal.getTime();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);

        Date end = cal.getTime();
        return Pair.of(start.getTime(), end.getTime());
    }

    public static LocalDateTime parserToLocalDateTime(String input) {
        if (input == null) return null;
        try {
            if (input.length() > 11) {
                return parserTimeFromDateTimeString(input);
            }
            Long time = parserTimeFromDateString(input);
            LocalDate localDate = longToLocalDate(time);
            if (localDate == null) return null;
            return localDate.atStartOfDay()
                    .withHour(now().getHour())
                    .withMinute(now().getMinute());
        } catch (Exception e) {
            log.error("Fail to parser date time: {}", input, e);
            return null;
        }
    }

    public static String parserToNewFormat(String input, SimpleDateFormat simpleDateFormat) {
        try {
            LocalDateTime localDateTime = parserToLocalDateTime(input);
            if (localDateTime == null) return null;
            return simpleDateFormat.format(localDateTime);
        } catch (Exception exception) {
            log.error("fail to parser: {} to  {}", input, simpleDateFormat, exception);
            return null;
        }
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER_HH_MM = ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_HH_MM_SS = ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_HH_MM_Z = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static LocalDateTime toLocalDateTimeOrNow(String input) {
        if (input == null) return LocalDateTime.now();
        LocalDateTime localDateTime = parserTimeFromDateTimeString(input);
        if (localDateTime == null) return LocalDateTime.now();
        return localDateTime;
    }

    public static LocalDateTime parserTimeFromDateTimeString(String input) {
        return Stream.of(
                        DATE_TIME_FORMATTER_HH_MM_SS,
                        DATE_TIME_FORMATTER_HH_MM,
                        DATE_TIME_FORMATTER_HH_MM_Z)
                .map(dateTimeFormatter -> {
                    try {
                        return LocalDateTime.parse(input, dateTimeFormatter);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static Long parserTimeFromDateString(String input) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
            final SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM/dd");
            final SimpleDateFormat format4 = new SimpleDateFormat("dd/MM/yyyy");
            if (input.split("-").length == 3) {
                if (input.split("-")[0].length() == 4) {
                    Date date = format1.parse(input);
                    return date.getTime();
                } else {
                    Date date = format2.parse(input);
                    return date.getTime();
                }
            } else if (input.split("-").length == 2) {
                Date date = format2.parse(input.concat("-" + now().getYear()));
                return date.getTime();
            }
            if (input.split("/").length == 3) {
                if (input.split("/")[0].length() == 4) {
                    Date date = format3.parse(input);
                    return date.getTime();
                } else {
                    Date date = format4.parse(input);
                    return date.getTime();
                }
            } else if (input.split("/").length == 2) {
                Date date = format4.parse(input.concat("/" + now().getYear()));
                return date.getTime();
            }
            Date date = format2.parse(input.concat("-" + now().getMonthValue() + "-" + now().getYear()));
            return date.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime fromUTC(String time) {
        if (time == null || "".equals(time)) return null;
        try {
            return LocalDateTime.parse(time.replaceFirst("UTC", "").trim(), ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.error("Fail to parse time {}", time, e);
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LocalDateTime localDateTime = fromUTC("2026-07-15 04:00:00 UTC");
        String s = localDateTime.toString();
        System.out.println(getYearBefore(2));
        final long startTimeBeforeXWeek = getStartTimeBeforeXWeek(1);
        System.out.println(getStartTimeBeforeXDay(5));
        System.out.println(getCurrentDateEndDay());
        System.out.println(getTimeBeforeOneMonths());
        System.out.println(getCurrentDay());
        System.out.println(getEndTimeBeforeXDay(1));
        System.out.println(getStartTimeOfDay());
        System.out.println(getCurrentDateEndDay() + "-pre");
        System.out.println(getCurrentDay() + "-current day");
        System.out.println(getNameOfDay(1615222800000L).toLowerCase() + "-name day");
        System.out.println(getHourOfAnyDay(1618765200000L, 20) + "-any day");

        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(1619099500000L)));
        System.out.println("demo-12");
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(getHourOfAnyDay(1619099500000L, 20))));
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(getHourOfAnyDay(1619199600000L, 20))));
        System.out.println("demo");
        getDatesBetween(1619099500000L, 1619199600000L).forEach(d ->
                System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(d)))
        );

        System.out.println(getCurrentDay() + "-current day");
        System.out.println(getNameOfDay(1615222800000L).toLowerCase() + "-name day");

        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(getStartTimeBeforeXDay(-1))));


        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(getStartTimeBeforeXDayFromTime(1615222800000L, 1))) + " - before");
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(1619024400000L)) + " - current");

        Long aLong = dateStringToLong("20-04-2021", "dd-MM-yyyy");
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(aLong)) + " - demo - " + aLong);

        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(1621357200000L)) + " - demo hour -");
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(1621364400944L)) + " - demo hour -");
        System.out.println(new SimpleDateFormat("HH:mm-dd-MM-yyyy").format(new Date(1621386000453L)) + " - demo hour -");
    }

    public static Long getStartTime2020() {
        return now()
                .withYear(2020)
                .withDayOfMonth(1)
                .withMonth(1)
                .withHour(0)
                .withMinute(0)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static String getDate(Long time) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(time));
    }

    public static String getHour(Long time) {
        return new SimpleDateFormat("HH").format(new Date(time));
    }

    public static String getStartTimeOfWeekByTime(Long time) {
        /*if sunday -> old week*/
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.setTimeInMillis(time);
        // Set the calendar to monday of the current week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(cal.getTime());
    }

    public static Integer getDayIndex(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getMonthOfYear(Long time) {
        return new SimpleDateFormat("MM/yyyy").format(new Date(time));
    }

    public static Integer getMonth(Long time) {
        return Integer.parseInt(new SimpleDateFormat("MM").format(new Date(time)));
    }

    public static Integer getYear(Long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(time)));
    }

    public static String getMonthOfYear(Long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    public static Long getOneDayMini() {
        return 24 * 60 * 60 * 1000L;
    }

    public static Long getOneHourMini() {
        return 60 * 60 * 1000L;
    }

    public static Long getOneMonthMini() {
        return 30 * 24 * 60 * 60 * 1000L;
    }

    public static Long getStartTimeOfDay() {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getHourOfDay(Integer hour) {
        return now()
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getHourOfAnyDay(Long time, Integer hour) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        return zdt
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static Long getEndTimeOfDay() {
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static int getNumDayOfMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static long getStartTimeOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getTimeInMillis();
    }

    public static long getEndTimeOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withMonth(month)
                .withYear(year)
                .withDayOfMonth(numDays)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getEndTimeOfMonth(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withYear(year)
                .withMonth(month)
                .withDayOfMonth(numDays)
                .getLong(INSTANT_SECONDS) * 1000;
    }

    public static long getEndTimeOfMonthBeforeXMonth(Integer xMonth) {
        final long currentTimeBeforeXMonth = now()
                .minusMonths(xMonth)
                .getLong(INSTANT_SECONDS) * 1000;
        return getEndTimeOfMonth(currentTimeBeforeXMonth);
    }

    public static long getEndTimeOfMonth() {
        return getEndTimeOfMonth(getThisMonth(), getThisYear());
    }

    public static int getThisYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getThisMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }


    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
                localDateTime.atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS) * 1000;
    }

    public static boolean gte(LocalDateTime v1, LocalDateTime v2) {
        return localDateTimeToLong(v1) > localDateTimeToLong(v2);
    }

    public static Long localDateToLong(LocalDate localDate) {
        return localDate == null ? null :
                now()
                        .withYear(localDate.getYear())
                        .withMonth(localDate.getMonthValue())
                        .withDayOfMonth(localDate.getDayOfMonth())
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0)
                        .getLong(INSTANT_SECONDS) * 1000;
    }

    public static LocalDate longToLocalDateOrNow(Long time) {
        if (time == null || time <= 0) time = getMilliseconds();
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime getLocalDateTimeBeforeXDay(int numDayBefore) {
        return now()
                .minusDays(numDayBefore)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toLocalDateTime();
    }

    public static LocalDate longToLocalDate(Long time) {
        if (time == null || time <= 0) return null;
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static List<Long> getDatesBetween(Long startDate, Long endDate) {
        LocalDate localDateStart = longToLocalDateOrNow(startDate);
        LocalDate localDateEnd = longToLocalDateOrNow(endDate);
        long numOfDays = ChronoUnit.DAYS.between(localDateStart, localDateEnd);
        return Stream.iterate(localDateStart, date -> date.plusDays(1))
                .limit(numOfDays + 1)
                .map(t -> t.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .collect(Collectors.toList());
    }

    public static LocalDateTime longToLocalDateTimeOrNow(Long time) {
        if (time == null) time = getMilliseconds();
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime longToLocalDateTime(Long time) {
        if (time == null || time < 0) return null;
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static boolean isEndDayOfMonth(Long time) {
        return !getMonth(time).equals(getMonth(time + getOneDayMini()))
                || time >= getStartTimeOfDay();
    }

    public static boolean isStartDayOfMonth(Long time) {
        return !getMonth(time).equals(getMonth(time - getOneDayMini()));
    }

    public static Long convertMinuteToMillisecond(Integer minute) {
        return (long) minute * 60 * 1000;
    }

    public static Integer countNumDays(Long startTime, Long endTime) {
        final Set<Long> dayOfMonth = new HashSet<>();
        for (Long start = startTime; start < endTime; start += getOneDayMini()) {
            dayOfMonth.add(getStartTimeOfDayFromTime(start));
        }
        return dayOfMonth.size();
    }

    public static Set<Long> getDays(Long startTime, Long endTime) {
        final Set<Long> dayOfMonth = new HashSet<>();
        for (Long start = startTime; start < endTime; start += getOneDayMini()) {
            dayOfMonth.add(getStartTimeOfDayFromTime(start));
        }
        return dayOfMonth;
    }

    public static List<Long> getListDays(Long startTime, Long endTime) {
        final List<Long> listDays = new ArrayList<>();
        for (Long start = startTime; start < endTime; start += getOneDayMini()) {
            listDays.add(getStartTimeOfDayFromTime(start));
        }
        return listDays;
    }

    public static String getXdayBeforeToday(Integer day) {
        LocalDate dateBefore = LocalDate.now().minusDays(day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateBefore.format(formatter);
        return formattedDate;
    }

    public static Long getStartTimeOfDayBeforeXDay(Long time, Integer numDay) {
        return time - numDay * getOneDayMini();
    }

    public static int getIntHour(Long time) {
        Date date = new Date(time);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Long getMinute(Long startTime, Long endTime) {
        return (endTime - startTime) / (1000 * 60);
    }

    public static LocalTime convertToLocalTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime;
    }
}