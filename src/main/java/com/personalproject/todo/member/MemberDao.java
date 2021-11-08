package com.personalproject.todo.member;

public interface MemberDao {

    Member findMember(String id);

    String findId(String id);

    int insertMember(Member member);
}
