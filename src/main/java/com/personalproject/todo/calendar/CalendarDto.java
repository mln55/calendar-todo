package com.personalproject.todo.calendar;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDto {

    private int[][] days;
    private int[][] status;
    private String[][] date;
    private String[][] onclick;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int prevYear;
    private int prevMonth;
    private int nextYear;
    private int nextMonth;
    private LocalDate sysDate;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private LocalDate inputDate;

    public CalendarDto() { }

    public CalendarDto(int[][] days, int[][] status, String[][] date, String[][] onclick, int currentYear,
            int currentMonth, int currentDay, int prevYear, int prevMonth, int nextYear, int nextMonth,
            LocalDate sysDate, LocalDate firstDate, LocalDate lastDate, LocalDate inputDate) {
        this.days = days;
        this.status = status;
        this.date = date;
        this.onclick = onclick;
        this.currentYear = currentYear;
        this.currentMonth = currentMonth;
        this.currentDay = currentDay;
        this.prevYear = prevYear;
        this.prevMonth = prevMonth;
        this.nextYear = nextYear;
        this.nextMonth = nextMonth;
        this.sysDate = sysDate;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        this.inputDate = inputDate;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .toString();
    }
}
