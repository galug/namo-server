package com.example.namo2.domain.moim.ui;

import com.example.namo2.domain.memo.MoimMemoService;
import com.example.namo2.domain.moim.application.MoimFacade;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.moim.ui.dto.LocationInfo;
import com.example.namo2.domain.moim.ui.dto.MoimMemoDto;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleDto;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
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

@Tag(name = "Moim", description = "모임 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/moims")
public class MoimController {
    private final MoimFacade moimFacade;
    private final MoimService moimService;
    private final MoimMemoService moimMemoService;
    private final Converter converter;

    @Operation(summary = "모임 생성", description = "모임 생성 API")
    @PostMapping("")
    public BaseResponse<MoimResponse.MoimIdDto> createMoim(@RequestPart MultipartFile img,
                                                           @RequestPart String groupName,
                                                           HttpServletRequest request) {
        MoimResponse.MoimIdDto moimIdDto = moimFacade.createMoim((Long) request.getAttribute("userId"), groupName, img);
        return new BaseResponse(moimIdDto);
    }

    @Operation(summary = "모임 조회", description = "모임 조회 API")
    @GetMapping("")
    public BaseResponse<List<MoimResponse.MoimDto>> findMoims(HttpServletRequest request) {
        List<MoimResponse.MoimDto> moims = moimFacade.getMoims((Long) request.getAttribute("userId"));
        return new BaseResponse(moims);
    }

    @Operation(summary = "모임 이름 변경", description = "모임 이름 변경 API, 변경자 입장에서만 적용")
    @PatchMapping("/name")
    public BaseResponse<Long> modifyMoimName(@RequestBody MoimRequest.PatchMoimNameDto patchMoimNameDto,
                                             HttpServletRequest request) {
        Long moimId = moimFacade.modifyMoimName(patchMoimNameDto, (Long) request.getAttribute("userId"));
        return new BaseResponse(moimId);
    }

    @Operation(summary = "모임 참여", description = "모임 참여 API")
    @PatchMapping("/participate/{code}")
    public BaseResponse<Long> createMoimAndUser(@PathVariable("code") String code, HttpServletRequest request) {
        Long moimId = moimService.participate((Long) request.getAttribute("userId"), code);
        return new BaseResponse(moimId);
    }

    @Operation(summary = "모임 탈퇴", description = "모임 탈퇴 API")
    @DeleteMapping("/withdraw/{moimId}")
    public BaseResponse removeMoimAndUser(@PathVariable("moimId") Long moimId, HttpServletRequest request) {
        moimService.withdraw((Long) request.getAttribute("userId"), moimId);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 생성", description = "모임 스케쥴 생성 API")
    @PostMapping("/schedule")
    public BaseResponse<Long> createMoimSchedule(@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleDto scheduleReq) {
        Long scheduleId = moimService.createSchedule(scheduleReq);
        return new BaseResponse(scheduleId);
    }

    @Operation(summary = "모임 스케쥴 생성", description = "모임 스케쥴 생성 API")
    @PatchMapping("/schedule")
    public BaseResponse<Long> modifyMoimSchedule(@Valid @RequestBody MoimScheduleRequest.PatchMoimScheduleDto scheduleReq) {
        moimService.updateSchedule(scheduleReq);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 카테고리 수정", description = "모임 스케쥴 카테고리 수정 API")
    @PatchMapping("/schedule/category")
    public BaseResponse<Long> modifyMoimScheduleCategory(@Valid @RequestBody MoimScheduleRequest.PatchMoimScheduleCategoryDto scheduleReq, HttpServletRequest request) {
        moimService.updateScheduleCategory(scheduleReq, (Long) request.getAttribute("userId"));
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 삭제", description = "모임 스케쥴 삭제 API")
    @DeleteMapping("/schedule/{moimScheduleId}")
    public BaseResponse<Long> removeMoimSchedule(@PathVariable Long moimScheduleId) {
        moimService.deleteSchedule(moimScheduleId);
        return BaseResponse.ok();
    }

    @Operation(summary = "월간 모임 스케쥴 조회", description = "월간 모임 스케쥴 조회 API")
    @GetMapping("/schedule/{moimId}/{month}")
    public BaseResponse<MoimScheduleDto> getMoimSchedules(@PathVariable("moimId") Long moimId,
                                                          @PathVariable("month") String month) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<MoimScheduleDto> schedules = moimService.findMoimSchedules(moimId, localDateTimes);
        return new BaseResponse(schedules);
    }

    @Operation(summary = "모임 스케쥴 생성 알람", description = "모임 스케쥴 생성 알람 API")
    @PostMapping("/schedule/alarm")
    public BaseResponse createMoimScheduleAlarm(@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleAlarmDto postMoimScheduleAlarmDto) {
        moimService.createScheduleAlarm(postMoimScheduleAlarmDto);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 변경 알람", description = "모임 스케쥴 변경 알람 API")
    @PatchMapping("/schedule/alarm")
    public BaseResponse updateMoimScheduleAlarm(@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleAlarmDto postMoimScheduleAlarmDto) {
        moimService.updateScheduleAlarm(postMoimScheduleAlarmDto);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 메모 생성", description = "모임 모임 매모 생성 API")
    @PostMapping("/schedule/memo/{moimScheduleId}")
    public BaseResponse<Object> createMoimMemo(@RequestPart(required = false) List<MultipartFile> imgs,
                                               @PathVariable Long moimScheduleId,
                                               @RequestPart(required = true) String name,
                                               @RequestParam(defaultValue = "0") String money,
                                               @RequestPart(required = true) String participants) {
        LocationInfo locationInfo = new LocationInfo(name, money, participants);
        moimMemoService.create(moimScheduleId, locationInfo, imgs);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 메모 텍스트 추가", description = "모임 메모 텍스트 추가 API")
    @PatchMapping("/schedule/memo/text/{moimScheduleId}")
    public BaseResponse<Object> createMoimScheduleText(@PathVariable Long moimScheduleId,
                                                       HttpServletRequest request,
                                                       @RequestBody MoimScheduleRequest.PostMoimScheduleTextDto moimScheduleText) {
        moimService.createMoimScheduleText(moimScheduleId, (Long) request.getAttribute("userId"), moimScheduleText);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 메모 장소 수정", description = "모임 메모 장소 수정 API")
    @PatchMapping("/schedule/memo/{memoLocationId}")
    public BaseResponse<Object> updateMoimMemo(@RequestPart(required = false) List<MultipartFile> imgs,
                                               @PathVariable Long memoLocationId,
                                               @RequestPart(required = true) String name,
                                               @RequestPart(required = true) String money,
                                               @RequestPart(required = true) String participants) {
        LocationInfo locationInfo = new LocationInfo(name, money, participants);
        moimMemoService.update(memoLocationId, locationInfo, imgs);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 메모 조회", description = "모임 메모 조회 API")
    @GetMapping("/schedule/memo/{moimScheduleId}")
    public BaseResponse<Object> findMoimMemo(@PathVariable("moimScheduleId") Long moimScheduleId) {
        MoimMemoDto moimMemoDto = moimMemoService.find(moimScheduleId);
        return new BaseResponse(moimMemoDto);
    }

    @Operation(summary = "월간 모임 메모 조회", description = "월간 모임 메모 조회 API")
    @GetMapping("/schedule/memo/month/{month}")
    public BaseResponse<SliceDiaryDto<DiaryDto>> findMonthMoimMemo(@PathVariable("month") String month, Pageable pageable, HttpServletRequest request) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        SliceDiaryDto<DiaryDto> diaryDto = moimMemoService.findMonth((Long) request.getAttribute("userId"), localDateTimes, pageable);
        return new BaseResponse(diaryDto);
    }

    @Operation(summary = "모임 메모 삭제", description = "모임 메모 삭제 API")
    @DeleteMapping("/schedule/memo/{memoLocationId}")
    public BaseResponse<Object> deleteMoimMemo(@PathVariable Long memoLocationId) {
        moimMemoService.delete(memoLocationId);
        return BaseResponse.ok();
    }
}
