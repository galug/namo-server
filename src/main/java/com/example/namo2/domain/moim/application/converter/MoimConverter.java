package com.example.namo2.domain.moim.application.converter;

import com.example.namo2.domain.moim.domain.Moim;

public class MoimConverter {
	private MoimConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static Moim toMoim(String name, String imgUrl) {
		return Moim.builder()
			.name(name)
			.imgUrl(imgUrl)
			.build();
	}
}
