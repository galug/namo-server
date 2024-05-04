package com.example.namo2.domain.group.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
		@NotNull
		private Long moimId;
		@NotBlank
		private String moimName;
	}
}
