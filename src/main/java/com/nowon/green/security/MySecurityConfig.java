package com.nowon.green.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.Locale;

@EnableWebSecurity
public class MySecurityConfig {


    @Bean
    public MessageSource messageSource(){
        Locale.setDefault(Locale.KOREA);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        //커스텀할 설정파일위치 먼저 지정 후,
        //에러메세지가 기본적으로 정의되어 있는 기본설정파일 위치
        // classpath:org/springframework/security/messages
        messageSource.setBasenames("classpath:message/security_message","classpath:org/springframework/security/messages" );
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() throws IOException {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService customUserDetailsService(){
        return new CustomUserDetailsService();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/css/**").permitAll() //static 자원 설정
                        .antMatchers("/","/signup","/signin").permitAll()
                        .anyRequest().authenticated())
                //인증이 필요할때 security가 이동시키는 주소
                .formLogin(formLogin->formLogin
                        .loginPage("/signin")
                        .loginProcessingUrl("/signin")//action="/signin" method="post"
                        .usernameParameter("email")//default:username
                        .passwordParameter("pass") //default:password
                        //여기까지 signin.html 정보와 이어진다.
                        .permitAll())
                .logout(logout->logout
                        .logoutUrl("/signout"))
                .oauth2Login(oauth2->oauth2
                        .loginPage("/signin")
                        .userInfoEndpoint(userInfo->userInfo
                                .userService(customOAuth2UserService())))
                //.csrf(csrf->csrf.disable())// csrf 적용안할때 disable()로 표기 //표기하지 않으면 기본보안설정
        ;
        return http.build();
        //CommonOAuth2Provider
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new MyOAuth2UserService();
    }
}


