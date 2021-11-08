package com.personalproject.todo.todo;

import static com.personalproject.todo.utils.DateTimeUtils.stringDateToLocalDate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("todo")
public class TodoViewController {

    private final TodoService todoService;

    public TodoViewController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 일정 등록 후 redirect
    @PostMapping("addTodo")
    public String addTodo(
        TodoDto todoDto,
        Authentication authentication
    ) {
        String id = authentication.getName();
        todoDto.setMId(id);
        todoService.insertTodo(todoDto);
        return String.format("redirect:/calendar/cal?date=%s", todoDto.getBeginDate());
    }

    // 일정 수정 후 redirect
    @PostMapping("modifyTodo")
    public String modifyTodo(
        TodoDto todoDto,
        Authentication authentication
    ) {
        String id = authentication.getName();

        todoDto.setMId(id);
        todoService.modifyTodo(todoDto);
        return String.format("redirect:/calendar/cal?date=%s", todoDto.getBeginDate());
    }

    // 일정 삭제 후 redirect
    @PostMapping("deleteTodo")
    public String deleteTodo(
        int no,
        String date
    ) {
        todoService.deleteTodo(no);
        return String.format("redirect:/calendar/cal?date=%s", date);
    }

    // 일정 색상 변경 후 redirect
    @PostMapping("changeTodoColor")
    public String changeTodoColor(
        int no,
        String color,
        String date,
        Authentication authentication
    ) {

        color = color.replace("numSign", "#");
        todoService.changeTodoColor(no, color); // 반환 값 있음

        return String.format("redirect:/calendar/cal?date=%s", date);
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
        Authentication authentication
    ) {

        String id = authentication.getName();
        LocalDate firstLocalDate = stringDateToLocalDate(firstDate);
        LocalDate lastLocalDate = stringDateToLocalDate(lastDate);

        List<TodoDto> todoDtoSearchList = todoService.getTodoByMemberWithSearch(id, firstLocalDate, lastLocalDate, include, ignore, target, sort);
        model.addAttribute("todoSearchList", todoDtoSearchList);

        return "fragments/page/todoSearchResult";
    }
}
