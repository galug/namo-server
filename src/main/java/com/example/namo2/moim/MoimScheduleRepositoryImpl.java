package com.example.namo2.moim;

import com.example.namo2.entity.category.QCategory;
import com.example.namo2.entity.category.QPalette;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import com.example.namo2.entity.moimschedule.QMoimSchedule;
import com.example.namo2.entity.moimschedule.QMoimScheduleAndUser;
import com.example.namo2.entity.schedule.Schedule;
import com.example.namo2.entity.user.User;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.entity.category.QCategory.category;
import static com.example.namo2.entity.category.QPalette.palette;
import static com.example.namo2.entity.moimschedule.QMoimSchedule.moimSchedule;
import static com.example.namo2.entity.moimschedule.QMoimScheduleAndUser.moimScheduleAndUser;
import static com.example.namo2.entity.schedule.QSchedule.schedule;

public class MoimScheduleRepositoryImpl implements MoimScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public MoimScheduleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public SliceDiaryDto<DiaryDto> findMoimScheduleMemoByMonth(Long userId, List<LocalDateTime> dates, Pageable pageable) {
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
        Slice<DiaryDto> diarySlices = moimSchedulesSlice.map(DiaryDto::new);
        return new SliceDiaryDto(diarySlices);
    }
}
