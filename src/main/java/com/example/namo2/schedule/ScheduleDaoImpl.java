package com.example.namo2.schedule;

import com.example.namo2.entity.schedule.Alarm;
import com.example.namo2.entity.schedule.Schedule;
import com.example.namo2.entity.user.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.QGetScheduleRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.namo2.entity.category.QCategory.category;
import static com.example.namo2.entity.category.QPalette.palette;
import static com.example.namo2.entity.schedule.QAlarm.alarm;
import static com.example.namo2.entity.schedule.QImage.image;
import static com.example.namo2.entity.schedule.QSchedule.schedule;

@Slf4j
public class ScheduleDaoImpl implements ScheduleDaoCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ScheduleDaoImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    @Override
    public List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
//        List<GetScheduleRes> schedules = queryFactory
//                .select(new QGetScheduleRes(
//                        schedule.id, schedule.name, schedule.period.startDate, schedule.period.endDate,
//                        schedule.period.dayInterval, schedule.location, schedule.category.id, schedule.category.name,
//                        schedule.category.palette.color, schedule.eventId, schedule.hasDiary))
//                .from(schedule)
//                .join(schedule.category, category).fetchJoin()
//                .join(schedule.category.palette, palette).fetchJoin()
//                .where(schedule.user.eq(user).and(schedule.period.startDate.before(endDate).and(schedule.period.endDate.after(startDate))))
//                .fetch();
        List<GetScheduleRes> result = em.createQuery("select new com.example.namo2.schedule.dto.GetScheduleRes(" +
                        "s.id, s.name, s.period.startDate, s.period.endDate, s.period.dayInterval,s.location,s.category.id," +
                        "s.category.name, s.category.palette.color, s.eventId, s.hasDiary)" +
                        " from Schedule s" +
                        " join s.category c" +
                        " join c.palette p" +
                        " where s.user = :user and s.period.startDate <= :endDate and s.period.endDate >= :startDate", GetScheduleRes.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        List<Long> scheduleIds = result.stream().map((schedule) -> schedule.getScheduleId()).collect(Collectors.toList());
        List<Alarm> alarms = queryFactory
                .select(alarm)
                .from(alarm)
                .where(alarm.schedule.id.in(scheduleIds))
                .fetch();
        Map<Long, List<Alarm>> alarmScheduleIdMap =
                alarms.stream().collect(Collectors.groupingBy((alarm) -> alarm.getSchedule().getId()));
        result.stream().filter(s -> alarmScheduleIdMap.containsKey(s.getScheduleId())).forEach(s -> s.setAlarmDate(alarmScheduleIdMap.get(s.getScheduleId()).stream().map(Alarm::getAlarmDate)
                .collect(Collectors.toList())));
        return result;
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
