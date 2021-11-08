package com.personalproject.todo.todo;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("todo")
public class TodoRestController {

    TodoService todoService;

    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 일정 수정을 위한 정보를 ajax를 통해 반환한다.
    @PostMapping("minimalTodoInfo")
    public Object minimalTodoInfo(
        Integer no,
        Authentication authentication
    ) {
        String id = authentication.getName();
        TodoDto todo = todoService.membersTodo(id, no);
        MinimalTodoDto miniTodoDto = new MinimalTodoDto(todo);
        return miniTodoDto;
    }

    // 멤버의 일정 하나를 반환한다.
    @PostMapping("getMembersTodo")
    public Object getMembersTodo(
        Integer no,
        Authentication authentication
    ) {
        String id = authentication.getName();
        return todoService.membersTodo(id, no);
    }
    
}
