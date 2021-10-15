package com.personalproject.todo.controller;

import com.personalproject.todo.vo.MiniTodo;
import com.personalproject.todo.vo.Todo;
import com.personalproject.todo.service.CalendarService;
import com.personalproject.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/todo")
@PreAuthorize("isAuthenticated()")
public class TodoController {

    @Autowired
    CalendarService calendarService;

    @Autowired
    TodoService todoService;

    // 일정 등록
    @PostMapping("/addTodo")
    public String addTodo(
            Todo todo,
            Authentication authentication,
            Model model) {

        String id = authentication.getName();

        todo.setMId(id);
        todo.setColor(todo.getColor().replace("numSign", "#"));
        todoService.insertTodo(todo);
        return String.format("redirect:/calendar/cal?date=%s", todo.getBeginDate());
    }

    // 일정 수정을 위한 필수 정보를 ajax로 보낸다.
    @PostMapping("/minimalTodoInfo")
    @ResponseBody
    public Object minimalTodoInfo(
            int no,
            Authentication authentication) {

        String id = authentication.getName();
        Todo todo = todoService.getMembersTodo(id, no);
        MiniTodo mTodo = new MiniTodo();
        mTodo.setTitle(todo.getTitle());
        mTodo.setContent(todo.getContent());
        mTodo.setBeginDate(todo.getBeginDate());
        mTodo.setDueDate(todo.getDueDate());
        mTodo.setColor(todo.getColor());
        return mTodo;
    }

    // 일정 수정
    @PostMapping("/modifyTodo")
    public String modifyTodo(
            Todo todo,
            Authentication authentication,
            Model model) {

        String id = authentication.getName();

        todo.setMId(id);
        todo.setColor(todo.getColor().replace("numSign", "#"));
        String date = todo.getBeginDate();
        todoService.modifyTodo(todo);

        return String.format("redirect:/calendar/cal?date=%s", date);
    }

    // 일정 삭제
    @RequestMapping("/deleteTodo")
    public String deleteTodo(
            int no,
            String date) {

        todoService.deleteTodo(no);
        return String.format("redirect:/calendar/cal?date=%s", date);
    }

    // 일정 색상 변경
    @RequestMapping(path = "/changeTodoColor")
    public String changeTodoColor(
            int no,
            String color,
            String date,
            Authentication authentication) {

        color = color.replace("numSign", "#");

        todoService.changeTodoColor(no, color); // 반환 값 있음

        String[] dateArr = date.split("-");
        return String.format("redirect:/calendar/cal?date=%s-%s-%s", dateArr[0], dateArr[1], dateArr[2]);
    }

    @PostMapping(path = "/getMembersTodo")
    @ResponseBody
    public Object getMembersTodo(
            int no,
            Authentication authentication) {
        String id = authentication.getName();

        return todoService.getMembersTodo(id, no);
    }

    // ajax 요청이 오면 일정 검색 결과 페이지를 반환
    @PostMapping(path = "/getTodoWithSearch")
    public String getTodoWithSearch(
            String firstDate,
            String lastDate,
            String include,
            @RequestParam(defaultValue = "off") String ignore,
            String target,
            String sort,
            Model model,
            Authentication authentication) {

        String id = authentication.getName();

        List<Todo> todoSearchList = todoService.getTodoByMemberWithSearch(id, firstDate, lastDate, include, ignore, target, sort);
        model.addAttribute("todoSearchList", todoSearchList);

        return "fragments/page/todoSearchResult";
    }
}