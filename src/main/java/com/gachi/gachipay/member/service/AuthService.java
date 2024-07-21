package com.gachi.gachipay.member.service;

import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.model.Auth;
import com.gachi.gachipay.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    /**
     * 회원 가입
     */
    public Member register(Auth.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());

        // username 중복 체크
        if (exists) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        // 비밀번호 암호화
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));

        // member 테이블에 저장
        var result = this.memberRepository.save(member.toEntity());


        return result;
    }

    /**
     * 로그인
     */
    public Member authenticate(Auth.SignIn member) {
        var result = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 username 입니다."));

        if (!this.passwordEncoder.matches(member.getPassword(), result.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return result;
    }


}
