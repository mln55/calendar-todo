package com.personalproject.todo.dao;

import com.personalproject.todo.vo.Member;

public interface MemberDao {

    Member findMember(String id);

    String findId(String id);

    int insertMember(Member member);
}
