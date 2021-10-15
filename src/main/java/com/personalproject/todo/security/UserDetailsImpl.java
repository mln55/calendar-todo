package com.personalproject.todo.security;

import com.personalproject.todo.vo.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private Member member;

    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String auth = member.getAuth();
        String role = "ROLE_ANONYMOUS";

        switch (auth) {
            case "none":
                role = "ROLE_USER";
                break;
            case "email auth":
                role = "ROLE_MEMBER";
                break;
            case "all auth":
                role = "ROLE_ADMIN";
                break;
        }

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
