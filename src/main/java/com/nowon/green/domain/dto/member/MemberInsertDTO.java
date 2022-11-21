package com.nowon.green.domain.dto.member;

import com.nowon.green.domain.entity.MemberEntity;
import com.nowon.green.security.MemberRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Getter
@Setter
//회원가입처리를 위한 데이터
public class MemberInsertDTO {
    private String email;
    private String pass;
    private String name;

    //회원가입 서비스처리시 DB에서 Entity객체로 저장해야하므로
    //메서드로 생성시킬 수 있는 편의 메서드
    public MemberEntity toEntity(PasswordEncoder pe){
        return MemberEntity.builder()
                .email(email).pass(pe.encode(pass)).name(name)
                .build();
    }
}
