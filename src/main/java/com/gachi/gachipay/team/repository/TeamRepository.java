package com.gachi.gachipay.team.repository;

import com.gachi.gachipay.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 스케쥴러를 위한 그룹 아이디와 날짜 존재 확인
//    boolean existsIdAndFeeDueDate(Long id, LocalDate feeDueDate);
}
