package com.example.namo2.domain.individual.dao.repository.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

import com.example.namo2.domain.user.domain.User;

public interface ScheduleRepositoryCustom {
	List<ScheduleResponse.GetScheduleDto> findSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate);

	ScheduleResponse.SliceDiaryDto findScheduleDiaryByMonthDto(User user, LocalDateTime startDate,
		LocalDateTime endTime, Pageable pageable);

	List<ScheduleResponse.GetDiaryByUserDto> findAllScheduleDiary(User user);

	Schedule findOneScheduleAndImages(Long scheduleId);

	List<ScheduleResponse.GetScheduleDto> findMoimSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate);
}
