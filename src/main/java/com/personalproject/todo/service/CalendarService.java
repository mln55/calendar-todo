package com.personalproject.todo.service;

import java.util.Map;

public interface CalendarService {

    Map<String, Object> getCalendar(String date);

    Map<String, Object> getSmallCalendar(String date);

}
