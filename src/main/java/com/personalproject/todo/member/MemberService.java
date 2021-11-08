package com.personalproject.todo.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member geMember(String id) {
        return memberDao.findMember(id);
    }

    public boolean isOverlapId(String id) {
        return memberDao.findId(id) != null;
    }

    public int insertMember(Member member) {
        return memberDao.insertMember(member);
    }
}
