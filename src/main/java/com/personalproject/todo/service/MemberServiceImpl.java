package com.personalproject.todo.service;

import com.personalproject.todo.dao.MemberDao;
import com.personalproject.todo.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Member getMember(String id) {
        return memberDao.findMember(id);
    }

    @Override
    public boolean isOverlapId(String id) {
        return memberDao.findId(id) != null;
    }

    @Override
    public int insertMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberDao.insertMember(member);
    }


}