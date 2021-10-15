package com.personalproject.todo.service;

import com.personalproject.todo.vo.Todo;

import java.util.List;
import java.util.Map;

public interface TodoService {

    Map<String, Object> getTodoByMemberThisMonth(String mId, String firstDate, String lastDate, String currentDate);

    List<Todo> getTodoByMemberSelectedDate(String mId, String selectedDate);

    int insertTodo(Todo todo);

    int modifyTodo(Todo todo);

    int deleteTodo(int no);

    int changeTodoColor(int no, String color);

    Todo getMembersTodo(String mId, int no);

    List<Todo> getTodoByMemberWithSearch(String mId, String firstDate, String lastDate, String include, String ignore, String target, String sort);
}
