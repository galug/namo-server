package com.example.namo2.domain.schedule.application.impl;

import com.example.namo2.domain.schedule.dao.repository.ScheduleRepository;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;


@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long scheduleId){
        return scheduleRepository.findById(scheduleId).orElseThrow(()->new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
    }
    public List<ScheduleResponse.GetScheduleDto> getSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate
    ) throws BaseException {
        return scheduleRepository.findSchedulesByUserId(user, startDate, endDate);
    }

    public List<ScheduleResponse.GetScheduleDto> getMoimSchedulesByUser(User user, LocalDateTime startDate, LocalDateTime endDate
    ) throws BaseException {
        return scheduleRepository.findMoimSchedulesByUserId(user, startDate, endDate);
    }

    public List<ScheduleResponse.GetScheduleDto> getAllSchedulesByUser(User user) {
        return scheduleRepository.findSchedulesByUserId(user, null, null);
    }

    public List<ScheduleResponse.GetScheduleDto> getAllMoimSchedulesByUser(User user) {
        return scheduleRepository.findMoimSchedulesByUserId(user, null, null);
    }

    public ScheduleResponse.SliceDiaryDto getScheduleDiaryByUser(
        User user,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    ) throws BaseException {
        return scheduleRepository.findScheduleDiaryByMonthDto(user, startDate, endDate, pageable);
    }

    public List<ScheduleResponse.GetDiaryByUserDto> getAllDiariesByUser(User user) {
        return scheduleRepository.findAllScheduleDiary(user);
    }

    public void removeSchedule(Schedule schedule){
        scheduleRepository.delete(schedule);
    }

}
