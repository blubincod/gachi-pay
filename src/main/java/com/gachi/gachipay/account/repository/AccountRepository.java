package com.gachi.gachipay.account.repository;

import com.gachi.gachipay.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long > {
    /**
     * 계좌 존재 여부
     * @param accountNumber
     * @return
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * 계좌 정보 가져오기
     */
    Optional<Account> findFirstByOrderByIdDesc();
}
