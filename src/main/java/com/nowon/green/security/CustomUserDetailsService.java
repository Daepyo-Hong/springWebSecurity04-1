package com.nowon.green.security;

import com.nowon.green.domain.entity.MemberEntity;
import com.nowon.green.domain.entity.MemberEntityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

//DaoAuthenticationPrivider
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    //username, password, 권한들(ROLE)
    @Autowired
    private MemberEntityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>>> username(email): "+username);
        return new MyUser(repository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.")));
        //로그인시도시 이메일이존제하지않으면 UsernameNotFoundException 예외발생되나
        //최종적으로는 BadCredentialsException 으로 처리됩니다.

/*
        //email 컬럼을 조회조건으로 해서 멤버조회
        MemberEntity entity= repository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("유저가없어용가리"));

        String password=entity.getPass();       //encoded password
        Collection<SimpleGrantedAuthority> authorities =entity.getRoles()
                .stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toSet());

        //return new User(username,password,authorities);
        return new MyUser(username,password,authorities);
        */
    }
}
