package com.personalproject.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String id = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = userDetailsService.loadUserByUsername(id);

        /*
			null을 return하면 FailureHandler에 BadCredentialsException 인스턴스인 exception이 인자로 들어간다.
			커스텀하여 메시지를 추가할 수 있는 방법이 있다.
			xml로 에러메시지가 등록된 properties파일 및 MessageSourceAccessor를 등록한다.
		 */

		/*
			username이 null 일 때
			throw UsernameNotFoundException, return null 등을 해도 BadCredentialsException가 던져진다.
			설정으로 바꿀 수 있지만 보안상의 이유로 추천되지 않는다.
			if(memberDetails.getUsername() == null) { }
		*/

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(id, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

//         AuthenticationProvider를 동작하게 하기 위해 return true로 설정.
        return true;
    }
}
