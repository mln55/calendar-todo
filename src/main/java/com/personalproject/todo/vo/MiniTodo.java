package com.personalproject.todo.vo;

import lombok.Data;

@Data
public class MiniTodo {

    private String title;
    private String content;
    private String beginDate;
    private String dueDate;
    private String color;
}
