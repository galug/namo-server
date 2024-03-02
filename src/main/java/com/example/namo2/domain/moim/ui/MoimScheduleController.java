package com.example.namo2.domain.moim.ui;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.domain.moim.application.MoimScheduleFacade;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;

import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Moim", description = "모임 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/moims/schedule")
public class MoimScheduleController {
	private final MoimScheduleFacade moimScheduleFacade;
	private final Converter converter;

	@Operation(summary = "모임 스케쥴 생성", description = "모임 스케쥴 생성 API")
	@PostMapping("")
	public BaseResponse<Long> createMoimSchedule(
		@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleDto scheduleReq) {
		Long scheduleId = moimScheduleFacade.createSchedule(scheduleReq);
		return new BaseResponse(scheduleId);
	}

	@Operation(summary = "모임 스케쥴 수정", description = "모임 스케쥴 수정 API")
	@PatchMapping("")
	public BaseResponse<Long> modifyMoimSchedule(
		@Valid @RequestBody MoimScheduleRequest.PatchMoimScheduleDto scheduleReq) {
		moimScheduleFacade.modifyMoimSchedule(scheduleReq);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 스케쥴 카테고리 수정", description = "모임 스케쥴 카테고리 수정 API")
	@PatchMapping("category")
	public BaseResponse<Long> modifyMoimScheduleCategory(
		@Valid @RequestBody MoimScheduleRequest.PatchMoimScheduleCategoryDto scheduleReq, HttpServletRequest request) {
		moimScheduleFacade.modifyMoimScheduleCategory(scheduleReq, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 스케쥴 삭제", description = "모임 스케쥴 삭제 API")
	@DeleteMapping("/{moimScheduleId}")
	public BaseResponse<Long> removeMoimSchedule(@PathVariable Long moimScheduleId) {
		moimScheduleFacade.removeMoimSchedule(moimScheduleId);
		return BaseResponse.ok();
	}

	@Operation(summary = "월간 모임 스케쥴 조회", description = "월간 모임 스케쥴 조회 API")
	@GetMapping("/{moimId}/{month}")
	public BaseResponse<MoimScheduleResponse.MoimScheduleDto> getMoimSchedules(@PathVariable("moimId") Long moimId,
		@PathVariable("month") String month) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		List<MoimScheduleResponse.MoimScheduleDto> schedules = moimScheduleFacade.getMoimSchedules(moimId,
			localDateTimes);
		return new BaseResponse(schedules);
	}

	@Operation(summary = "모임 스케쥴 생성 알람", description = "모임 스케쥴 생성 알람 API")
	@PostMapping("/alarm")
	public BaseResponse createMoimScheduleAlarm(
		@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleAlarmDto postMoimScheduleAlarmDto,
		HttpServletRequest request) {
		moimScheduleFacade.createMoimScheduleAlarm(postMoimScheduleAlarmDto, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 스케쥴 변경 알람", description = "모임 스케쥴 변경 알람 API")
	@PatchMapping("/alarm")
	public BaseResponse modifyMoimScheduleAlarm(
		@Valid @RequestBody MoimScheduleRequest.PostMoimScheduleAlarmDto postMoimScheduleAlarmDto,
		HttpServletRequest request) {
		moimScheduleFacade.modifyMoimScheduleAlarm(postMoimScheduleAlarmDto, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}
}
