package com.gachi.gachipay.member.entity;


import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.team.entity.TeamMembership;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@EntityListeners(AuditingEntityListener.class) // 감사(auditing) 기능 활성화
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    private String username; // 회원 이름

    private String password; // 비밀번호

    private String phoneNumber; // 전화번호

    @OneToMany(mappedBy = "member")
    private List<Account> accounts = new ArrayList<>(); // 회원 계좌들

    @OneToMany(mappedBy = "member")
    private List<TeamMembership> teamMemberships = new ArrayList<>(); // 회원이 속한 그룹들

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // 계좌 추가 메서드
    public void addAccount(Account account) {
        accounts.add(account);
        account.setMember(this);
    }
}
