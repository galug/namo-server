package com.example.namo2.domain.schedule.dao.repository;

import static com.example.namo2.domain.schedule.domain.QImage.*;

import jakarta.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.schedule.domain.Schedule;

public class ImageRepositoryImpl implements ImageRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	public ImageRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
		this.em = em;
	}

	@Override
	public void deleteDiaryImages(Schedule schedule) {
		queryFactory
			.delete(image)
			.where(image.schedule.eq(schedule))
			.execute();
		em.flush();
		em.clear();
	}
}
