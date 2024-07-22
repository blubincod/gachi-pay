package com.gachi.gachipay.team.repository;

import com.gachi.gachipay.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
