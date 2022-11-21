package com.nowon.green.service.impl;

import com.nowon.green.domain.dto.member.MemberInsertDTO;
import com.nowon.green.domain.entity.MemberEntityRepository;
import com.nowon.green.security.MemberRole;
import com.nowon.green.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceProcess2 implements MemberService {
    @Autowired
    MemberEntityRepository repository;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public void save(MemberInsertDTO dto) {

        System.out.println("MemberServiceProcess2");

        //미리 만들어 놓은 dto의 편의 메서드를 이용하여 사용 dto의 Getter 비 사용
        //repository.save(dto.toEntity(encoder).addRole(MemberRole.USER));
        /*
        //편의메서드 필수는 아니고 직접 service에서 dto의 Getter 사용해도 됩니다.
        repository.save(MemberEntity.builder()
                        .email(dto.getEmail()).name(dto.getName())
                        .pass(encoder.encode(dto.getPass()))
                .build().addRole(MemberRole.USER));
        */
    }
}
