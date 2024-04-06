package com.example.namo2.domain.schedule.ui;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.domain.schedule.application.ScheduleFacade;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;

import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Schedule", description = "일정 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
	private final ScheduleFacade scheduleFacade;
	private final Converter converter;

	@Operation(summary = "일정 생성", description = "일정 생성 API")
	@PostMapping("")
	public BaseResponse<ScheduleResponse.ScheduleIdDto> createSchedule(
		@Valid @RequestBody ScheduleRequest.PostScheduleDto postScheduleDto,
		HttpServletRequest request
	) {
		ScheduleResponse.ScheduleIdDto scheduleIddto = scheduleFacade.createSchedule(
			postScheduleDto,
			(Long)request.getAttribute("userId")
		);
		return new BaseResponse<>(scheduleIddto);
	}

	@Operation(summary = "일정 다이어리 생성", description = "일정 다이어리 생성 API")
	@PostMapping("/diary")
	public BaseResponse<ScheduleResponse.ScheduleIdDto> createDiary(
		@RequestPart(required = false) List<MultipartFile> imgs,
		@RequestPart String scheduleId,
		@RequestPart(required = false) String content
	) {
		ScheduleResponse.ScheduleIdDto dto = scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return new BaseResponse<>(dto);
	}

	@Operation(summary = "일정 월별 조회", description = "개인 일정 & 모임 일정 월별 조회 API")
	@GetMapping("/{month}")
	public BaseResponse<List<ScheduleResponse.GetScheduleDto>> getSchedulesByUser(
		@PathVariable("month") String month,
		HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getSchedulesByUser(
			(Long)request.getAttribute("userId"), localDateTimes);
		return new BaseResponse<>(userSchedule);
	}

	@Operation(summary = "모임 일정 월별 조회", description = "모임 일정 월별 조회 API")
	@GetMapping("/moim/{month}")
	public BaseResponse<List<ScheduleResponse.GetScheduleDto>> getMoimSchedulesByUser(
		@PathVariable("month") String month,
		HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getMoimSchedulesByUser(
			(Long)request.getAttribute("userId"),
			localDateTimes
		);
		return new BaseResponse<>(userSchedule);
	}

	@Operation(summary = "모든 일정 조회", description = "유저의 모든 개인 일정과 모임 일정 조회 API")
	@GetMapping("/all")
	public BaseResponse<List<ScheduleResponse.GetScheduleDto>> getAllSchedulesByUser(HttpServletRequest request) {
		List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getAllSchedulesByUser(
			(Long)request.getAttribute("userId")
		);
		return new BaseResponse<>(userSchedule);
	}

	@Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정 조회 API")
	@GetMapping("/moim/all")
	public BaseResponse<List<ScheduleResponse.GetScheduleDto>> getAllMoimSchedulesByUser(
		HttpServletRequest request) {
		List<ScheduleResponse.GetScheduleDto> moimSchedule = scheduleFacade.getAllMoimSchedulesByUser(
			(Long)request.getAttribute("userId")
		);
		return new BaseResponse<>(moimSchedule);
	}

	@Operation(summary = "일정 기록 월간 조회", description = "일정 기록 월간 조회 API")
	@GetMapping("/diary/{month}")
	public BaseResponse<ScheduleResponse.SliceDiaryDto> findDiaryByMonth(
		@PathVariable("month") String month,
		Pageable pageable,
		HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		ScheduleResponse.SliceDiaryDto diaries = scheduleFacade.getMonthDiary(userId, localDateTimes, pageable);
		return new BaseResponse<>(diaries);
	}

	//유저별 기록 조회
	@Operation(summary = "개인 일정 기록 전체 조회", description = "개인 일정 기록 전체 조회 API")
	@GetMapping("/diary/all")
	public BaseResponse<List<ScheduleResponse.GetDiaryByUserDto>> findAllDiary(HttpServletRequest request) {
		Long userId = (Long)request.getAttribute("userId");
		List<ScheduleResponse.GetDiaryByUserDto> diaries = scheduleFacade.getAllDiariesByUser(userId);
		return new BaseResponse<>(diaries);
	}

	//일정 별 기록 조회 == 1개 조회
	@Operation(summary = "일정 기록 개별 조회", description = "일정 기록 개별 조회 API")
	@GetMapping("/diary/day/{scheduleId}")
	public BaseResponse<ScheduleResponse.GetDiaryByScheduleDto> findDiaryById(
		@PathVariable("scheduleId") Long scheduleId
	) {
		ScheduleResponse.GetDiaryByScheduleDto diary = scheduleFacade.getDiaryBySchedule(scheduleId);
		return new BaseResponse<>(diary);
	}

	@Operation(summary = "일정 수정", description = "일정 수정 API")
	@PatchMapping("/{scheduleId}")
	public BaseResponse<ScheduleResponse.ScheduleIdDto> modifyUserSchedule(
		HttpServletRequest request,
		@PathVariable("scheduleId") Long scheduleId,
		@RequestBody ScheduleRequest.PostScheduleDto req) {
		ScheduleResponse.ScheduleIdDto dto = scheduleFacade.modifySchedule(
			scheduleId,
			req,
			(Long)request.getAttribute("userId")
		);
		return new BaseResponse<>(dto);
	}

	@Operation(summary = "일정 기록 수정", description = "일정 기록 수정 API")
	@PatchMapping("/diary")
	public BaseResponse<String> updateDiary(
		@RequestPart(required = false) List<MultipartFile> imgs,
		@RequestPart String scheduleId,
		@RequestPart(required = false) String content
	) {
		scheduleFacade.removeDiary(Long.valueOf(scheduleId));
		scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return new BaseResponse<>("수정에 성공하셨습니다.");
	}

	/**
	 * kind 0 은 개인 일정
	 * kind 1 은 모임 일정
	 */
	@Operation(summary = "일정 삭제", description = "개인 캘린더에서 개인 혹은 모임 일정 삭제 API")
	@DeleteMapping("/{scheduleId}/{kind}")
	public BaseResponse<String> deleteUserSchedule(
		@PathVariable("scheduleId") Long scheduleId,
		@PathVariable("kind") Integer kind,
		HttpServletRequest request
	) {
		scheduleFacade.removeSchedule(scheduleId, kind, (Long)request.getAttribute("userId"));
		return new BaseResponse<>("삭제에 성공하였습니다.");
	}

	@Operation(summary = "일정 다이어리 삭제", description = "일정 다이어리 삭제 API")
	@DeleteMapping("/diary/{scheduleId}")
	public BaseResponse<String> deleteDiary(@PathVariable("scheduleId") Long scheduleId) {
		scheduleFacade.removeDiary(scheduleId);
		return new BaseResponse<>("삭제에 성공하셨습니다.");
	}
}
