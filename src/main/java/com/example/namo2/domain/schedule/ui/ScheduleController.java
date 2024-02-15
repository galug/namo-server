package com.example.namo2.domain.schedule.ui;

import com.example.namo2.domain.schedule.application.ScheduleFacade;
import com.example.namo2.domain.schedule.application.impl.ScheduleService;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.utils.Converter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Schedule", description = "스케줄 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleFacade scheduleFacade;
    private final Converter converter;

    @Operation(summary = "스케줄 생성", description = "스케줄 생성 API")
    @PostMapping("")
    public BaseResponse<ScheduleResponse.ScheduleIdRes> createSchedule(
        @Valid @RequestBody ScheduleRequest.PostScheduleReq postScheduleReq
        , HttpServletRequest request
    ) throws BaseException {
        ScheduleResponse.ScheduleIdRes scheduleIdRes = scheduleFacade.createSchedule(
            postScheduleReq
            , (Long) request.getAttribute("userId")
        );
        return new BaseResponse<>(scheduleIdRes);
    }

    @Operation(summary = "스케줄 다이어리 생성", description = "스케줄 다이어리 생성 API")
    @PostMapping("/diary")
    public BaseResponse<ScheduleResponse.ScheduleIdRes> createDiary(
        @RequestPart(required = false) List<MultipartFile> imgs,
        @RequestPart String scheduleId,
        @RequestPart(required = false) String content
    ) throws BaseException {
        ScheduleResponse.ScheduleIdRes scheduleIdRes = scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>(scheduleIdRes);
    }

    @Operation(summary = "스케줄 월별 조회", description = "스케줄 월별 조회 API")
    @GetMapping("/{month}")
    public BaseResponse<List<ScheduleResponse.GetScheduleRes>> getSchedulesByUser(
        @PathVariable("month") String month,
        HttpServletRequest request
    ) throws BaseException {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<ScheduleResponse.GetScheduleRes> userSchedule = scheduleFacade.getSchedulesByUser((Long) request.getAttribute("userId"), localDateTimes);
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모임 스케줄 월별 조회", description = "모임 스케줄 월별 조회 API")
    @GetMapping("/moim/{month}")
    public BaseResponse<List<ScheduleResponse.GetScheduleRes>> getMoimSchedulesByUser(
        @PathVariable("month") String month
        , HttpServletRequest request
    ) throws BaseException {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<ScheduleResponse.GetScheduleRes> userSchedule = scheduleFacade.getMoimSchedulesByUser(
            (Long) request.getAttribute("userId"),
            localDateTimes
        );
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모든 스케줄 조회", description = "모든 스케줄 조회 API")
    @GetMapping("/all")
    public BaseResponse<List<ScheduleResponse.GetScheduleRes>> getAllSchedulesByUser(HttpServletRequest request) throws BaseException {
        List<ScheduleResponse.GetScheduleRes> userSchedule = scheduleFacade.getAllSchedulesByUser(
            (Long) request.getAttribute("userId")
        );
        return new BaseResponse<>(userSchedule);
    }

    @Operation(summary = "모든 모임 스케줄 조회", description = "모든 모임 스케줄 조회 API")
    @GetMapping("/moim/all")
    public BaseResponse<List<ScheduleResponse.GetScheduleRes>> getAllMoimSchedulesByUser(HttpServletRequest request) throws BaseException {
        List<ScheduleResponse.GetScheduleRes> moimSchedule = scheduleFacade.getAllMoimSchedulesByUser(
            (Long) request.getAttribute("userId")
        );
        return new BaseResponse<>(moimSchedule);
    }

    @Operation(summary = "스케줄 다이어리 월간 조회", description = "스케줄 다이어리 월간 조회 API")
    @GetMapping("/diary/{month}")
    public BaseResponse<ScheduleResponse.SliceDiaryDto> findDiaryByMonth(
        @PathVariable("month") String month,
        Pageable pageable,
        HttpServletRequest request
    ) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        ScheduleResponse.SliceDiaryDto diaries = scheduleFacade.getMonthDiary(userId, localDateTimes, pageable);
        return new BaseResponse<>(diaries);
    }

    //유저별 다이어리 조회
    @Operation(summary = "개인 스케줄 다이어리 전체 조회", description = "개인 스케줄 다이어리 전체 조회 API")
    @GetMapping("/diary/all")
    public BaseResponse<List<ScheduleResponse.GetDiaryByUserRes>> findALLDiary(HttpServletRequest request) throws BaseException {
        Long userId = (Long) request.getAttribute("userId");
        List<ScheduleResponse.GetDiaryByUserRes> diaries = scheduleFacade.getAllDiariesByUser(userId);
        return new BaseResponse<>(diaries);
    }

    //스케줄 별 다이어리 조회 == 1개 조회
    @Operation(summary = "스케줄 다이어리 개별 조회", description = "스케줄 다이어리 개별 조회 API")
    @GetMapping("/diary/day/{scheduleId}")
    public BaseResponse<ScheduleResponse.GetDiaryByScheduleRes> findDiaryById (
        @PathVariable("scheduleId") Long scheduleId,
        HttpServletRequest request
    ) throws BaseException {
        ScheduleResponse.GetDiaryByScheduleRes diary = scheduleFacade.getDiaryBySchedule(scheduleId);
        return new BaseResponse<>(diary);
    }

    @Operation(summary = "스케줄 수정", description = "스케줄 수정 API")
    @PatchMapping("/{schedule}")
    public BaseResponse<ScheduleResponse.ScheduleIdRes> modifyUserSchedule(
            @PathVariable("schedule") Long scheduleId,
            @RequestBody ScheduleRequest.PostScheduleReq postScheduleReq) throws BaseException {
        ScheduleResponse.ScheduleIdRes scheduleIdRes = scheduleFacade.modifySchedule(scheduleId, postScheduleReq);
        return new BaseResponse<>(scheduleIdRes);
    }

    @Operation(summary="스케줄 다이어리 수정", description = "스케줄 다이어리 수정 API")
    @PatchMapping("/diary")
    public BaseResponse<String> updateDiary(
        @RequestPart(required = false) List<MultipartFile> imgs,
        @RequestPart String scheduleId,
        @RequestPart(required = false) String content
    ) throws BaseException {
        scheduleFacade.removeDiary(Long.valueOf(scheduleId));
        scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
        return new BaseResponse<>("수정에 성공하셨습니다.");
    }

    /**
     * kind 0 은 개인 스케줄
     * kind 1 은 모임 스케줄
     */
    @Operation(summary = "스케줄 삭제", description = "스케줄 삭제 API")
    @DeleteMapping("/{schedule}/{kind}")
    public BaseResponse<String> deleteUserSchedule(
        @PathVariable("schedule") Long scheduleId,
        @PathVariable("kind") Integer kind,
        HttpServletRequest request
    ) throws BaseException {
        scheduleFacade.removeSchedule(scheduleId, kind, (Long) request.getAttribute("userId"));
        return new BaseResponse<>("삭제에 성공하였습니다.");
    }

    @Operation(summary = "스케줄 다이어리 삭제", description = "스케줄 다이어리 삭제 API")
    @DeleteMapping("/diary/{scheduleId}")
    public BaseResponse<String> deleteDiary(@PathVariable("scheduleId") Long scheduleId) throws BaseException {
        scheduleFacade.removeDiary(scheduleId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }
}
