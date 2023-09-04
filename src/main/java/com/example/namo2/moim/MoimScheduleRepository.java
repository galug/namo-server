package com.example.namo2.moim;

import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoimScheduleRepository extends JpaRepository<MoimSchedule, Long>, MoimScheduleRepositoryCustom {
}
