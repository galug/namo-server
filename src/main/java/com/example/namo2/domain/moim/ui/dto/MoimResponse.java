package com.example.namo2.domain.moim.ui.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MoimResponse {
	private MoimResponse() {
		throw new IllegalStateException("Util Class");
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimIdDto {
		private Long moimId;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class MoimDto {
		private Long groupId;
		private String groupName;
		private String groupImgUrl;
		private String groupCode;
		private List<MoimUserDto> moimUsers;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class MoimUserDto {
		private Long userId;
		private String userName;
		private Integer color;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class MoimParticipantDto {
		private Long moimId;
		private String code;
	}
}
