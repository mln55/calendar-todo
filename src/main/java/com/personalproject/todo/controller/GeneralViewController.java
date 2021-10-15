package com.personalproject.todo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class GeneralViewController {


    Logger logger = log;

    @GetMapping("")
    public String home(
            HttpSession session,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("thanksMsg", session.getAttribute("thanksMsg"));
        model.addAttribute("loginFailMsg", session.getAttribute("loginFailMsg"));
        session.removeAttribute("thanksMsg");
        session.removeAttribute("loginFailMsg");
        getClientIP(request);
        return "home";
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        return "forward:/static/favicon.ico";
    }

    @GetMapping("/robots.txt")
    public String robots() {
        return "forward:/static/robots.txt";
    }

    private String getClientIP(HttpServletRequest request) {


        String ip = request.getHeader("X-Forwarded-For");
        logger.info("> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.info("> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.info(">  WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            logger.info("> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            logger.info("> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            logger.info("> getRemoteAddr : "+ip);
        }
        logger.info("> Result : IP Address : "+ip);

        return ip;
    }
}