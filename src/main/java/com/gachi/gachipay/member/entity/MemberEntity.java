package com.gachi.gachipay.member.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    private String email; //이메일

    private String name; //이름

    private String password; //비밀번호

    private String rrn; //주민등록번호
}
