package com.personalproject.todo.configuration;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 정적 페이지를 매핑하는 컨트롤러를 설정한다.
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/favicon.ico").setViewName("forward:/static/favicon.ico");
        registry.addViewController("/robots.txt").setViewName("forward:/static/robots.txt");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 파라미터로 들어오는 문자열을 LocalDate 객체로 변환하기 위한 포매터를 등록한다.
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-M-d"));
        registrar.registerFormatters(registry);
    }
}
