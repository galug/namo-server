package com.example.namo2.schedule;

import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponse;
import com.example.namo2.config.BaseResponseStatus;
import com.example.namo2.entity.Period;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.utils.Converter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final Converter converter;

    public ScheduleController(ScheduleService scheduleService, Converter converter) {
        this.scheduleService = scheduleService;
        this.converter = converter;
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<ScheduleIdRes> createSchedule(@RequestBody PostScheduleReq postScheduleReq) {
        try {
            ScheduleDto scheduleDto = new ScheduleDto(postScheduleReq);

            ScheduleIdRes scheduleIdRes = scheduleService.createSchedule(scheduleDto, 1L);
            return new BaseResponse<>(scheduleIdRes);
        } catch (BaseException baseException) {
            System.out.println("baseException.getStatus() = " + baseException.getStatus());
            return new BaseResponse(baseException.getStatus());
        }
    }

    /**
     * Todo: 그룹 스케줄 조회에 대한 처리가 필요할 예정
     */
    @ResponseBody
    @GetMapping("/{month}")
    public BaseResponse<List<GetScheduleRes>> findUserSchedule(@PathVariable("month")String month) {
        try {
            List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
            List<GetScheduleRes> userSchedule = scheduleService.findUsersSchedule(1L, localDateTimes);
            return new BaseResponse<>(userSchedule);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{schedule}")
    public BaseResponse<ScheduleIdRes> updateUserSchedule(@PathVariable("schedule") Long scheduleId, @RequestBody PostScheduleReq postScheduleReq) {
        try {
            ScheduleDto scheduleDto = new ScheduleDto(postScheduleReq);
            ScheduleIdRes scheduleIdRes = scheduleService.updateSchedule(scheduleId, scheduleDto);
            return new BaseResponse<>(scheduleIdRes);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/{schedule}")
    public BaseResponse<String> deleteUserSchedule(@PathVariable("schedule") Long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
            return new BaseResponse<>("삭제에 성공하였습니다.");
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }
}
