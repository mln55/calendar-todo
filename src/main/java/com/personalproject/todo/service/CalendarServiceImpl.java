package com.personalproject.todo.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final int ROWS = 6; // 달력 표시 행
    private final int COLS = 7; // 달력 표시 열

    public Map<String, Object> getCalendar(String inputDate) {
        Calendar cal = Calendar.getInstance(new Locale("korean", "korea"));

        int[][] days = new int[ROWS][COLS]; // 달력에 표시될 일
        int[][] status = new int[ROWS][COLS]; // -1: 이전 달 0:현재 달 1:다음 달
        String[][] onclick = new String[ROWS][COLS]; // 해당 날짜 링크
        String[][] date = new String[ROWS][COLS]; // 등록된 일정을 확인하기 위한 날짜 데이터 2020-9-3
        String[][] fullDate = new String[ROWS][COLS]; // 등록된 일정을 확인하기 위한 날짜 데이터 2020-09-03
        String sysDate; // 시스템 시간. year, month, day가 입력 되지 않았을 때 사용 / d-day 계산을 위해 사용 / TODAY 표시

        sysDate = new SimpleDateFormat("yyyy-M-d").format(System.currentTimeMillis());
        String[] dateSplit = inputDate.split("-");
        int currentYear = Integer.parseInt(dateSplit[0]);
        int currentMonth = Integer.parseInt(dateSplit[1]);
        int currentDay = Integer.parseInt(dateSplit[2]);

        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int nextYear = currentMonth == 12 ? currentYear + 1 : currentYear;
        int nextMonth = currentMonth == 12 ? 1 : currentMonth + 1;

        cal.set(currentYear, currentMonth - 1, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int currentLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        setDaysAndStatus(days, status, firstDayOfWeek, currentLastDay, prevYear, prevMonth);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (status[i][j] == -1) {
                    date[i][j] = prevYear + "-" + prevMonth + "-" + days[i][j];
                    onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);
                    fullDate[i][j] = intShortDateToFullDate(prevYear, prevMonth, days[i][j]);

                } else if (status[i][j] == 0) {
                    date[i][j] = currentYear + "-" + currentMonth + "-" + days[i][j];
                    fullDate[i][j] = intShortDateToFullDate(currentYear, currentMonth, days[i][j]);
                    if (days[i][j] != currentDay) {
                        onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);
                    }
                } else {
                    date[i][j] = nextYear + "-" + nextMonth + "-" + days[i][j];
                    onclick[i][j] = String.format("location.href='/calendar/cal?date=%s';", date[i][j]);
                    fullDate[i][j] = intShortDateToFullDate(nextYear, nextMonth, days[i][j]);
                }
            }
        }

        String firstDate = date[0][0];
        String lastDate = date[ROWS - 1][COLS - 1];


        Map<String, Object> calendarMap = new HashMap<>();
        calendarMap.put("days", days);
        calendarMap.put("status", status);
        calendarMap.put("date", date);
        calendarMap.put("fullDate", fullDate);
        calendarMap.put("onclick", onclick);
        calendarMap.put("sysDate", sysDate);
        calendarMap.put("currentYear", currentYear);
        calendarMap.put("currentMonth", currentMonth);
        calendarMap.put("currentDay", currentDay);
        calendarMap.put("prevYear", prevYear);
        calendarMap.put("prevMonth", prevMonth);
        calendarMap.put("nextYear", nextYear);
        calendarMap.put("nextMonth", nextMonth);
        calendarMap.put("firstDate", firstDate);
        calendarMap.put("lastDate", lastDate);
        calendarMap.put("selectedDate", inputDate);

        return calendarMap;
    }

    /*
        년.월 눌렀을 때 getSmallCalendar로 시스템 날짜에 맞는 달력을 표시 한다.
    */
    @Override
    public Map<String, Object> getSmallCalendar(String date) {

        int[][] days = new int[ROWS][COLS];
        int[][] status = new int[ROWS][COLS];
        String[] dateSplit = date.split("-");
        int currentYear = Integer.parseInt(dateSplit[0]);
        int currentMonth = Integer.parseInt(dateSplit[1]);
        int currentDay = Integer.parseInt(dateSplit[2]);
        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int nextYear = currentMonth == 12 ? currentYear + 1 : currentYear;
        int nextMonth = currentMonth == 12 ? 1 : currentMonth + 1;

        Calendar cal = Calendar.getInstance(new Locale("korean", "korea"));
        cal.set(Calendar.YEAR, currentYear);
        cal.set(Calendar.MONTH, currentMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        currentDay = currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH) ?
                cal.getActualMaximum(Calendar.DAY_OF_MONTH) : Math.max(1, currentDay);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int currentLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        setDaysAndStatus(days, status, firstDayOfWeek, currentLastDay, prevYear, prevMonth);

        Map<String, Object> calendarMap = new HashMap<>();
        calendarMap.put("days", days);
        calendarMap.put("status", status);
        calendarMap.put("year", currentYear);
        calendarMap.put("month", currentMonth);
        calendarMap.put("day", currentDay);
        calendarMap.put("prevYear", prevYear);
        calendarMap.put("prevMonth", prevMonth);
        calendarMap.put("nextYear", nextYear);
        calendarMap.put("nextMonth", nextMonth);

        return calendarMap;
    }

    private String intShortDateToFullDate(int year, int month, int day) {
        return String.format("%d-%02d-%02d", year, month, day);
    }

    // 입력받은 date에 맞는 달력의 days, status 설정
    private void setDaysAndStatus(int[][] days, int[][] status, int firstDayOfWeek, int currentLastDay, int prevYear, int prevMonth) {

        int dayOfWeek = 1;
        int tmpDay;
        // 현재 달 1일이 일요일이 아니면 전달의 날짜를 설정
        if (firstDayOfWeek != 1) {
            // 현재 달에 표시될 전 달 날짜의 최대 인덱스
            Calendar prevCal = Calendar.getInstance();
            prevCal.set(prevYear, prevMonth - 1, 1);
            int prevLastDay = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (dayOfWeek = 1; dayOfWeek < firstDayOfWeek; dayOfWeek++) {
                tmpDay = prevLastDay - firstDayOfWeek + dayOfWeek + 1;
                days[0][dayOfWeek - 1] = tmpDay;
                status[0][dayOfWeek - 1] = -1;
            }
        }

        // 현재 달 1일부터 차례로 진행
        tmpDay = 1;
        boolean isNextMonth = false;
        for (int i = 0; i < ROWS; i++) {
            while (dayOfWeek <= COLS) {
                if (isNextMonth) status[i][dayOfWeek - 1] = 1;
                days[i][dayOfWeek - 1] = tmpDay;
                tmpDay++;
                dayOfWeek++;
                if(tmpDay > currentLastDay) {
                    tmpDay = 1;
                    isNextMonth = true;
                }
            }
            dayOfWeek = 1;
        }
    }

}