package com.gachi.gachipay.transaction.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RestController
public class PayController {

    @PostMapping("/pay")
    public void pay(){

    }
}
