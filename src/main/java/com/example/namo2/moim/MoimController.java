package com.example.namo2.moim;

import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.moim.dto.*;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import com.example.namo2.utils.Converter;
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
    private final MoimService moimService;
    private final MoimMemoService moimMemoService;
    private final Converter converter;

    @Operation(summary = "모임 생성", description = "모임 생성 API")
    @PostMapping("")
    public BaseResponse<PostMoimRes> createMoim(@RequestPart MultipartFile img,
                                                @RequestPart String groupName,
                                                HttpServletRequest request) {
        Long moimId = moimService.create((Long) request.getAttribute("userId"), groupName, img);
        return new BaseResponse(new PostMoimRes(moimId));
    }

    @Operation(summary = "모임 조회", description = "모임 조회 API")
    @GetMapping("")
    public BaseResponse<List<GetMoimRes>> findMoimList(HttpServletRequest request) {
        List<GetMoimRes> moims = moimService.findMoims((Long) request.getAttribute("userId"));
        return new BaseResponse(moims);
    }

    @Operation(summary = "모임 이름 변경", description = "모임 이름 변경 API, 변경자 입장에서만 적용")
    @PatchMapping("/name")
    public BaseResponse<Long> updateName(@RequestBody PatchMoimName patchMoimName, HttpServletRequest request) {
        Long moimId = moimService.patchMoimName(patchMoimName, (Long) request.getAttribute("userId"));
        return new BaseResponse(moimId);
    }

    @Operation(summary = "모임 참여", description = "모임 참여 API")
    @PatchMapping("/participate/{code}")
    public BaseResponse<Long> updateName(@PathVariable("code") String code, HttpServletRequest request) {
        Long moimId = moimService.participate((Long) request.getAttribute("userId"), code);
        return new BaseResponse(moimId);
    }

    @Operation(summary = "모임 탈퇴", description = "모임 탈퇴 API")
    @DeleteMapping("/withdraw/{moimId}")
    public BaseResponse withdraw(@PathVariable("moimId") Long moimId, HttpServletRequest request) {
        moimService.withdraw((Long) request.getAttribute("userId"), moimId);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 생성", description = "모임 스케쥴 생성 API")
    @PostMapping("/schedule")
    public BaseResponse<Long> createMoimSchedule(@Valid @RequestBody PostMoimScheduleReq scheduleReq) {
        Long scheduleId = moimService.createSchedule(scheduleReq);
        return new BaseResponse(scheduleId);
    }

    @Operation(summary = "모임 스케쥴 생성", description = "모임 스케쥴 생성 API")
    @PatchMapping("/schedule")
    public BaseResponse<Long> updateMoimSchedule(@Valid @RequestBody PatchMoimScheduleReq scheduleReq) {
        moimService.updateSchedule(scheduleReq);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 카테고리 수정", description = "모임 스케쥴 카테고리 수정 API")
    @PatchMapping("/schedule/category")
    public BaseResponse<Long> updateMoimScheduleCategory(@Valid @RequestBody PatchMoimScheduleCategoryReq scheduleReq, HttpServletRequest request) {
        moimService.updateScheduleCategory(scheduleReq, (Long) request.getAttribute("userId"));
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 삭제", description = "모임 스케쥴 삭제 API")
    @DeleteMapping("/schedule/{moimScheduleId}")
    public BaseResponse<Long> deleteMoimSchedule(@PathVariable Long moimScheduleId) {
        moimService.deleteSchedule(moimScheduleId);
        return BaseResponse.ok();
    }

    @Operation(summary = "월간 모임 스케쥴 조회", description = "월간 모임 스케쥴 조회 API")
    @GetMapping("/schedule/{moimId}/{month}")
    public BaseResponse<MoimScheduleRes> findMoimSchedules(@PathVariable("moimId") Long moimId,
                                                           @PathVariable("month") String month) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<MoimScheduleRes> schedules = moimService.findMoimSchedules(moimId, localDateTimes);
        return new BaseResponse(schedules);
    }

    @Operation(summary = "모임 스케쥴 생성 알람", description = "모임 스케쥴 생성 알람 API")
    @PostMapping("/schedule/alarm")
    public BaseResponse createMoimScheduleAlarm(@Valid @RequestBody MoimScheduleAlarmDto moimScheduleAlarmDto) {
        moimService.createScheduleAlarm(moimScheduleAlarmDto);
        return BaseResponse.ok();
    }

    @Operation(summary = "모임 스케쥴 변경 알람", description = "모임 스케쥴 변경 알람 API")
    @PatchMapping("/schedule/alarm")
    public BaseResponse updateMoimScheduleAlarm(@Valid @RequestBody MoimScheduleAlarmDto moimScheduleAlarmDto) {
        moimService.updateScheduleAlarm(moimScheduleAlarmDto);
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
    public BaseResponse<Object> createMoimScheduleText(@PathVariable Long moimScheduleId, HttpServletRequest request, @RequestBody PostMoimScheduleText moimScheduleText) {
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
