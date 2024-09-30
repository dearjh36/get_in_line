package com.project.getinline.config;

import com.project.getinline.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 insert문(sql)에 시큐리티 헤더를 추가해줌
        // insert into'admin' ('email' , ... , {시큐리티 헤더} 'password' ... )
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth,
            PasswordEncoder passwordEncoder,
            AdminService adminService
    ) throws Exception {
        auth.userDetailsService(adminService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 시큐리티 설정
        http.authorizeRequests()
                .antMatchers("/", "/events/**", "/places/**")
                    .permitAll()
                // 어떤 요청이든 인증타야함
                .anyRequest()
                  .authenticated()
                .and()
                .formLogin()
                // 로그인 페이지, 로그아웃 페이지는 허용
                    .permitAll()
                .and()
                .logout()
                   .permitAll()
                  .logoutSuccessUrl("/");
    }
}
