package com.example.namo2.domain.schedule.ui;

import java.time.LocalDateTime;
import java.util.List;

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

import com.example.namo2.domain.schedule.application.ScheduleFacade;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.Converter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "4. Diary (개인)", description = "개인 기록 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {
	private final ScheduleFacade scheduleFacade;
	private final Converter converter;

	@Operation(summary = "기록 생성", description = "기록 생성 API")
	@PostMapping("")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<ScheduleResponse.ScheduleIdDto> createDiary(
			@RequestPart(required = false) List<MultipartFile> imgs,
			@RequestPart String scheduleId,
			@RequestPart(required = false) String content
	) {
		ScheduleResponse.ScheduleIdDto dto = scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return new BaseResponse<>(dto);
	}

	@Operation(summary = "일정 기록 월간 조회", description = "일정 기록 월간 조회 API")
	@GetMapping("/{month}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
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

	@Operation(summary = "개인 일정 기록 전체 조회", description = "개인 일정 기록 전체 조회 API")
	@GetMapping("/all")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<List<ScheduleResponse.GetDiaryByUserDto>> findAllDiary(
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		List<ScheduleResponse.GetDiaryByUserDto> diaries = scheduleFacade.getAllDiariesByUser(userId);
		return new BaseResponse<>(diaries);
	}

	//일정 별 기록 조회 == 1개 조회
	@Operation(summary = "일정 기록 개별 조회", description = "일정 기록 개별 조회 API")
	@GetMapping("/{scheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<ScheduleResponse.GetDiaryByScheduleDto> findDiaryById(
			@PathVariable("scheduleId") Long scheduleId
	) {
		ScheduleResponse.GetDiaryByScheduleDto diary = scheduleFacade.getDiaryBySchedule(scheduleId);
		return new BaseResponse<>(diary);
	}

	@Operation(summary = "일정 기록 수정", description = "일정 기록 수정 API")
	@PatchMapping("")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<String> updateDiary(
			@RequestPart(required = false) List<MultipartFile> imgs,
			@RequestPart String scheduleId,
			@RequestPart(required = false) String content
	) {
		scheduleFacade.removeDiary(Long.valueOf(scheduleId));
		scheduleFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return new BaseResponse<>("수정에 성공하셨습니다.");
	}

	@Operation(summary = "일정 기록 삭제", description = "일정 기록 삭제 API")
	@DeleteMapping("/{scheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<String> deleteDiary(
			@PathVariable("scheduleId") Long scheduleId
	) {
		scheduleFacade.removeDiary(scheduleId);
		return new BaseResponse<>("삭제에 성공하셨습니다.");
	}
}
