package com.gachi.gachipay.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        System.out.println("pr-test-one 입니다.");
        return "index";
    }

}
