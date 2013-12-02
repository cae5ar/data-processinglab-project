package com.pstu.dtl.shared.dto;

import java.util.Date;


@SuppressWarnings("serial")
public class DateRange implements IDto {

    private Date leftDate;
    private Date rightDate;

    public DateRange() {
        this(new Date(), new Date());
    }

    public DateRange(Date leftDate, Date rightDate) {
        this.leftDate = leftDate;
        this.rightDate = rightDate;
    }

    public void setLeftDate(Date leftDate) {
        this.leftDate = leftDate;
    }

    public Date getLeftDate() {
        return leftDate;
    }

    public void setRightDate(Date rightDate) {
        this.rightDate = rightDate;
    }

    public Date getRightDate() {
        return rightDate;
    }

    @Override
    public String toString() {
        return "[" + leftDate + " .. " + rightDate + "]";
    }

    /** Возвращает временной промежуток, покрывающий оба заданных */
    public static DateRange cover(DateRange dr1, DateRange dr2) {
        if (dr1 == null && dr2 == null) return null;
        if (dr1 == null) dr1 = new DateRange(null, null);
        if (dr2 == null) dr2 = new DateRange(null, null);

        return new DateRange(min(dr1.getLeftDate(), dr2.getLeftDate()), max(dr1.getRightDate(), dr2.getRightDate()));
    }

    public static DateRange cover(DateRange dr1, Date dr2) {
        return cover(dr1, new DateRange(dr2, dr2));
    }

    @SuppressWarnings("deprecation")
    static public DateRange monthDateRange(Date date) {
        date = (Date) date.clone();

        DateRange dr = new DateRange();
        date.setDate(1);
        dr.setLeftDate((Date) date.clone());

        date.setMonth(date.getMonth() + 1);
        dr.setRightDate(date);
        return dr;
    }

    @SuppressWarnings("deprecation")
    static public DateRange halthYearDateRange() {
        Date from = new Date();
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        from.setDate(1);
        if (from.getMonth() > 5)
            from.setMonth(5);
        else
            from.setMonth(-1);
        Date to = (Date) from.clone();
        to.setMonth(from.getMonth() + 7);
        return new DateRange(from, to);
    }

    static private int compare(Date date1, Date date2) {
        if (date1 == null && date2 == null) return 0;
        if (date1 == null) return -1;
        if (date2 == null) return 1;
        return date1.compareTo(date2);
    }

    static private Date min(Date date1, Date date2) {
        return compare(date1, date2) <= 0 ? date1 : date2;
    }

    static private Date max(Date date1, Date date2) {
        return compare(date1, date2) >= 0 ? date1 : date2;
    }

}
