package com.personalproject.todo.calendar;

import static com.personalproject.todo.utils.ObjectUtils.objectToMap;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("calendar")
public class CalendarRestController {

    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // 날짜 선택 용 달력 정보를 리턴한다.
    @RequestMapping(path = "scal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> smallCalendar(LocalDate date) {
        return objectToMap(SmallCalendarDto.class, calendarService.getSmallCalendar(date));
    }
}
