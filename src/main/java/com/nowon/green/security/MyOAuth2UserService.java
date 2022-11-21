package com.nowon.green.security;

import com.nowon.green.domain.entity.MemberEntity;
import com.nowon.green.domain.entity.MemberEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Log4j2
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private MemberEntityRepository mRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //DefaultOAuth2UserService
        //소셜로그인 인증완료...

        //DB에서 인증후 인증정보전달 MyUser 타입에 통합

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();     //google, naver, kakao 등등 중에 출력됨
        log.info("registrationId: " + registrationId);
       // /*
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // attributes.keySet();
        for( String key : attributes.keySet() ){
            System.out.println(key+" : "+attributes.get(key));
        }
        //*/
       // return socialUserProcess(oAuth2User, registrationId);
        return super.loadUser(userRequest);
    }

    //google, naver, kakao 별로 제공하는 인증정보가 각각 달라요. 수집을 통일화 할게요
    private OAuth2User socialUserProcess(OAuth2User oAuth2User, String registrationId) {
        String email = null;
        String name = null;
        if (registrationId.equals("google")) {
            name = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
        } else if (registrationId.equals("naver")) {
            //네이버인 경우 처리작업
        }
        //소셜정보를 DB에 저장하는 경우//
        //System.out.println(">>>>>"+mRepository);
        //1. 이미 등록이 되어있으면 문제가발생
        Optional<MemberEntity> result = mRepository.findByEmail(email);

        if(result.isPresent()){
            System.out.println("이미 존재하는 이메일");
            return new MyUser(result.get());
        }

        MemberEntity savedSocialUser = mRepository.save(MemberEntity.builder()
                .email(email).name(name).nickName(name)
                .pass(encoder.encode(registrationId))//비밀번호가 의미없음. DB기준필수요소이기에 넣어준것.
                .social(true)   //소셜유저인지만(종류는 아직 구분 안함)
                .build().addRole(MemberRole.USER));

        return new MyUser(savedSocialUser);
    }
}
