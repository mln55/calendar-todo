package com.personalproject.todo.controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.personalproject.todo.service.CalendarService;
import com.personalproject.todo.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    CalendarService calendarService;

    @Autowired
    TodoService todoService;

    @GetMapping({"/cal"})
    public String calendar(
            @RequestParam(required = false) String date,
            Authentication authentication,
            Model model) {

        // 비정상 데이터 처리
        if (date == null || date.trim().equals("")) {
            date = new SimpleDateFormat("yyyy-M-d").format(System.currentTimeMillis());
            return String.format("redirect:/calendar/cal?date=%s", date);
        } else {
            try {
                String[] dateSplit = date.split("-");
                int year = Integer.parseInt(dateSplit[0]);
                int month = Integer.parseInt(dateSplit[1]);
                int day = Integer.parseInt(dateSplit[2]);

                if (year < 1970 || year > 2100 || month < 1 || month > 12 || day < 1 || day > 31) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }


        String id = authentication.getName();
        Map<String, Object> calMap = calendarService.getCalendar(date);

        String firstDate = (String) calMap.get("firstDate");
        String lastDate = (String) calMap.get("lastDate");
        Map<String, Object> todoMap = todoService.getTodoByMemberThisMonth(id, firstDate, lastDate, date);

        model.addAllAttributes(calMap);
        model.addAllAttributes(todoMap);
        model.addAttribute("todoSearchList", todoService.getTodoByMemberWithSearch(id, firstDate, lastDate, "none", "on", "none", "asc"));

        return "calendar";
    }

    // 날짜 선택 용 캘린더
    @RequestMapping({"/scal"})
    @ResponseBody
    public Object smallCalendar(
            @RequestParam String date,
            Model model) {

        return calendarService.getSmallCalendar(date);
    }

}
