package com.example.namo2.domain.memo.dao.repository;

import static com.example.namo2.domain.memo.domain.QMoimMemoLocation.*;
import static com.example.namo2.domain.memo.domain.QMoimMemoLocationAndUser.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;

import com.example.namo2.domain.moim.domain.MoimSchedule;

public class MoimMemoLocationRepositoryImpl implements MoimMemoLocationRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public MoimMemoLocationRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule) {
		return queryFactory.select(moimMemoLocation).distinct()
			.from(moimMemoLocation)
			.join(moimMemoLocation.moimMemo).fetchJoin()
			.leftJoin(moimMemoLocation.moimMemoLocationImgs).fetchJoin()
			.where(moimMemoLocation.moimMemo.moimSchedule.eq(moimSchedule))
			.fetch();
	}

	@Override
	public List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations) {
		return queryFactory.select(moimMemoLocationAndUser)
			.from(moimMemoLocationAndUser)
			.where(moimMemoLocationAndUser.moimMemoLocation.in(moimMemoLocations))
			.fetch();
	}
}
