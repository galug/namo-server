package com.example.namo2.domain.moim.ui;

import java.util.List;

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

import com.example.namo2.domain.moim.application.MoimFacade;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;
import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Group", description = "그룹 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
	private final MoimFacade moimFacade;

	@Operation(summary = "그룹 생성", description = "그룹 생성 API")
	@PostMapping("")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<MoimResponse.MoimIdDto> createMoim(
			@RequestPart(required = false) MultipartFile img,
			@RequestPart(required = true) String groupName,
			HttpServletRequest request
	) {
		MoimResponse.MoimIdDto moimIdDto = moimFacade.createMoim((Long)request.getAttribute("userId"), groupName, img);
		return new BaseResponse(moimIdDto);
	}

	@Operation(summary = "그룹 조회", description = "유저가 참여중인 그룹 조회 API")
	@GetMapping("")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<List<MoimResponse.MoimDto>> findMoims(
			HttpServletRequest request
	) {
		List<MoimResponse.MoimDto> moims = moimFacade.getMoims((Long)request.getAttribute("userId"));
		return new BaseResponse(moims);
	}

	@Operation(summary = "그룹 이름 수정", description = "그룹 이름 수정 API, 변경자의 입장에서만 수정되어 적용됨")
	@PatchMapping("/name")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> modifyMoimName(
			@Valid @RequestBody MoimRequest.PatchMoimNameDto patchMoimNameDto,
			HttpServletRequest request
	) {
		Long moimId = moimFacade.modifyMoimName(patchMoimNameDto, (Long)request.getAttribute("userId"));
		return new BaseResponse(moimId);
	}

	@Operation(summary = "그룹 참여", description = "그룹 참여 API")
	@PatchMapping("/participate/{code}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<MoimResponse.MoimParticipantDto> createMoimAndUser(
			@PathVariable("code") String code,
			HttpServletRequest request
	) {
		MoimResponse.MoimParticipantDto moimParticipantDto = moimFacade.createMoimAndUser(
				(Long)request.getAttribute("userId"),
				code);
		return new BaseResponse(moimParticipantDto);
	}

	@Operation(summary = "그룹 탈퇴", description = "그룹 탈퇴 API")
	@DeleteMapping("/withdraw/{moimId}")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse removeMoimAndUser(
			@PathVariable("moimId") Long moimId,
			HttpServletRequest request
	) {
		moimFacade.removeMoimAndUser((Long)request.getAttribute("userId"), moimId);
		return BaseResponse.ok();
	}
}
