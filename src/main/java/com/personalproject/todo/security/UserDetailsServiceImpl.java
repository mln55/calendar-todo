package com.personalproject.todo.security;

import com.personalproject.todo.member.MemberDaoImpl;
import com.personalproject.todo.member.Member;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberDaoImpl jdbcMemberRepository;

    public UserDetailsServiceImpl(MemberDaoImpl jdbcMemberRepository) {
        this.jdbcMemberRepository = jdbcMemberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = jdbcMemberRepository.findMember(id);

        log.info(String.format("return UserDetails ID - %s:", id));
        return new UserDetailsImpl(member);
    }
}
