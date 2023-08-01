package com.example.namo2.schedule;

import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.moim.MoimAndUser;
import com.example.namo2.entity.moim.QMoimAndUser;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.QMoimScheduleAndUser;
import com.example.namo2.entity.schedule.Alarm;
import com.example.namo2.entity.schedule.QSchedule;
import com.example.namo2.entity.schedule.Schedule;
import com.example.namo2.entity.user.QUser;
import com.example.namo2.entity.user.User;
import com.example.namo2.moim.dto.MoimScheduleRes;
import com.example.namo2.moim.dto.MoimScheduleUserDto;
import com.example.namo2.moim.dto.QMoimScheduleRes;
import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.schedule.dto.SliceDiaryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.namo2.entity.moim.QMoimAndUser.moimAndUser;
import static com.example.namo2.entity.moimschedule.QMoimScheduleAndUser.moimScheduleAndUser;
import static com.example.namo2.entity.schedule.QAlarm.alarm;
import static com.example.namo2.entity.schedule.QImage.image;
import static com.example.namo2.entity.schedule.QSchedule.schedule;
import static com.example.namo2.entity.user.QUser.user;

@Slf4j
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    @Override
    public List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<GetScheduleRes> result = em.createQuery("select new com.example.namo2.schedule.dto.GetScheduleRes(" +
                        "s.id, s.name, s.period.startDate, s.period.endDate, s.period.dayInterval,s.location,s.category.id," +
                        "s.category.name, s.category.palette.color, s.hasDiary)" +
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
    public SliceDiaryDto<DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Schedule> content = queryFactory
                .select(schedule)
                .from(schedule)
                .join(schedule.category).fetchJoin()
                .join(schedule.category.palette).fetchJoin()
                .where(schedule.user.eq(user)
                        .and(schedule.period.startDate.before(endDate)
                                .and(schedule.period.endDate.after(startDate))
                                .and(schedule.hasDiary.isTrue())
                        ))
                .orderBy(schedule.period.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        SliceImpl<Schedule> schedules = new SliceImpl<>(content, pageable, hasNext);
        Slice<DiaryDto> diarySlices = schedules.map(DiaryDto::new);
        return new SliceDiaryDto(diarySlices);
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

    @Override
    public List<MoimScheduleRes> findScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate) {
        List<MoimAndUser> moimAndUsers = queryFactory.select(moimAndUser)
                .from(moimAndUser)
                .join(moimAndUser.user).fetchJoin()
                .where(moimAndUser.moim.id.eq(moimId))
                .fetch();
        List<User> users = moimAndUsers.stream()
                .map((moimAndUser -> moimAndUser.getUser()))
                .collect(Collectors.toList());

        List<MoimScheduleRes> results = em.createQuery("select new com.example.namo2.moim.dto.MoimScheduleRes(" +
                        "s.name, s.period.startDate, s.period.endDate, s.period.dayInterval, u.name, mu.color)" +
                        " from Schedule s, MoimAndUser mu" +
                        " join s.user u" +
                        " where s.user in (:users) " +
                        "and s.period.startDate <= :endDate " +
                        "and s.period.endDate >= :startDate " +
                        "and mu.user = s.user and mu.moim.id = :moimId", MoimScheduleRes.class)
                .setParameter("users", users)
                .setParameter("moimId", moimId)
                .setParameter("endDate", endDate)
                .setParameter("startDate", startDate)
                .getResultList();
        Map<Long, List<MoimScheduleUserDto>> moimUserMapping = moimAndUsers.stream()
                .collect(Collectors.groupingBy(
                        (moimAndUser -> moimAndUser.getMoim().getId()),
                        Collectors.mapping(
                                moimAndUser -> new MoimScheduleUserDto(moimAndUser.getUser().getName(), moimAndUser.getColor()),
                                Collectors.toList()
                        )
                ));
        List<MoimScheduleRes> groupSchedules = queryFactory.select(new QMoimScheduleRes(moimScheduleAndUser.moimSchedule.name,
                        moimScheduleAndUser.moimSchedule.period.startDate,
                        moimScheduleAndUser.moimSchedule.period.endDate,
                        moimScheduleAndUser.moimSchedule.period.dayInterval,
                        moimScheduleAndUser.moimSchedule.moim.id))
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule)
                .where(moimScheduleAndUser.user.in(users)
                        .and(moimScheduleAndUser.moimSchedule.period.startDate.loe(endDate))
                        .and(moimScheduleAndUser.moimSchedule.period.endDate.goe(startDate)))
                .fetch();
        groupSchedules.forEach((groupSchedule) -> {
            Long groupMoimId = groupSchedule.getMoimId();
            boolean isCurMoimSchedule = false;
            if (groupMoimId == moimId) {
                isCurMoimSchedule = true;
            }
            groupSchedule.setUsers(moimUserMapping.get(groupMoimId), isCurMoimSchedule);
        });
        results.addAll(groupSchedules);
        return results;
    }
}
