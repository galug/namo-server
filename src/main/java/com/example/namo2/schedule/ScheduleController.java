package com.example.namo2.schedule;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.GetDiaryRes;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleIdRes;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public BaseResponse<ScheduleIdRes> createSchedule(@Valid @RequestBody PostScheduleReq postScheduleReq, HttpServletRequest request) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.createSchedule(postScheduleReq, (Long) request.getAttribute("userId"));
        return new BaseResponse<>(scheduleIdRes);
    }

    /**
     * Todo: 그룹 스케줄 조회에 대한 처리가 필요할 예정
     */
    @ResponseBody
    @GetMapping("/{month}")
    @ApiOperation(value = "스케줄 월별 조회 ")
    public BaseResponse<List<GetScheduleRes>> findUserSchedule(@PathVariable("month") String month, HttpServletRequest request) throws BaseException {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<GetScheduleRes> userSchedule = scheduleService.findUsersSchedule((Long) request.getAttribute("userId"), localDateTimes);
        return new BaseResponse<>(userSchedule);
    }

    @ResponseBody
    @PatchMapping("/{schedule}")
    @ApiOperation(value = "스케줄 수정")
    public BaseResponse<ScheduleIdRes> updateUserSchedule(
            @PathVariable("schedule") Long scheduleId,
            @RequestBody PostScheduleReq postScheduleReq) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.updateSchedule(scheduleId, postScheduleReq);
        return new BaseResponse<>(scheduleIdRes);
    }

    /**
     * Todo: 스케줄 삭제 시 이미지 삭제 S3 에 서 동반되어야함
     */
    @ResponseBody
    @DeleteMapping("/{schedule}")
    @ApiOperation(value = "스케줄 삭제")
    public BaseResponse<String> deleteUserSchedule(@PathVariable("schedule") Long scheduleId) throws BaseException {
        scheduleService.deleteSchedule(scheduleId);
        return new BaseResponse<>("삭제에 성공하였습니다.");
    }

    @ResponseBody
    @PostMapping("/diary")
    @ApiOperation(value = "스케줄 다이어리 생성")
    public BaseResponse<ScheduleIdRes> createDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                                   @RequestPart String scheduleId,
                                                   @RequestPart(required = false) String content) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>(scheduleIdRes);
    }

    @ResponseBody
    @GetMapping("/diary/{month}")
    @ApiOperation(value = "스케줄 다이어리 월간 조회")
    public BaseResponse<List<DiaryDto>> findDiaryByMonth(
            @PathVariable("month") String month,
            HttpServletRequest request) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<DiaryDto> diaries = scheduleService.findMonthDiary(userId, localDateTimes);
        return new BaseResponse<>(diaries);
    }

    @ResponseBody
    @GetMapping("/diary/day/{scheduleId}")
    @ApiOperation(value = "스케줄 다이어리 개별 조회")
    public BaseResponse<GetDiaryRes> findDiaryById (
            @PathVariable("scheduleId") Long scheduleId,
            HttpServletRequest request) throws BaseException {
        GetDiaryRes diary = scheduleService.findDiary(scheduleId);
        return new BaseResponse<>(diary);
    }

    @ResponseBody
    @PatchMapping("/diary")
    @ApiOperation(value = "스케줄 다이어리 수정")
    public BaseResponse<String> updateDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                            @RequestPart String scheduleId,
                                            @RequestPart(required = false) String content) throws BaseException {
        scheduleService.deleteDiary(Long.valueOf(scheduleId));
        scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>("수정에 성공하셨습니다.");
    }

    @ResponseBody
    @DeleteMapping("/diary/{scheduleId}")
    @ApiOperation(value = "스케줄 다이어리 삭제")
    public BaseResponse<String> deleteDiary(@PathVariable("scheduleId") Long scheduleId) throws BaseException {
        scheduleService.deleteDiary(scheduleId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }
}
