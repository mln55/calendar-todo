package com.personalproject.todo.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoDao todoDao;

    public TodoService(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    public Map<String, Object> getTodoByMemberThisMonth(String mId, LocalDate firstDate, LocalDate lastDate, LocalDate currentDate) {
        Map<String, Object> todoMap = new HashMap<>();
        List<Todo> todoList = todoDao.todoByMemberThisMonth(mId, firstDate, lastDate);
        List<TodoDto> todoDtoList = new ArrayList<>();
        for (Todo todo : todoList) todoDtoList.add(new TodoDto(todo));

        List<TodoDto> todoList4Brief = new ArrayList<>();
        Map<String, Integer> todoExtraMap = new HashMap<>();
        boolean hasTodoList = false;
        Set<Integer> levelSet = new HashSet<>();
        LocalDateTime currTodoBegin;
        LocalDateTime currTodoDue;
        LocalDateTime prevTodoBegin;
        LocalDateTime prevTodoDue;

        LocalDateTime currentDateTime = currentDate.atStartOfDay();
        // todoList를 돌며 각 일자마다 화면에 표시될 상위 4개 Todo의 barLevel을 설정한다.
        // 현재 todo까지의 todo들의 barLevel을 확인하여 겹치는 일정들의 level을 set에 저장한다.
        // 1. set 크키가 4라면 현재 todo는 화면에 표시되지 않으므로 표시되지 않는 일정 개수에 추가한다.
        // 2. set 크기가 4가 아니라면 현재 todo는 화면에 표시되므로 현재 todo의 barLevel을 설정한다.
        for (int i = 0; i < todoDtoList.size(); i++) {
            TodoDto todoDto = todoDtoList.get(i);
            currTodoBegin = LocalDate.from(todoDto.getBeginDate()).atTime(todoDto.getBeginTime());
            currTodoDue = LocalDate.from(todoDto.getDueDate()).atTime(todoDto.getDueTime());

            if (!hasTodoList && currTodoBegin.isBefore(currentDateTime) && currTodoDue.isAfter(currentDateTime)) {
                hasTodoList = true;
            }

            for (int j = 0; j < i; j++) {
                TodoDto prevTodo = todoDtoList.get(j);
                prevTodoBegin = LocalDate.from(prevTodo.getBeginDate()).atTime(prevTodo.getBeginTime());
                prevTodoDue = LocalDate.from(prevTodo.getDueDate()).atTime(prevTodo.getDueTime());
                if (
                    ChronoUnit.SECONDS.between(currTodoBegin, prevTodoBegin) <= 0 // currentTodoBegin이 늦다.
                    && ChronoUnit.SECONDS.between(currTodoBegin, prevTodoDue) >= 0 // currentTodoBegin이 빠르다.
                    && prevTodo.getBarLevel() != 0
                ) {
                    levelSet.add(prevTodo.getBarLevel());
                }
            }
            if (levelSet.size() == 4) {
                int diff = (int) ChronoUnit.DAYS.between(currTodoDue, currTodoBegin);
                for (int d = 0; d <= diff; d++) {
                    LocalDateTime targetDate = currTodoBegin.plusDays(d);
                    todoExtraMap.compute(targetDate.toLocalDate().toString(), (k, v) -> (v == null ? 1 : v + 1));
                }
            } else {
                for (int level = 1; level <= 4; level++) {
                    if (!levelSet.contains(level)) {
                        todoDto.setBarLevel(level);
                        todoList4Brief.add(todoDto);
                        break;
                    }
                }
            }
            levelSet.clear();

            // -1 : 종료됨, 0 : D-DAY, 나머지 : D-N
            setTodoDDay(todoDto);
        }

        todoMap.put("hasTodoList", hasTodoList);
        todoMap.put("todoList", todoDtoList);
        todoMap.put("todoExtraMap", todoExtraMap);
        todoMap.put("todoList4Brief", todoList4Brief);
        return todoMap;
    }

    public List<TodoDto> getTodoByMemberSelectedDate(String mId, LocalDate selectedDate) {

        List<Todo> todoList = todoDao.todoByMemberSelectedDate(mId, selectedDate);
        List<TodoDto> todoDtoList = new ArrayList<>();
        for (Todo todo : todoList) todoDtoList.add(new TodoDto(todo));
        setTodoDDay(todoDtoList);
        return todoDtoList;
    }

    public List<TodoDto> getTodoByMemberWithSearch(String mId, LocalDate firstDate, LocalDate lastDate, String include, String ignore, String target, String sort) {

        List<Todo> todoSearchList = todoDao.todoByMemberWithSearch(mId, firstDate, lastDate, include, ignore, target, sort);
        List<TodoDto> todoDtoSearchList = new ArrayList<>();
        for (Todo todo : todoSearchList) todoDtoSearchList.add(new TodoDto(todo));
        setTodoDDay(todoDtoSearchList);

        // 비어있을 때
        if(todoDtoSearchList.size() == 0) {
            return todoDtoSearchList;
        }

        // 타깃이 none(검색)이거나 dday일 경우, dday가 -1인 것을 뒤로 보낸다.
        if(target.equals("none") || target.equals("dday")) {
            List<TodoDto> underDday = new ArrayList<>();
            List<TodoDto> overDday= new ArrayList<>();

            for (TodoDto todoDto : todoDtoSearchList) {
                if(todoDto.getDDay() == -1) {
                    overDday.add(todoDto);
                } else {
                    underDday.add(todoDto);
                }
            }
            underDday.addAll(overDday);
            return underDday;
        }

        // 해당하지 않을 경우 그냥 return
        return todoDtoSearchList;
    }

    public int insertTodo(TodoDto todoDto) {
        Todo todo = new Todo.Builder()
            .mId(todoDto.getMId())
            .title(todoDto.getTitle())
            .content(todoDto.getContent())
            .beginDate(todoDto.getBeginDate().atTime(todoDto.getBeginTime()))
            .dueDate(todoDto.getDueDate().atTime(todoDto.getDueTime()))
            .color(todoDto.getColor().replace("numSign", "#"))
            .build();
        return todoDao.insertTodo(todo);
    }

    public int modifyTodo(TodoDto todoDto) {
        Todo todo = new Todo.Builder()
            .no(todoDto.getNo())
            .mId(todoDto.getMId())
            .title(todoDto.getTitle())
            .content(todoDto.getContent())
            .beginDate(todoDto.getBeginDate().atTime(todoDto.getBeginTime()))
            .dueDate(todoDto.getDueDate().atTime(todoDto.getDueTime()))
            .color(todoDto.getColor().replace("numSign", "#"))
            .build();
        return todoDao.modifyTodo(todo);
    }

    public int deleteTodo(Integer no) {
        return todoDao.deleteTodo(no);
    }

    public int changeTodoColor(int no, String color) {
        return todoDao.changeTodoColor(no, color);
    }

    public TodoDto membersTodo(String mId, Integer no) {
        Todo todo = todoDao.membersTodo(no, mId);
        TodoDto todoDto = new TodoDto(todo);
        setTodoDDay(todoDto);
        return todoDto;
    }

    private void setTodoDDay(Object todoDtoObject) {
        LocalDateTime sysDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime dueDate;
        int dDay;
        if (todoDtoObject instanceof List<?>) {
            List<TodoDto> todoDtoList = (List<TodoDto>) todoDtoObject;
            for(TodoDto t : todoDtoList) {
                dueDate = t.getDueDate().atStartOfDay();
                dDay = (int) ChronoUnit.DAYS.between(sysDate, dueDate);
                dDay = dDay < 0 ? -1 : dDay;
                t.setDDay(dDay);
            }
        } else if (todoDtoObject instanceof TodoDto) {
            TodoDto todoDto = (TodoDto) todoDtoObject;
            dueDate = todoDto.getDueDate().atStartOfDay();
            dDay = (int) ChronoUnit.DAYS.between(sysDate, dueDate);
            dDay = dDay < 0 ? -1 : dDay;
            todoDto.setDDay(dDay);
        }

        
    }
}
