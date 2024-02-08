package com.example.namo2.domain.schedule;

import com.example.namo2.domain.schedule.dto.*;
import com.example.namo2.global.config.exception.BaseException;
import com.example.namo2.global.config.response.BaseResponse;
import com.example.namo2.global.utils.Converter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Schedule", description = "스케줄 관련 API")
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

    @Operation(summary = "스케줄 생성", description = "스케줄 생성 API")
    @PostMapping("")
    public BaseResponse<ScheduleIdRes> createSchedule(@Valid @RequestBody PostScheduleReq postScheduleReq, HttpServletRequest request) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.createSchedule(postScheduleReq, (Long) request.getAttribute("userId"));
        return new BaseResponse<>(scheduleIdRes);
    }

    @Operation(summary = "스케줄 월별 조회", description = "스케줄 월별 조회 API")
    @GetMapping("/{month}")
    public BaseResponse<List<GetScheduleRes>> findUserSchedule(@PathVariable("month") String month, HttpServletRequest request) throws BaseException {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<GetScheduleRes> userSchedule = scheduleService.findUsersSchedule((Long) request.getAttribute("userId"), localDateTimes);
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모임 스케줄 월별 조회", description = "모임 스케줄 월별 조회 API")
    @GetMapping("/moim/{month}")
    public BaseResponse<List<GetScheduleRes>> findUserMoimSchedule(@PathVariable("month") String month, HttpServletRequest request) throws BaseException {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<GetScheduleRes> userSchedule = scheduleService.findUsersMoimSchedule((Long) request.getAttribute("userId"), localDateTimes);
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모든 스케줄 조회", description = "모든 스케줄 조회 API")
    @GetMapping("/all")
    public BaseResponse<List<GetScheduleRes>> findUserALLSchedule(HttpServletRequest request) throws BaseException {
        List<GetScheduleRes> userSchedule = scheduleService.findUsersALLSchedule((Long) request.getAttribute("userId"));
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모든 모임 스케줄 조회", description = "모든 모임 스케줄 조회 API")
    @GetMapping("/moim/all")
    public BaseResponse<List<GetScheduleRes>> findMoimALLSchedule(HttpServletRequest request) throws BaseException {
        List<GetScheduleRes> moimSchedule = scheduleService.findMoimALLSchedule((Long) request.getAttribute("userId"));
        return new BaseResponse<>(moimSchedule);
    }

    @Operation(summary = "스케줄 수정", description = "스케줄 수정 API")
    @PatchMapping("/{schedule}")
    public BaseResponse<ScheduleIdRes> updateUserSchedule(
            @PathVariable("schedule") Long scheduleId,
            @RequestBody PostScheduleReq postScheduleReq) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.updateSchedule(scheduleId, postScheduleReq);
        return new BaseResponse<>(scheduleIdRes);
    }

    /**
     * kind 0 은 개인 스케줄
     * kind 1 은 모임 스케줄
     */
    @Operation(summary = "스케줄 삭제", description = "스케줄 삭제 API")
    @DeleteMapping("/{schedule}/{kind}")
    public BaseResponse<String> deleteUserSchedule(@PathVariable("schedule") Long scheduleId,
                                                   @PathVariable("kind") Integer kind,
                                                   HttpServletRequest request) throws BaseException {
        scheduleService.deleteSchedule(scheduleId, kind, (Long) request.getAttribute("userId"));
        return new BaseResponse<>("삭제에 성공하였습니다.");
    }

    @Operation(summary = "스케줄 다이어리 생성", description = "스케줄 다이어리 생성 API")
    @PostMapping("/diary")
    public BaseResponse<ScheduleIdRes> createDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                                   @RequestPart String scheduleId,
                                                   @RequestPart(required = false) String content) throws BaseException {
        ScheduleIdRes scheduleIdRes = scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>(scheduleIdRes);
    }

    @Operation(summary = "스케줄 다이어리 월간 조회", description = "스케줄 다이어리 월간 조회 API")
    @GetMapping("/diary/{month}")
    public BaseResponse<SliceDiaryDto> findDiaryByMonth(
            @PathVariable("month") String month, Pageable pageable,
            HttpServletRequest request) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        SliceDiaryDto diaries = scheduleService.findMonthDiary(userId, localDateTimes, pageable);
        return new BaseResponse<>(diaries);
    }

    @Operation(summary = "개인 스케줄 다이어리 전체 조회", description = "개인 스케줄 다이어리 전체 조회 API")
    @GetMapping("/diary/all")
    public BaseResponse<List<OnlyDiaryDto>> findALLDiary(HttpServletRequest request) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        List<OnlyDiaryDto> diaries = scheduleService.findAllDiary(userId);
        return new BaseResponse<>(diaries);
    }

    @Operation(summary = "스케줄 다이어리 개별 조회", description = "스케줄 다이어리 개별 조회 API")
    @GetMapping("/diary/day/{scheduleId}")
    public BaseResponse<GetDiaryRes> findDiaryById (
            @PathVariable("scheduleId") Long scheduleId,
            HttpServletRequest request) throws BaseException {
        GetDiaryRes diary = scheduleService.findDiary(scheduleId);
        return new BaseResponse<>(diary);
    }

    @Operation(summary="스케줄 다이어리 수정", description = "스케줄 다이어리 수정 API")
    @PatchMapping("/diary")
    public BaseResponse<String> updateDiary(@RequestPart(required = false) List<MultipartFile> imgs,
                                            @RequestPart String scheduleId,
                                            @RequestPart(required = false) String content) throws BaseException {
        scheduleService.deleteDiary(Long.valueOf(scheduleId));
        scheduleService.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>("수정에 성공하셨습니다.");
    }

    @Operation(summary = "스케줄 다이어리 삭제", description = "스케줄 다이어리 삭제 API")
    @DeleteMapping("/diary/{scheduleId}")
    public BaseResponse<String> deleteDiary(@PathVariable("scheduleId") Long scheduleId) throws BaseException {
        scheduleService.deleteDiary(scheduleId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }
}
