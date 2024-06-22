package com.gachi.gachipay.configuration;

import com.gachi.gachipay.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;

//    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보안 설정 비활성화
//                .formLogin(formLogin -> formLogin.disable())// 폼 로그인 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/public/**").permitAll() // 홈 화면은 인증 필요 X
                        .anyRequest().authenticated() // 그 외는 모두 인증 필요
                )
                .logout(logout -> logout // 로그아웃 설정
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL 설정
                        .permitAll() // 로그아웃 요청은 인증 없이 접근 가능
                )

                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth/loginInfo", true) // 로그인 성공시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service) // 해당 서비스 로직을 타도록 설정
                        ));

        return http.build();
    }
}
