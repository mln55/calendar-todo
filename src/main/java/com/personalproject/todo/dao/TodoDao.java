package com.personalproject.todo.dao;

import com.personalproject.todo.vo.Todo;

import java.util.List;

public interface TodoDao {

    List<Todo> todoByMemberThisMonth(String mId, String firstDate, String lastDate);

    List<Todo> todoByMemberSelectedDate(String mId, String selectedDate);

    int insertTodo(Todo todo);

    int modifyTodo(Todo todo);

    int deleteTodo(int no);

    int changeTodoColor(int no, String color);

    Todo membersTodo(String mId, int no);

    List<Todo> todoByMemberWithSearch(String mId, String firstDate, String lastDate, String include, String ignore, String target, String sort);
}
