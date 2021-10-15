package com.personalproject.todo.service;

import com.personalproject.todo.vo.Member;

public interface MemberService {

    Member getMember(String id);

    boolean isOverlapId(String id);

    int insertMember(Member member);
}