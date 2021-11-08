package com.personalproject.todo.calendar;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmallCalendarDto {

    private int[][] days;
    private int[][] status;
    private int year;
    private int month;
    private int day;
    private int prevYear;
    private int prevMonth;
    private int nextYear;
    private int nextMonth;

    public SmallCalendarDto() { }

    public SmallCalendarDto(int[][] days, int[][] status, int year, int month, int day,
            int prevYear, int prevMonth, int nextYear, int nextMonth) {
        this.days = days;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.prevYear = prevYear;
        this.prevMonth = prevMonth;
        this.nextYear = nextYear;
        this.nextMonth = nextMonth;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }
}
