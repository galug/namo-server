package com.example.namo2.domain.group.ui;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

import com.example.namo2.domain.group.application.MoimMemoFacade;
import com.example.namo2.domain.group.ui.dto.GroupDiaryRequest;
import com.example.namo2.domain.group.ui.dto.GroupDiaryResponse;
import com.example.namo2.domain.group.ui.dto.GroupScheduleRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "8. Diary (모임)", description = "모임 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/diaries")
public class GroupDiaryController {
	private final MoimMemoFacade moimMemoFacade;
	private final Converter converter;

	@Operation(summary = "모임 기록 생성", description = "모임 기록 생성 API")
	@PostMapping(value = "/{moimScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Void> createMoimMemo(
			@PathVariable Long moimScheduleId,
			@RequestPart(required = false) List<MultipartFile> imgs,
			@RequestPart String name,
			@RequestPart String money,
			@RequestPart String participants
	) {
		GroupDiaryRequest.LocationDto locationDto = new GroupDiaryRequest.LocationDto(name, money, participants);
		moimMemoFacade.create(moimScheduleId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 장소 수정", description = "모임 기록 장소 수정 API")
	@PatchMapping(value = "/{activityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> updateMoimMemo(
			@PathVariable Long activityId,
			@RequestPart(required = false) List<MultipartFile> imgs,
			@RequestPart String name,
			@RequestPart String money,
			@RequestPart String participants
	) {
		GroupDiaryRequest.LocationDto locationDto = new GroupDiaryRequest.LocationDto(name, money, participants);
		moimMemoFacade.modifyMoimMemoLocation(activityId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
	@GetMapping("/{moimScheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> getMoimMemo(
			@PathVariable("moimScheduleId") Long moimScheduleId
	) {
		GroupDiaryResponse.MoimDiaryDto moimDiaryDto = moimMemoFacade.getMoimMemoWithLocations(moimScheduleId);
		return new BaseResponse(moimDiaryDto);
	}

	@Operation(summary = "월간 모임 기록 조회", description = "월간 모임 기록 조회 API")
	@GetMapping("/month/{month}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto>> findMonthMoimMemo(
			@PathVariable("month") String month,
			Pageable pageable,
			HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto> diaryDto = moimMemoFacade
				.getMonthMonthMoimMemo((Long)request.getAttribute("userId"), localDateTimes, pageable);
		return new BaseResponse(diaryDto);
	}

	@Operation(summary = "개인 페이지 모임 기록 삭제", description = "모임 기록 장소 삭제 API")
	@DeleteMapping("/person/{scheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removePersonMoimMemo(
			@PathVariable Long scheduleId,
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		moimMemoFacade.removePersonMoimMemo(scheduleId, userId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 전체 삭제", description = "모임 기록 전체 삭제 API")
	@DeleteMapping("/all/{moimDiaryId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemo(
			@PathVariable Long moimDiaryId
	) {
		moimMemoFacade.removeMoimMemo(moimDiaryId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 장소 삭제", description = "모임 기록 장소 삭제 API")
	@DeleteMapping("/{activityId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemoLocation(
			@PathVariable Long activityId
	) {
		moimMemoFacade.removeMoimMemoLocation(activityId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 텍스트 추가", description = "모임 기록 텍스트 추가 API")
	@PatchMapping("/text/{moimScheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> createMoimScheduleText(
			@PathVariable Long moimScheduleId,
			@RequestBody GroupScheduleRequest.PostGroupScheduleTextDto moimScheduleText,
			HttpServletRequest request
	) {
		moimMemoFacade.createMoimScheduleText(moimScheduleId,
				(Long)request.getAttribute("userId"),
				moimScheduleText);
		return BaseResponse.ok();
	}
}
