// package com.gachi.gachipay.global.oauth;
// 
// import com.gachi.gachipay.member.entity.Member;
// import lombok.Builder;
// 
// import java.util.Map;
// 
// @Builder
// public class OAuth2UserinfoDto {
//     private final String name;
//     private final String email;
//     private final String profile;
// 
//     /**
//      * Google 유저 정보
//      */
//     public OAuth2UserinfoDto ofGoogle(Map<String,Object> attributes){
//         System.out.println(attributes);
// 
//         return null;
//     }
// 
//     //  TODO KAKAO 유저 정보
//     //  TODO NAVER 유저 정보
// 
//     public Member toEntity(){
//         return Member.builder()
//                 .username(name)
//                 .email(email)
//                 .build();
//     }
// }
