package com.personalproject.todo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.personalproject.todo.member.Auth;
import com.personalproject.todo.member.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private final Member member;

    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Auth auth = member.getAuth();
        String role = auth == null ? "ROLE_ANONYMOUS" : auth.value();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member == null ? null : member.getPassword();
    }

    @Override
    public String getUsername() {
        return member == null ? null : member.getId();
    }

    // 계정이 만료되지 않았는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 사용 가능한지
    @Override
    public boolean isEnabled() {
        return true;
    }
}
