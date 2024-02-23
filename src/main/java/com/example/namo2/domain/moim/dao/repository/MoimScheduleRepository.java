package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.MoimSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimScheduleRepository extends JpaRepository<MoimSchedule, Long>, MoimScheduleRepositoryCustom {
}
