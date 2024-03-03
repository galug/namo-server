package com.example.namo2.domain.moim.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MoimRequest {
	private MoimRequest() {
		throw new IllegalStateException("Utils Class");
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatchMoimNameDto {
		private Long moimId;
		private String moimName;
	}
}
