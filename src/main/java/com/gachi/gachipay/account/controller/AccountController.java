package com.gachi.gachipay.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/user/account")
    public ResponseEntity<?> testAccount(){
        System.out.println("GET ACCOUNT:)");

        return ResponseEntity.ok(null);
    }
}
