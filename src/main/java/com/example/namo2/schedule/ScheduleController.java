package com.example.namo2.schedule;

import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponse;
import com.example.namo2.config.BaseResponseStatus;
import com.example.namo2.entity.Period;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.PostScheduleRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.utils.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final Converter converter;

    public ScheduleController(ScheduleService scheduleService, Converter converter) {
        this.scheduleService = scheduleService;
        this.converter = converter;
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostScheduleRes> createSchedule(@RequestBody PostScheduleReq postScheduleReq) {
        try {
            Period period = new Period(postScheduleReq.getStartDate(), postScheduleReq.getEndDate(), postScheduleReq.getAlarm());
            Point point = new Point(postScheduleReq.getX(), postScheduleReq.getY());
            ScheduleDto scheduleDto = new ScheduleDto(postScheduleReq.getName(), period, point);

            PostScheduleRes postScheduleRes = scheduleService.createSchedule(scheduleDto, 1L, postScheduleReq.getCategoryId());
            return new BaseResponse<>(postScheduleRes);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        } catch (IllegalArgumentException illegalArgumentException) {
            return new BaseResponse<>(BaseResponseStatus.SCHEDULE_ILLEGAL_ARGUMENT_FAILURE);
        }
    }

}
