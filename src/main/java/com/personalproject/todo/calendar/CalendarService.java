package com.personalproject.todo.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    private final int ROWS = 6; // 달력 표시 행
    private final int COLS = 7; // 달력 표시 열

    // 입력받은 날짜가 포함된 6x7 달력에 대한 정보를 담은 dto를 반환한다.
    public CalendarDto getCalendar(LocalDate inputDate) {
        int[][] days = new int[ROWS][COLS]; // 달력에 표시될 일 dayOfMonth
        int[][] status = new int[ROWS][COLS]; // -1: 이전 달 0:현재 달 1:다음 달
        String[][] onclick = new String[ROWS][COLS]; // 해당 날짜 링크
        String[][] date = new String[ROWS][COLS]; // 등록된 일정을 확인하기 위한 날짜 데이터 2020-09-03
        LocalDate sysDate = LocalDate.now(); // 시스템 날짜. year, month, day가 입력 되지 않았을 때 사용 / d-day 계산을 위해 사용 / TODAY 표시

        int currentYear = inputDate.getYear();
        int currentMonth = inputDate.getMonthValue();
        int currentDay = inputDate.getDayOfMonth();

        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int nextYear = currentMonth == 12 ? currentYear + 1 : currentYear;
        int nextMonth = currentMonth == 12 ? 1 : currentMonth + 1;

        LocalDate firstDate = inputDate.withDayOfMonth(1);
        LocalDate lastDate = inputDate.withDayOfMonth(inputDate.lengthOfMonth());

        int firstDayOfWeek = firstDate.getDayOfWeek().getValue();
        int currentLastDay = lastDate.getDayOfMonth();

        setDaysAndStatus(days, status, firstDayOfWeek, currentLastDay, prevYear, prevMonth);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (status[i][j] == -1) {
                    date[i][j] = intShortDateToFullDate(prevYear, prevMonth, days[i][j]);
                    onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);

                } else if (status[i][j] == 0) {
                    date[i][j] = intShortDateToFullDate(currentYear, currentMonth, days[i][j]);
                    if (days[i][j] != currentDay) {
                        onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);
                    }
                } else {
                    date[i][j] = intShortDateToFullDate(nextYear, nextMonth, days[i][j]);
                    onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);
                }
            }
        }

        return new CalendarDto(
            days,
            status,
            date,
            onclick,
            currentYear,
            currentMonth,
            currentDay,
            prevYear,
            prevMonth,
            nextYear,
            nextMonth,
            sysDate,
            firstDate,
            lastDate,
            inputDate
        );
    }

    // 년, 월 선택을 위한 달력 정보를 담은 dto를 반환한다.
    public SmallCalendarDto getSmallCalendar(LocalDate date) {

        int[][] days = new int[ROWS][COLS];
        int[][] status = new int[ROWS][COLS];
        int currentYear = date.getYear();
        int currentMonth = date.getMonthValue();
        int currentDay = date.getDayOfMonth();
        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int nextYear = currentMonth == 12 ? currentYear + 1 : currentYear;
        int nextMonth = currentMonth == 12 ? 1 : currentMonth + 1;

        int firstDayOfWeek = date.withDayOfMonth(1).getDayOfWeek().getValue();
        int currentLastDay = date.lengthOfMonth();

        setDaysAndStatus(days, status, firstDayOfWeek, currentLastDay, prevYear, prevMonth);

        return new SmallCalendarDto(
            days,
            status,
            currentYear,
            currentMonth,
            currentDay,
            prevYear,
            prevMonth,
            nextYear,
            nextMonth
        );
    }

    private String intShortDateToFullDate(int year, int month, int day) {
        return String.format("%d-%02d-%02d", year, month, day);
    }

    // 입력받은 date에 맞는 달력의 days, status 설정
    private void setDaysAndStatus(int[][] days, int[][] status, int firstDayOfWeek, int currentLastDay, int prevYear, int prevMonth) {

        int dayOfWeek = 0;
        int day;
        // 현재 달 1일이 일요일이 아니면 전달의 날짜를 설정
        if (firstDayOfWeek != DayOfWeek.SUNDAY.getValue()) {
            // 현재 달에 표시될 전 달 날짜의 최대 인덱스
            int prevLastDay = LocalDate.of(prevYear, prevMonth, 1).lengthOfMonth();
            while (dayOfWeek < firstDayOfWeek) {
                day = prevLastDay - firstDayOfWeek + dayOfWeek + 1;
                days[0][dayOfWeek] = day;
                status[0][dayOfWeek] = -1;
                dayOfWeek++;
            }
        }

        // 현재 달 1일부터 차례로 진행
        day = 1;
        boolean isNextMonth = false;
        for (int i = 0; i < ROWS; i++) {
            while (dayOfWeek < COLS) {
                if (isNextMonth) status[i][dayOfWeek] = 1;
                days[i][dayOfWeek] = day;
                day++;
                dayOfWeek++;
                if(day > currentLastDay) {
                    day = 1;
                    isNextMonth = true;
                }
            }
            dayOfWeek = 0;
        }
    }
}
