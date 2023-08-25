package com.example.namo2.moim;

import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.LocationInfo;
import com.example.namo2.moim.dto.MoimMemoDto;
import com.example.namo2.moim.dto.MoimScheduleAlarmDto;
import com.example.namo2.moim.dto.PatchMoimName;
import com.example.namo2.moim.dto.PatchMoimScheduleReq;
import com.example.namo2.moim.dto.PostMoimRes;
import com.example.namo2.moim.dto.PostMoimScheduleReq;
import com.example.namo2.moim.dto.MoimScheduleRes;
import com.example.namo2.utils.Converter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/moims")
@Api(value = "Moims")
public class MoimController {
    private final MoimService moimService;
    private final MoimMemoService moimMemoService;
    private final Converter converter;

    @PostMapping("")
    @ApiOperation(value = "모임 생성")
    public BaseResponse<PostMoimRes> createMoim(@RequestPart MultipartFile img,
                                                @RequestPart String groupName,
                                                HttpServletRequest request) {
        Long moimId = moimService.create((Long) request.getAttribute("userId"), groupName, img);
        return new BaseResponse(new PostMoimRes(moimId));
    }

    @GetMapping("")
    @ApiOperation(value = "모임 리스트 조회")
    public BaseResponse<List<GetMoimRes>> findMoimList(HttpServletRequest request) {
        List<GetMoimRes> moims = moimService.findMoims((Long) request.getAttribute("userId"));
        return new BaseResponse(moims);
    }

    @PatchMapping("/name")
    @ApiOperation(value = "모임 이름 수정하기(자신만 바뀜)")
    public BaseResponse<Long> updateName(@RequestBody PatchMoimName patchMoimName, HttpServletRequest request) {
        Long moimId = moimService.patchMoimName(patchMoimName, (Long) request.getAttribute("userId"));
        return new BaseResponse(moimId);
    }

    @PatchMapping("/participate/{code}")
    @ApiOperation(value = "모임 참여하기")
    public BaseResponse<Long> updateName(@PathVariable("code") String code, HttpServletRequest request) {
        Long moimId = moimService.participate((Long) request.getAttribute("userId"), code);
        return new BaseResponse(moimId);
    }

    @DeleteMapping("/withdraw/{moimId}")
    @ApiOperation(value = "모임 탈퇴하기")
    public BaseResponse withdraw(@PathVariable("moimId") Long moimId, HttpServletRequest request) {
        moimService.withdraw((Long) request.getAttribute("userId"), moimId);
        return BaseResponse.ok();
    }

    @PostMapping("/schedule")
    @ApiOperation(value = "모임 스케줄 생성")
    public BaseResponse<Long> createMoimSchedule(@Valid @RequestBody PostMoimScheduleReq scheduleReq) {
        Long scheduleId = moimService.createSchedule(scheduleReq);
        return new BaseResponse(scheduleId);
    }

    @PatchMapping("/schedule")
    @ApiOperation(value = "모임 스케줄 수정")
    public BaseResponse<Long> updateMoimSchedule(@Valid @RequestBody PatchMoimScheduleReq scheduleReq) {
        moimService.updateSchedule(scheduleReq);
        return BaseResponse.ok();
    }

    @DeleteMapping("/schedule/{moimScheduleId}")
    @ApiOperation(value = "모임 스케줄 삭제")
    public BaseResponse<Long> deleteMoimSchedule(@PathVariable Long moimScheduleId) {
        moimService.deleteSchedule(moimScheduleId);
        return BaseResponse.ok();
    }

    @GetMapping("/schedule/{moimId}/{month}")
    @ApiOperation(value = "모임 스케줄 조회")
    public BaseResponse<MoimScheduleRes> findMoimSchedules(@PathVariable("moimId") Long moimId,
                                                           @PathVariable("month") String month) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<MoimScheduleRes> schedules = moimService.findMoimSchedules(moimId, localDateTimes);
        return new BaseResponse(schedules);
    }

    @PostMapping("/schedule/alarm")
    @ApiOperation(value = "모임 스케줄 알람 생성")
    public BaseResponse createMoimScheduleAlarm(@Valid @RequestBody MoimScheduleAlarmDto moimScheduleAlarmDto) {
        moimService.createScheduleAlarm(moimScheduleAlarmDto);
        return BaseResponse.ok();
    }

    @PostMapping("/schedule/memo/{moimScheduleId}")
    @ApiOperation(value = "모임 메모 장소 생성")
    public BaseResponse<Object> findMoimMemo(@RequestPart(required = false) List<MultipartFile> imgs,
                                             @PathVariable Long moimScheduleId,
                                             @RequestPart(required = true) String name,
                                             @RequestPart(required = true) String money,
                                             @RequestPart(required = true) String participants) {
        LocationInfo locationInfo = new LocationInfo(name, money, participants);
        moimMemoService.create(moimScheduleId, locationInfo, imgs);
        return BaseResponse.ok();
    }

    @PatchMapping("/schedule/memo/{memoLocationId}")
    @ApiOperation(value = "모임 메모 장소 수정")
    public BaseResponse<Object> updateMoimMemo(@RequestPart(required = false) List<MultipartFile> imgs,
                                               @PathVariable Long memoLocationId,
                                               @RequestPart(required = true) String name,
                                               @RequestPart(required = true) String money,
                                               @RequestPart(required = true) String participants) {
        LocationInfo locationInfo = new LocationInfo(name, money, participants);
        moimMemoService.update(memoLocationId, locationInfo, imgs);
        return BaseResponse.ok();
    }

    @GetMapping("/schedule/memo/{moimScheduleId}")
    @ApiOperation(value = "모임 메모 조회")
    public BaseResponse<Object> findMoimMemo(@PathVariable("moimScheduleId") Long moimScheduleId) {
        MoimMemoDto moimMemoDto = moimMemoService.find(moimScheduleId);
        return new BaseResponse(moimMemoDto);
    }

    @DeleteMapping("/schedule/memo/{memoLocationId}")
    @ApiOperation(value = "모임 메모 장소 삭제")
    public BaseResponse<Object> deleteMoimMemo(@PathVariable Long memoLocationId) {
        moimMemoService.delete(memoLocationId);
        return BaseResponse.ok();
    }
}
