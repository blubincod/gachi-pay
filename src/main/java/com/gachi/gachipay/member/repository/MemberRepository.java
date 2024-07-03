package com.gachi.gachipay.member.repository;

import com.gachi.gachipay.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 회원 이름 존재 여부
     *
     * @param username
     * @return
     */
    boolean existsByUsername(String username);

    /**
     * 회원 이름으로 정보 가져오기
     *
     * @param username
     * @return
     */
    Optional<Member> findByUsername(String username);

//    Optional<Member> findByEmail(String email);
}
