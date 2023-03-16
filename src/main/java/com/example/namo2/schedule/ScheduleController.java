package com.example.namo2.schedule;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.utils.Converter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedules")
@Api(value = "Schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final Converter converter;

    public ScheduleController(ScheduleService scheduleService, Converter converter) {
        this.scheduleService = scheduleService;
        this.converter = converter;
    }

    @ResponseBody
    @PostMapping("")
    @ApiOperation(value = "스케줄 생성")
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
    @ApiOperation(value = "스케줄 월별 조회 ")
    public BaseResponse<List<GetScheduleRes>> findUserSchedule(@PathVariable("month") String month) {
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
    @ApiOperation(value = "스케줄 수정")
    public BaseResponse<ScheduleIdRes> updateUserSchedule(@PathVariable("schedule") Long scheduleId, @RequestBody PostScheduleReq postScheduleReq) {
        try {
            ScheduleDto scheduleDto = new ScheduleDto(postScheduleReq);
            ScheduleIdRes scheduleIdRes = scheduleService.updateSchedule(scheduleId, scheduleDto);
            return new BaseResponse<>(scheduleIdRes);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    /**
     * Todo: 스케줄 삭제 시 이미지 삭제 S3 에 서 동반되어야함
     */
    @ResponseBody
    @DeleteMapping("/{schedule}")
    @ApiOperation(value = "스케줄 삭제")
    public BaseResponse<String> deleteUserSchedule(@PathVariable("schedule") Long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
            return new BaseResponse<>("삭제에 성공하였습니다.");
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/diary")
    @ApiOperation(value = "스케줄 다이어리 생성")
    public BaseResponse<ScheduleIdRes> createDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                                   @RequestPart String scheduleId,
                                                   @RequestPart(required = false) String content) {
        try {
            ScheduleIdRes scheduleIdRes;
            if (imgs == null) {
                scheduleIdRes = scheduleService.createDiary(Long.valueOf(scheduleId), content);
            } else {
                scheduleIdRes = scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
            }
            return new BaseResponse<>(scheduleIdRes);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/diary/{month}")
    @ApiOperation(value = "스케줄 다이어리 조회")
    public BaseResponse<List<DiaryDto>> findDiaryByMonth(@PathVariable("month") String month) {
        try {
            Long userId = 1L;
            List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
            List<DiaryDto> diaries = scheduleService.findMonthDiary(userId, localDateTimes);
            return new BaseResponse<>(diaries);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/diary")
    @ApiOperation(value = "스케줄 다이어리 수정")
    public BaseResponse<String> updateDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                            @RequestPart String scheduleId,
                                            @RequestPart(required = false) String content) {
        try {
            scheduleService.deleteDiary(Long.valueOf(scheduleId));
            scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
            return new BaseResponse<>("수정에 성공하셨습니다.");
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/diary/{schedule}")
    @ApiOperation(value = "스케줄 다이어리 삭제")
    public BaseResponse<String> deleteDiary(@PathVariable("schedule") Long scheduleId) {
        try {
            scheduleService.deleteDiary(scheduleId);
            return new BaseResponse<>("삭제에 성공하셨습니다.");
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }
}
