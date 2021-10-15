package com.personalproject.todo.vo;

import lombok.Data;

@Data
public class Member {

    private String id;
    private String password;
    private String email;
    private String regDate;
    private String auth;
}
