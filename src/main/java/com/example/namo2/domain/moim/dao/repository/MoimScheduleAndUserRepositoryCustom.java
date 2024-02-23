package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimScheduleAndUserRepositoryCustom {
    List<MoimScheduleAndUser> findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(LocalDateTime startDate, LocalDateTime endDate, List<User> users);
}
