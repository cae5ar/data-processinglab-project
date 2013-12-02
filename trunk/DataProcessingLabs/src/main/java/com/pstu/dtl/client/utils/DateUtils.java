package com.pstu.dtl.client.utils;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.pstu.dtl.client.Site;

public class DateUtils {

    public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    public static String[] russianMonths;

    static {
        initializeRussianMonths();
    }

    @SuppressWarnings("deprecation")
    public static String formatCalendarDate(Date date) {
        DateTimeFormat formatter = DateTimeFormat.getFormat(" yyyy г.");
        String result = russianMonths[date.getMonth()] + formatter.format(date);
        return result;
    }

    private static void initializeRussianMonths() {
        russianMonths = new String[] {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
                "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    }

    /**
     * Форматирует дату с учетом текущего дня(заменяет дату на Сегодня, Вчера)
     * @param d - дата
     * @return возращает строка типа "Сегодня в 15:00:00", "21.08.2013 в 16:07:54"
     */
    @SuppressWarnings("deprecation")
    public static String formatNextFewDateTime(Date d) {
        StringBuilder s = new StringBuilder();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
        DateTimeFormat timeFormat = DateTimeFormat.getFormat("' в ' HH:mm:ss");
        Date now = new Date();
        if (d.getDate() == now.getDate()) {
            s.append(Site.messages.msgToday());
        }
        else {
            now.setDate(now.getDate() - 1);
            if (now.getDate() == d.getDate()) {
                s.append(Site.messages.msgYesterday());
            }
            else {
                s.append(dateFormat.format(d));
            }
        }
        s.append(timeFormat.format(d));
        return s.toString();
    }

    /**
     * Provides a <code>null</code>-safe way to return the number of
     * milliseconds on a <code>date</code>.
     * 
     * @param date The date whose value in milliseconds will be returned
     * @return The number of milliseconds in <code>date</code>, <code>0</code>
     *         (zero) if <code>date</code> is <code>null</code>.
     */
    private static long safeInMillis(Date date) {
        return date != null ? date.getTime() : 0;
    }

    /**
     * Returns the number of days between the passed dates.
     * 
     * @param endDate The upper limit of the date range
     * @param startDate The lower limit of the date range
     * @return The number of days between <code>endDate</code> and
     *         <code>starDate</code> (inclusive)
     */
    @SuppressWarnings(value = "deprecation")
    public static int differenceInDays(Date endDate, Date startDate) {
        int difference = 0;
        if (!areOnTheSameDay(endDate, startDate)) {
            int endDateOffset = -(endDate.getTimezoneOffset() * 60 * 1000);
            long endDateInstant = endDate.getTime() + endDateOffset;
            int startDateOffset = -(startDate.getTimezoneOffset() * 60 * 1000);
            long startDateInstant = startDate.getTime() + startDateOffset;
            double differenceDouble = (double) Math.abs(endDateInstant - startDateInstant) / (double) MILLIS_IN_A_DAY;
            differenceDouble = Math.max(1.0D, differenceDouble);
            difference = (int) differenceDouble;
        }
        return difference;
    }

    /**
     * Moves a date <code>shift</code> days. A clone of <code>date</code> to
     * prevent undesired object modifications.
     * 
     * @param date The date to shift
     * @param shift The number of days to push the original <code>date</code>
     *            <em>forward</em>
     * @return A <em>new</em> date pushed <code>shift</code> days forward
     */
    @SuppressWarnings("deprecation")
    public static Date shiftDate(Date date, int shift) {
        Date result = (Date) date.clone();
        result.setDate(date.getDate() + shift);
        return result;
    }

    @SuppressWarnings("deprecation")
    public static Date shiftMinutes(Date date, int minutes) {
        Date result = (Date) date.clone();
        result.setMinutes(date.getMinutes() + minutes);
        return result;
    }

    public static Date shiftDateByTime(Date date, int shift) {
        Date result = (Date) date.clone();
        long time = result.getTime();
        time += shift * 24L * 60L * 60L * 1000L;
        result.setTime(time);
        return result;
    }

    /**
     * Resets the date to have no time modifiers (hours, minutes, seconds.)
     * 
     * @param date The date to reset
     */
    @SuppressWarnings("deprecation")
    public static void resetTime(Date date) {
        long milliseconds = safeInMillis(date);
        milliseconds = (milliseconds / 1000) * 1000;
        date.setTime(milliseconds);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
    }

    /**
     * Indicates whether two dates are on the same date by comparing their day,
     * month and year values. Time values such as hours and minutes are not
     * considered in this comparison.
     * 
     * @param dateOne The first date to test
     * @param dateTwo The second date to test
     * @return <code>true</code> if both dates have their <code>date</code>,
     *         <code>month</code> and <code>year</code> properties with the
     *         <em>exact</em> same values (whatever they are)
     */
    @SuppressWarnings("deprecation")
    public static boolean areOnTheSameDay(Date dateOne, Date dateTwo) {
        return dateOne.getDate() == dateTwo.getDate() && dateOne.getMonth() == dateTwo.getMonth()
                && dateOne.getYear() == dateTwo.getYear();
    }

    /**
     * Returns a clone of the <code>anyDayInMonth</code> date set to the
     * <em>first</em> day of whatever its month is.
     * 
     * @param anyDayInMonth Any date on a month+year
     * @return A clone of the <code>anyDayInMonth</code> date, representing the
     *         first day of that same month and year
     */
    @SuppressWarnings("deprecation")
    public static Date firstOfTheMonth(Date anyDayInMonth) {
        Date first = (Date) anyDayInMonth.clone();
        first.setDate(1);
        return first;
    }

    @SuppressWarnings("deprecation")
    public static Date firstOfTheWeek(Date anyDayInWeek) {
        Date first = (Date) anyDayInWeek.clone();
        int day = (first.getDay() + 6) % 7;
        first.setDate(first.getDate() - day);
        return first;
    }

    /**
     * Moves the date of the passed object to be one day after whatever date it
     * has.
     * 
     * @param date An object representing a date
     * @return The day
     */
    @SuppressWarnings("deprecation")
    public static Date moveOneDayForward(Date date) {
        date.setDate(date.getDate() + 1);
        return date;
    }

    /**
     * Returns the date corresponding to the first day of the next month
     * relative to the passed <code>date</code>.
     * 
     * @param date The reference date
     * @return The first day of the next month, if the month of the passed date
     *         corresponds to december (<code>11</code>) <em>one</em> will be
     *         added to the year of the returned date.
     */
    @SuppressWarnings("deprecation")
    public static Date firstOfNextMonth(Date date) {
        Date firstOfNextMonth = null;
        if (date != null) {
            int year = date.getMonth() == 11 ? date.getYear() + 1 : date.getYear();
            firstOfNextMonth = new Date(year, date.getMonth() + 1 % 11, 1);
        }
        return firstOfNextMonth;
    }

    /**
     * Returns a day <em>exactly</em> 24 hours before the instant passed as
     * <code>date</code>. // TODO: This logic should address the time zone
     * offset
     * 
     * @param date A point in time from which the moment 24 hours before will be
     *            calculated
     * @return A new object <em>24</em> hours prior to the passed
     *         <code>date</code>
     */
    public static Date previousDay(Date date) {
        return new Date(date.getTime() - MILLIS_IN_A_DAY);
    }

    /**
     * precision = 1 - YEAR<br>
     * precision = 2 - MONTH<br>
     * precision = 3 - DAY<br>
     */
    @SuppressWarnings("deprecation")
    public static int compareDate(Date d1, Date d2, int precision) {
        int[] arr1 = new int[3];
        arr1[0] = d1.getYear();
        arr1[1] = d1.getMonth();
        arr1[2] = d1.getDate();

        int[] arr2 = new int[3];
        arr2[0] = d2.getYear();
        arr2[1] = d2.getMonth();
        arr2[2] = d2.getDate();

        int res = 0;
        for (int i = 0; i < precision && i < 3; i++)
            if (res == 0) res = arr1[i] - arr2[i];

        return res;
    }

}
