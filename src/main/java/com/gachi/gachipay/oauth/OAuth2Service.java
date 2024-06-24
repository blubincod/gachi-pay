package com.gachi.gachipay.oauth;

import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            String email = oAuth2User.getAttributes().get("email").toString();
            Optional<Member> optionalMember = memberRepository.findByEmail(email);

            if(optionalMember.isPresent()){
                System.out.println("로그인 되었습니다.");
            }

            if(!optionalMember.isPresent()){
                Member member = Member.builder()
                        .email(email)
                        .build();

                memberRepository.save(member);
            }

            return oAuth2User;
    }
}
