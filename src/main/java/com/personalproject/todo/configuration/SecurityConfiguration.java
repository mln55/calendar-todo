package com.personalproject.todo.configuration;

import com.personalproject.todo.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() { return new AccessDeniedHandlerImpl(); };

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() { return new AuthenticationEntryPointImpl(); };

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() { return new LoginSuccessHandlerImpl(); }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() { return new LoginFailureHandlerImpl(); }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { return new LogoutSuccessHandlerImpl(); }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 폴더 보안 대상 제외
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/calendar/**").authenticated()
                    .antMatchers("/todo/**").authenticated()
                    .antMatchers("/**").permitAll()
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .and()
                .formLogin()
                    .usernameParameter("id")
                    .passwordParameter("password")
                    .loginPage("/home")
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler())
                    .failureHandler(loginFailureHandler())
                    .and()
                .rememberMe()
                    .rememberMeParameter("keep-login")
                    .rememberMeCookieName("keep_login")
                    .key("keepLoginIdPw")
                    .tokenValiditySeconds(86400 * 14)
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .and()
                .csrf().disable();
    }
}
