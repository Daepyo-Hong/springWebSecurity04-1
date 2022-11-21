package com.nowon.green.controller;

import com.nowon.green.domain.dto.member.MemberInsertDTO;
import com.nowon.green.service.MemberService;
import com.nowon.green.service.impl.MemberServiceProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignController {


    @Qualifier("memberServiceProcess")
    @Autowired
    private MemberService service;

    @GetMapping("/signin")
    public String signin(){
        return "sign/signin";
    }
    @GetMapping("/signup")
    public String signup(){
        return "sign/signup";
    }

    @PostMapping("/signup")
    public String signup(MemberInsertDTO dto){
        service.save(dto);
        return "redirect:/signin?signup";
    }


}
