package com.personalproject.todo.vo;

import lombok.Data;

@Data
public class Todo {

    private int no;
    private String mId;
    private String title;
    private String content;
    private String beginDate;
    private String dueDate;
    private int status;
    private String color;
    private int dDay;
    private int barLevel;
}
