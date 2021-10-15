package com.personalproject.todo.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("text/html; charset=utf8");
        // response.getWriter().print("<script>alert('로그인이 필요한 서비스입니다.'); location.href='http://todo.develop-ing.site/';</script>");
        response.getWriter().print("<script>alert('로그인이 필요한 서비스입니다.'); location.href='/';</script>");
    }

}

