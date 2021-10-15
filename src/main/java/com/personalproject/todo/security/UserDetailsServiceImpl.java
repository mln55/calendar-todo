package com.personalproject.todo.security;

import com.personalproject.todo.dao.MemberDao;
import com.personalproject.todo.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberDao.findMember(id);

        log.info(String.format("return UserDetails ID - %s:", id));
        return new UserDetailsImpl(member);
    }
}
