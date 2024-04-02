package com.example.namo2.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Converter {
	public List<LocalDateTime> convertLongToLocalDateTime(String yearAndMonth) {
		try {
			List<Integer> ym = Arrays.stream(yearAndMonth.split(","))
				.map(Integer::valueOf)
				.toList();
			LocalDateTime startMonth = LocalDate.of(ym.get(0), ym.get(1), 1).atStartOfDay();
			LocalDateTime localDateTime = startMonth.plusMonths(1L);
			return List.of(startMonth, localDateTime);
		} catch (Exception e) {
			throw new BaseException(BaseResponseStatus.INVALID_FORMAT_FAILURE);
		}
	}

}
