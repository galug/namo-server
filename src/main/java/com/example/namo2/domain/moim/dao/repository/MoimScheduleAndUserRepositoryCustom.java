package com.example.namo2.domain.moim.dao.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;

import com.example.namo2.domain.user.domain.User;

public interface MoimScheduleAndUserRepositoryCustom {
	List<MoimScheduleAndUser> findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(LocalDateTime startDate,
		LocalDateTime endDate, List<User> users);

	List<MoimScheduleAndUser> findMoimScheduleMemoByMonthPaging(User user, List<LocalDateTime> dates,
		Pageable pageable);
}
