package com.example.namo2.domain.test.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.namo2.domain.test.ui.dto.TestRequest;
import com.example.namo2.domain.test.ui.dto.TestResponse;
import com.example.namo2.global.common.response.BaseResponse;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/log")
	public String testLog() {
		return "test";
	}

	@GetMapping("/authenticate")
	public String test() {
		return "인증 완료";
	}

	@PostMapping("/log")
	public BaseResponse<TestResponse.LogTestDto> loggingTest(
			@RequestBody TestRequest.LogTestDto logTestDto
	) {
		return new BaseResponse<>(TestResponse.LogTestDto.builder()
				.text(logTestDto.getText())
				.number(logTestDto.getNumber())
				.build());
	}
}
