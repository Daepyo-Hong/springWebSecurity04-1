package com.nowon.green.security;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.nowon.green.domain.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;


import lombok.Getter;

//User implements UserDetails
@Getter
public class MyUser extends User implements OAuth2User{// 페이지에서  #authentication.principal.mno , #authentication.principal.nick
    private long mno;
    private String nick;

    private Map<String, Object> attributes;

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    //추가 user정보 저장을 위해
    public MyUser(MemberEntity ent) {
        this(ent.getEmail(), ent.getPass(), ent.getRoles().stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toSet()));
        mno=ent.getMemberNo(); // member의 pk
        nick= (ent.getNickName()==null) ? "별칭없음" : ent.getNickName();// nickName 정보

        attributes=new HashMap<>();
        attributes.put("mno", mno);
        attributes.put("nick", nick);

    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() {
        return getUsername();
    }
}