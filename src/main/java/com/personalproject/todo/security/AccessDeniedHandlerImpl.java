package com.personalproject.todo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ade) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.format("accessDenied URI:%s  ID:%s", request.getRequestURI(), authentication.getName()));
        response.setContentType("text/html; charset=utf8");
        // response.getWriter().print("<script>alert('접근 권한이 없습니다.'); location.href='http://todo.develop-ing.site/';</script>");
        response.getWriter().print("<script>alert('접근 권한이 없습니다.'); location.href='/';</script>");
    }
}
