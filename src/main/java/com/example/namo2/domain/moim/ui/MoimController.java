package com.example.namo2.domain.moim.ui;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

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

import com.example.namo2.domain.moim.application.MoimFacade;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;

import com.example.namo2.global.common.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Moim", description = "모임 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/moims")
public class MoimController {
	private final MoimFacade moimFacade;

	@Operation(summary = "모임 생성", description = "모임 생성 API")
	@PostMapping("")
	public BaseResponse<MoimResponse.MoimIdDto> createMoim(@RequestPart MultipartFile img,
		@RequestPart String groupName,
		HttpServletRequest request) {
		MoimResponse.MoimIdDto moimIdDto = moimFacade.createMoim((Long)request.getAttribute("userId"), groupName, img);
		return new BaseResponse(moimIdDto);
	}

	@Operation(summary = "모임 조회", description = "모임 조회 API")
	@GetMapping("")
	public BaseResponse<List<MoimResponse.MoimDto>> findMoims(HttpServletRequest request) {
		List<MoimResponse.MoimDto> moims = moimFacade.getMoims((Long)request.getAttribute("userId"));
		return new BaseResponse(moims);
	}

	@Operation(summary = "모임 이름 변경", description = "모임 이름 변경 API, 변경자 입장에서만 적용")
	@PatchMapping("/name")
	public BaseResponse<Long> modifyMoimName(@RequestBody MoimRequest.PatchMoimNameDto patchMoimNameDto,
		HttpServletRequest request) {
		Long moimId = moimFacade.modifyMoimName(patchMoimNameDto, (Long)request.getAttribute("userId"));
		return new BaseResponse(moimId);
	}

	@Operation(summary = "모임 참여", description = "모임 참여 API")
	@PatchMapping("/participate/{code}")
	public BaseResponse<Long> createMoimAndUser(@PathVariable("code") String code, HttpServletRequest request) {
		Long moimId = moimFacade.createMoimAndUser((Long)request.getAttribute("userId"), code);
		return new BaseResponse(moimId);
	}

	@Operation(summary = "모임 탈퇴", description = "모임 탈퇴 API")
	@DeleteMapping("/withdraw/{moimId}")
	public BaseResponse removeMoimAndUser(@PathVariable("moimId") Long moimId, HttpServletRequest request) {
		moimFacade.removeMoimAndUser((Long)request.getAttribute("userId"), moimId);
		return BaseResponse.ok();
	}
}
