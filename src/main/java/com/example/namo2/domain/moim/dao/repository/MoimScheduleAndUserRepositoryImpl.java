package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.namo2.domain.category.domain.QCategory.category;
import static com.example.namo2.domain.moim.domain.QMoimSchedule.moimSchedule;
import static com.example.namo2.domain.moim.domain.QMoimScheduleAndUser.moimScheduleAndUser;

public class MoimScheduleAndUserRepositoryImpl implements MoimScheduleAndUserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MoimScheduleAndUserRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MoimScheduleAndUser> findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(LocalDateTime startDate, LocalDateTime endDate, List<User> users) {
        return queryFactory.select(moimScheduleAndUser)
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
                .join(moimScheduleAndUser.category, category).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimScheduleAndUser.user.in(users)
                        .and(moimScheduleAndUser.moimSchedule.period.startDate.loe(endDate))
                        .and(moimScheduleAndUser.moimSchedule.period.endDate.goe(startDate))
                        .and(moimScheduleAndUser.category.share.isTrue()))
                .fetch();
    }
}
