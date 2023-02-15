package com.example.namo2.schedule;

import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.QGetScheduleRes;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.namo2.entity.QCategory.category;
import static com.example.namo2.entity.QPalette.palette;
import static com.example.namo2.entity.QSchedule.schedule;

public class ScheduleDaoImpl implements ScheduleDaoCustom {

    private final JPAQueryFactory queryFactory;

    public ScheduleDaoImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<GetScheduleRes> findSchedulesByUserId(User user) {
        return queryFactory
                .select(new QGetScheduleRes(schedule.id, schedule.name, schedule.period.startDate,
                        schedule.period.endDate, schedule.period.alarmDate, schedule.point,
                        category.id, category.name, category.palette.color))
                .from(schedule)
                .join(schedule.category, category)
                .join(category.palette, palette)
                .where(schedule.user.eq(user))
                .fetch();
    }
}
