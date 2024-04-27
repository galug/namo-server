package com.example.namo2.domain.memo.ui;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.memo.application.MoimMemoFacade;
import com.example.namo2.domain.memo.ui.dto.MoimMemoRequest;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.Converter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "8. Diary (그룹)", description = "그룹 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/diaries")
public class GroupDiaryController {
	private final MoimMemoFacade moimMemoFacade;
	private final Converter converter;

	@Operation(summary = "그룹 기록 생성", description = "그룹 기록 생성 API")
	@PostMapping("/{moimScheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Void> createMoimMemo(
			@RequestPart(required = false) List<MultipartFile> imgs,
			@PathVariable Long moimScheduleId,
			@RequestPart(required = true) String name,
			@RequestParam(defaultValue = "0") String money,
			@RequestPart(required = true) String participants
	) {
		MoimMemoRequest.LocationDto locationDto = new MoimMemoRequest.LocationDto(name, money, participants);
		moimMemoFacade.create(moimScheduleId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "그룹 기록 장소 수정", description = "그룹 기록 장소 수정 API")
	@PatchMapping("/{memoLocationId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> updateMoimMemo(
			@RequestPart(required = false) List<MultipartFile> imgs,
			@PathVariable Long memoLocationId,
			@RequestPart(required = true) String name,
			@RequestPart(required = true) String money,
			@RequestPart(required = true) String participants
	) {
		MoimMemoRequest.LocationDto locationDto = new MoimMemoRequest.LocationDto(name, money, participants);
		moimMemoFacade.modifyMoimMemoLocation(memoLocationId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "그룹 기록 조회", description = "그룹 기록 조회 API")
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
		MoimMemoResponse.MoimMemoDto moimMemoDto = moimMemoFacade.getMoimMemoWithLocations(moimScheduleId);
		return new BaseResponse(moimMemoDto);
	}

	@Operation(summary = "월간 그룹 기록 조회", description = "월간 그룹 기록 조회 API")
	@GetMapping("/month/{month}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<MoimMemoResponse.SliceDiaryDto<MoimMemoResponse.DiaryDto>> findMonthMoimMemo(
			@PathVariable("month") String month,
			Pageable pageable,
			HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		MoimMemoResponse.SliceDiaryDto<MoimMemoResponse.DiaryDto> diaryDto = moimMemoFacade
				.getMonthMonthMoimMemo((Long)request.getAttribute("userId"), localDateTimes, pageable);
		return new BaseResponse(diaryDto);
	}

	@Operation(summary = "개인 페이지 그룹 기록 삭제", description = "그룹 기록 장소 삭제 API")
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

	@Operation(summary = "그룹 기록 전체 삭제", description = "그룹 기록 전체 삭제 API")
	@DeleteMapping("/all/{memoId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemo(
			@PathVariable Long memoId
	) {
		moimMemoFacade.removeMoimMemo(memoId);
		return BaseResponse.ok();
	}

	@Operation(summary = "그룹 기록 장소 삭제", description = "그룹 기록 장소 삭제 API")
	@DeleteMapping("/{memoLocationId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemoLocation(
			@PathVariable Long memoLocationId
	) {
		moimMemoFacade.removeMoimMemoLocation(memoLocationId);
		return BaseResponse.ok();
	}

	@Operation(summary = "그룹 기록 텍스트 추가", description = "그룹 기록 텍스트 추가 API")
	@PatchMapping("/text/{moimScheduleId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> createMoimScheduleText(
			@PathVariable Long moimScheduleId,
			HttpServletRequest request,
			@RequestBody MoimScheduleRequest.PostMoimScheduleTextDto moimScheduleText
	) {
		moimMemoFacade.createMoimScheduleText(moimScheduleId,
				(Long)request.getAttribute("userId"),
				moimScheduleText);
		return BaseResponse.ok();
	}
}
