package com.personalproject.todo.calendar;

import static com.personalproject.todo.utils.DateTimeUtils.stringDateToLocalDate;
import static com.personalproject.todo.utils.ObjectUtils.objectToMap;

import java.time.LocalDate;
import java.util.Map;

import com.personalproject.todo.todo.TodoService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("calendar")
public class CalendarViewController {

    private final CalendarService calendarService;
    private final TodoService todoService;

    @Autowired
    public CalendarViewController(CalendarService calendarService, TodoService todoService) {
        this.calendarService = calendarService;
        this.todoService = todoService;
    }

    // 일정 정보를 보여줄 달력 페이지 리턴
    @GetMapping("cal")
    public String calendar(
        String date,
        Authentication authentication,
        Model model
    ) {

        if (StringUtils.isBlank(date)) return "redirect:/calendar/cal?date=" + LocalDate.now().toString();
        LocalDate localDate = stringDateToLocalDate(date);
        String id = authentication.getName();
        CalendarDto calendarDto = calendarService.getCalendar(localDate);

        LocalDate firstDate = calendarDto.getFirstDate();
        LocalDate lastDate = calendarDto.getLastDate();
        Map<String, Object> todoMap = todoService.getTodoByMemberThisMonth(id, firstDate, lastDate, localDate);

        model.addAllAttributes(objectToMap(CalendarDto.class, calendarDto));
        model.addAllAttributes(todoMap);
        model.addAttribute("todoSearchList", todoService.getTodoByMemberWithSearch(id, firstDate, lastDate, "none", "on", "none", "asc"));
        return "calendar";
    }
}
