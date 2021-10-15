package com.personalproject.todo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.personalproject.todo.dao.TodoDao;
import com.personalproject.todo.vo.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoDao todoDao;

    @Override
    public Map<String, Object> getTodoByMemberThisMonth(String mId, String firstDate, String lastDate,
                                                        String currentDate) {
        Map<String, Object> todoMap = new HashMap<>();
        List<Todo> todoList = todoDao.todoByMemberThisMonth(mId, firstDate, lastDate); // beginDate 오름차순 dueDate 내림차순 정렬됨
        List<Todo> todoList4Brief = new ArrayList<>();
        Map<String, Integer> todoExtraMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        boolean hasTodoList = false;
        try {
            Set<Integer> levelSet = new HashSet<>();
            long sysTime = sdf.parse(sdf.format(System.currentTimeMillis())).getTime();
            long currTodoBegin;
            long currTodoDue;
            long prevTodoBegin;
            long prevTodoDue;

            long currentDateTime = sdf.parse(currentDate).getTime();
            // todoList를 돌며 각 일자마다 화면에 표시될 상위 4개 Todo의 barLevel을 설정한다.
            // 현재 todo까지의 todo들의 barLevel을 확인하여 겹치는 일정들의 level을 set에 저장한다.
            // 1. set 크키가 4라면 현재 todo는 화면에 표시되지 않으므로 표시되지 않는 일정 개수에 추가한다.
            // 2. set 크기가 4가 아니라면 현재 todo는 화면에 표시되므로 현재 todo의 barLevel을 설정한다.
            for (int i = 0; i < todoList.size(); i++) {
                Todo todo = todoList.get(i);
                currTodoBegin = sdf.parse(todo.getBeginDate()).getTime();
                currTodoDue = sdf.parse(todo.getDueDate()).getTime();

                if (!hasTodoList && currTodoBegin <= currentDateTime && currTodoDue >= currentDateTime) {
                    hasTodoList = true;
                }

                for (int j = 0; j < i; j++) {
                    Todo prevTodo = todoList.get(j);
                    prevTodoBegin = sdf.parse(prevTodo.getBeginDate()).getTime();
                    prevTodoDue = sdf.parse(prevTodo.getDueDate()).getTime();
                    if (currTodoBegin >= prevTodoBegin && currTodoBegin <= prevTodoDue && prevTodo.getBarLevel() != 0) {
                        levelSet.add(prevTodo.getBarLevel());
                    }
                }
                if (levelSet.size() == 4) {
                    long diff = currTodoDue - currTodoBegin;
                    for (int d = 0; d <= diff; d += 86400000) { // 60*60*24*1000
                        String targetDate = sdf.format(currTodoBegin + d);
                        todoExtraMap.compute(targetDate, (k, v) -> (v == null ? 1 : v + 1));
                    }
                } else {
                    for (int level = 1; level <= 4; level++) {
                        if (!levelSet.contains(level)) {
                            todo.setBarLevel(level);
                            todoList4Brief.add(todo);
                            break;
                        }
                    }
                }
                levelSet.clear();

                // -1 : 종료됨, 0 : D-DAY, 나머지 : D-N
                todo.setDDay(sysTime > currTodoDue ? -1 : sysTime == currTodoDue ? 0 : (int) (currTodoDue - sysTime)/86400000);
            }
        } catch (ParseException e) {

        }

        todoMap.put("hasTodoList", hasTodoList);
        todoMap.put("todoList", todoList);
        todoMap.put("todoExtraMap", todoExtraMap);
        todoMap.put("todoList4Brief", todoList4Brief);
        return todoMap;
    }

    @Override
    public List<Todo> getTodoByMemberSelectedDate(String mId, String selectedDate) {

        // day를 입력받지 못 한 경우에 00일로 설정 된다.
        if(selectedDate.startsWith("00", selectedDate.length() - 2)) return null;

        List<Todo> targetTodoList = todoDao.todoByMemberSelectedDate(mId, selectedDate);

        setTodoDDay(targetTodoList);
        return targetTodoList;
    }

    @Override
    public int insertTodo(Todo todo) {
        int result = todoDao.insertTodo(todo);

        if(result == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int modifyTodo(Todo todo) {
        int result = todoDao.modifyTodo(todo);

        if(result == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int deleteTodo(int no) {
        int result = todoDao.deleteTodo(no);

        if(result == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int changeTodoColor(int no, String color) {
        return todoDao.changeTodoColor(no, color);
    }

    @Override
    public Todo getMembersTodo(String mId, int no) {
        Todo todo = todoDao.membersTodo(mId, no);

        setTodoDDay(todo);
        return todo;
    }

    @Override
    public List<Todo> getTodoByMemberWithSearch(String mId, String firstDate, String lastDate, String include, String ignore, String target, String sort) {

        List<Todo> todoSearchList = todoDao.todoByMemberWithSearch(mId, firstDate, lastDate, include, ignore, target, sort);
        setTodoDDay(todoSearchList);

        // 비어있을 때
        if(todoSearchList.size() == 0) {
            return todoSearchList;
        }

        // 타깃이 none(검색)이거나 dday일 경우, dday가 -1인 것을 뒤로 보낸다.
        if(target.equals("none") || target.equals("dday")) {
            Iterator<Todo> it = todoSearchList.iterator();
            List<Todo> underDday = new ArrayList<Todo>();
            List<Todo> overDday= new ArrayList<Todo>();

            while(it.hasNext()) {
                Todo todo = it.next();
                if(todo.getDDay() == -1) {
                    overDday.add(todo);
                } else {
                    underDday.add(todo);
                }
            }
            underDday.addAll(overDday);

            return underDday;
        }

        // 해당하지 않을 경우 그냥 return
        return todoSearchList;
    }

    public void setTodoDDay(Object todoObject) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long sysDate;
        long dueDate;
        int dDay;
        List<Todo> todoList;
        Todo todo;
        try {
            sysDate = sdf.parse(sdf.format(System.currentTimeMillis())).getTime();

            if (todoObject instanceof List<?>) {
                todoList = (List<Todo>) todoObject;
                for(Todo t : todoList) {
                    dueDate = sdf.parse(t.getDueDate()).getTime();
                    dDay = sysDate > dueDate ? -1 : (int) (dueDate / (1000*60*60*24) - sysDate / (1000*60*60*24));
                    t.setDDay(dDay);
                }
            } else if (todoObject instanceof Todo) {
                todo = (Todo) todoObject;
                sysDate = sdf.parse(sdf.format(System.currentTimeMillis())).getTime();
                dueDate = sdf.parse(todo.getDueDate()).getTime();
                dDay = sysDate > dueDate ? -1 : (int) (dueDate / (1000*60*60*24) - sysDate / (1000*60*60*24));
                todo.setDDay(dDay);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}