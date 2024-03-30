package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.Moim;

public interface MoimRepositoryCustom {
	Moim findMoimHavingLockWithMoimAndUsersByCode(String code);

	Moim findHavingLockById(Long moimId);
}
