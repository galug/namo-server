package com.example.namo2.domain.schedule.application.impl;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.schedule.domain.Period;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodService {

	public void checkValidDate(Period period) {
		if (period.getStartDate().isAfter(period.getEndDate())) {
			throw new BaseException(BaseResponseStatus.INVALID_DATE);
		}
	}
}
