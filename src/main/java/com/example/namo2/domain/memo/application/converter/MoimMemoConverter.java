package com.example.namo2.domain.memo.application.converter;

import com.example.namo2.domain.memo.domain.MoimMemo;

import com.example.namo2.domain.moim.domain.MoimSchedule;

public class MoimMemoConverter {
	private MoimMemoConverter() {
		throw new IllegalStateException("Utill Classes");
	}

	public static MoimMemo toMoimMemo(MoimSchedule moimSchedule) {
		return MoimMemo.builder()
			.moimSchedule(moimSchedule)
			.build();
	}
}
