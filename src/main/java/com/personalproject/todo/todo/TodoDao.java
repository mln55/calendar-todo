package com.personalproject.todo.todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoDao {

    public List<Todo> todoByMemberThisMonth(String mId, LocalDate firstDate, LocalDate lastDate);

    public List<Todo> todoByMemberWithSearch(String mId, LocalDate firstDate, LocalDate lastDate, String include, String ignore, String target, String sort);

    public List<Todo> todoByMemberSelectedDate(String mId, LocalDate selectedDate);

    public int insertTodo(Todo todo);

    public int modifyTodo(Todo todo);

    public int deleteTodo(Integer no);

    public int changeTodoColor(Integer no, String color);

    public Todo membersTodo(Integer no, String mId);
}
