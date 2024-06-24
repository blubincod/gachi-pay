package com.gachi.gachipay.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @PostMapping("/member/test")
    public void getMember(){
        System.out.println();
    }

}
