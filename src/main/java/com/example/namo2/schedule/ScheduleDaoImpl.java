package com.example.namo2.schedule;

import com.example.namo2.entity.QImage;
import com.example.namo2.entity.Schedule;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.QSchedule;
import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.QGetScheduleRes;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.namo2.entity.QCategory.category;
import static com.example.namo2.entity.QImage.image;
import static com.example.namo2.entity.QPalette.palette;
import static com.example.namo2.entity.QSchedule.schedule;

public class ScheduleDaoImpl implements ScheduleDaoCustom {

    private final JPAQueryFactory queryFactory;

    public ScheduleDaoImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(new QGetScheduleRes(schedule.id, schedule.name, schedule.period.startDate,
                        schedule.period.endDate, schedule.period.alarmDate, schedule.location,
                        category.id, category.name, category.palette.color))
                .from(schedule)
                .join(schedule.category, category)
                .join(category.palette, palette)
                .where(schedule.user.eq(user).and(schedule.period.startDate.before(endDate).and(schedule.period.endDate.after(startDate))))
                .fetch();
    }

    @Override
    public List<Schedule> findScheduleDiaryByMonthDtoWithNotPaging(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(schedule)
                .distinct()
                .from(schedule)
                .leftJoin(schedule.images, image).fetchJoin()
                .where(schedule.user.eq(user)
                        .and(schedule.period.startDate.before(endDate)
                                .and(schedule.period.endDate.after(startDate))
                        .and(schedule.hasDiary.isTrue())
                        ))
                .fetch();
    }

    @Override
    public Schedule findScheduleAndImages(Long scheduleId) {
        return queryFactory
                .select(schedule)
                .distinct()
                .from(schedule)
                .leftJoin(schedule.images, image).fetchJoin()
                .where(schedule.id.eq(scheduleId))
                .fetchOne();
    }
}
