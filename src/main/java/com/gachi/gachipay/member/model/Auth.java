package com.gachi.gachipay.member.model;

import com.gachi.gachipay.member.entity.Member;
import lombok.Data;

public class Auth {

    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private String phoneNumber;

        public Member toEntity(){
            return Member.builder()
                    .username(this.username)
                    .password(this.password)
                    .phoneNumber(this.phoneNumber)
                    .build();
        }
    }
}
