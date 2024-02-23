package com.example.namo2.domain.moim.dao.repository;

import static com.example.namo2.domain.category.domain.QCategory.*;
import static com.example.namo2.domain.moim.domain.QMoimSchedule.*;
import static com.example.namo2.domain.moim.domain.QMoimScheduleAndUser.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class MoimScheduleRepositoryImpl implements MoimScheduleRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public MoimScheduleRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public ScheduleResponse.SliceDiaryDto findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates,
		Pageable pageable) {
		List<MoimScheduleAndUser> content = queryFactory.select(moimScheduleAndUser)
			.from(moimScheduleAndUser)
			.join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
			.join(moimSchedule.moimMemo).fetchJoin()
			.join(moimScheduleAndUser.category, category).fetchJoin()
			.join(category.palette).fetchJoin()
			.where(moimSchedule.period.startDate.before(dates.get(1)),
				moimSchedule.period.endDate.after(dates.get(0)),
				moimScheduleAndUser.user.id.eq(userId)
			)
			.orderBy(moimSchedule.period.startDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		SliceImpl<MoimScheduleAndUser> moimSchedulesSlice = new SliceImpl<>(content, pageable, hasNext);
		Slice<ScheduleResponse.DiaryDto> diarySlices = moimSchedulesSlice.map(ScheduleResponse.DiaryDto::new);
		return new ScheduleResponse.SliceDiaryDto(diarySlices);
	}
}
