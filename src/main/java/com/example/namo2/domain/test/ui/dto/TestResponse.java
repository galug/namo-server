package com.example.namo2.domain.test.ui.dto;

import lombok.Builder;

public class TestResponse {

	private TestResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Builder
	public static class LogTestDto {
		private String text;
		private Integer number;
	}
}
