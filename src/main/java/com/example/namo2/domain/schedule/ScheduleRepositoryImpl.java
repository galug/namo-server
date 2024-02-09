package com.example.namo2.domain.schedule;

import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.moim.dto.MoimScheduleRes;
import com.example.namo2.domain.moim.dto.MoimScheduleUserDto;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.GetScheduleRes;
import com.example.namo2.domain.schedule.dto.OnlyDiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.namo2.entity.category.QCategory.category;
import static com.example.namo2.entity.category.QPalette.palette;
import static com.example.namo2.entity.moim.QMoimAndUser.moimAndUser;
import static com.example.namo2.entity.moimschedule.QMoimSchedule.moimSchedule;
import static com.example.namo2.entity.moimschedule.QMoimScheduleAndUser.moimScheduleAndUser;
import static com.example.namo2.entity.schedule.QImage.image;
import static com.example.namo2.entity.schedule.QSchedule.schedule;

@Slf4j
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }


    /**
     * 알람을 한 번에 가지고 오는 편이 성능상 더 좋은 퍼포먼스를 발휘할지도?
     */
    @Override
    public List<GetScheduleRes> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<GetScheduleRes> results = findPersonalSchedulesByUserId(user, startDate, endDate);
        List<GetScheduleRes> moimSchedules = findMoimSchedulesByUserId(user, startDate, endDate);
        if (moimSchedules != null) {
            results.addAll(moimSchedules);
        }
        return results;
    }

    public List<GetScheduleRes> findPersonalSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<Schedule> schedules = queryFactory
                .select(schedule).distinct()
                .from(schedule)
                .leftJoin(schedule.alarms).fetchJoin()
                .where(schedule.user.eq(user),
                        scheduleDateLoe(endDate),
                        scheduleDateGoe(startDate))
                .fetch();
        return schedules.stream().map(schedule -> new GetScheduleRes(schedule))
                .collect(Collectors.toList());
    }

    private BooleanExpression scheduleDateLoe(LocalDateTime endDate) {
        return endDate != null ? schedule.period.startDate.before(endDate) : null;
    }

    private BooleanExpression scheduleDateGoe(LocalDateTime startDate) {
        return startDate != null ? schedule.period.endDate.after(startDate) : null;
    }

    @Override
    public List<GetScheduleRes> findMoimSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<MoimScheduleAndUser> moimScheduleAndUsers = queryFactory
                .select(moimScheduleAndUser).distinct()
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
                .leftJoin(moimSchedule.moimScheduleAlarms).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimScheduleAndUser.user.eq(user),
                        moimScheduleDateLoe(endDate),
                        moimScheduleDateGoe(startDate)
                )
                .fetch();
        return moimScheduleAndUsers.stream()
                .map(GetScheduleRes::new).collect(Collectors.toList());
    }

    private BooleanExpression moimScheduleDateGoe(LocalDateTime startDate) {
        return startDate != null ? moimSchedule.period.endDate.after(startDate) : null;
    }

    private BooleanExpression moimScheduleDateLoe(LocalDateTime endDate) {
        return endDate != null ? moimSchedule.period.startDate.before(endDate) : null;
    }

    @Override
    public SliceDiaryDto<DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Schedule> content = queryFactory
                .select(schedule)
                .from(schedule)
                .join(schedule.category, category).fetchJoin()
                .join(category.palette, palette).fetchJoin()
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
    public List<OnlyDiaryDto> findAllScheduleDiary(User user) {
        List<Schedule> schedules = queryFactory
                .select(schedule).distinct()
                .from(schedule)
                .leftJoin(schedule.images, image).fetchJoin()
                .where(schedule.user.eq(user),
                        schedule.hasDiary.isTrue()
                )
                .fetch();
        return schedules.stream().map(OnlyDiaryDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Schedule findOneScheduleAndImages(Long scheduleId) {
        return queryFactory
                .select(schedule)
                .distinct()
                .from(schedule)
                .leftJoin(schedule.images, image).fetchJoin()
                .where(schedule.id.eq(scheduleId))
                .fetchOne();
    }

    @Override
    public List<MoimScheduleRes> findMonthScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate) {
        // 모임에 속한 유저들 파악
        List<MoimAndUser> moimAndUsers = queryFactory.select(moimAndUser)
                .from(moimAndUser)
                .join(moimAndUser.user).fetchJoin()
                .where(moimAndUser.moim.id.eq(moimId))
                .fetch();
        List<User> users = moimAndUsers.stream()
                .map((moimAndUser -> moimAndUser.getUser()))
                .collect(Collectors.toList());
        Map<Long, MoimScheduleUserDto> moimAndUserDtoMap = moimAndUsers.stream()
                .collect(Collectors.toMap(
                        (moimAndUser -> moimAndUser.getUser().getId()),
                        (moimAndUser -> new MoimScheduleUserDto(moimAndUser.getUser().getId(), moimAndUser.getUser().getName(), moimAndUser.getColor()))
                ));

        List<MoimScheduleRes> results = findIndivisualSchedule(moimId, startDate, endDate, users);
        Map<MoimSchedule, List<MoimScheduleUserDto>> groupScheduleMap = findMoimScheduleAndScheduleUserMap(startDate, endDate, users, moimAndUserDtoMap);

        for (MoimSchedule moimSchedule : groupScheduleMap.keySet()) {
            List<MoimScheduleUserDto> moimScheduleUserDtos = groupScheduleMap.get(moimSchedule);
            MoimScheduleRes moimScheduleRes = new MoimScheduleRes(moimSchedule.getName(), moimSchedule.getPeriod().getStartDate(), moimSchedule.getPeriod().getEndDate(),
                    moimSchedule.getPeriod().getDayInterval(), moimSchedule.getMoim().getId(), moimSchedule.getId(),
                    moimSchedule.getLocation().getX(), moimSchedule.getLocation().getY(), moimSchedule.getLocation().getLocationName());
            moimScheduleRes.setUsers(moimScheduleUserDtos, moimSchedule.getMoim().getId() == moimId, moimSchedule.getMoimMemo() != null);
            results.add(moimScheduleRes);
        }
        return results;
    }

    private List<MoimScheduleRes> findIndivisualSchedule(Long moimId, LocalDateTime startDate, LocalDateTime endDate, List<User> users) {
        return em.createQuery("select new com.example.namo2.moim.dto.MoimScheduleRes(" +
                        "s.name, s.period.startDate, s.period.endDate, s.period.dayInterval, u.id, u.name, mu.color)" +
                        " from Schedule s, MoimAndUser mu" +
                        " join s.user u " +
                        " join s.category c" +
                        " where s.user in (:users) " +
                        "and s.period.startDate <= :endDate " +
                        "and s.period.endDate >= :startDate " +
                        "and c.share = :share " +
                        "and mu.user = s.user and mu.moim.id = :moimId", MoimScheduleRes.class)
                .setParameter("users", users)
                .setParameter("moimId", moimId)
                .setParameter("endDate", endDate)
                .setParameter("startDate", startDate)
                .setParameter("share", true)
                .getResultList();
    }

    private Map<MoimSchedule, List<MoimScheduleUserDto>> findMoimScheduleAndScheduleUserMap(LocalDateTime startDate, LocalDateTime endDate, List<User> users, Map<Long, MoimScheduleUserDto> moimAndUserDtoMap) {
        List<MoimScheduleAndUser> moimScheduleAndUsers = queryFactory.select(moimScheduleAndUser)
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
                .join(moimScheduleAndUser.category, category).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimScheduleAndUser.user.in(users)
                        .and(moimScheduleAndUser.moimSchedule.period.startDate.loe(endDate))
                        .and(moimScheduleAndUser.moimSchedule.period.endDate.goe(startDate))
                        .and(moimScheduleAndUser.category.share.isTrue()))
                .fetch();
        return moimScheduleAndUsers.stream().collect(
                Collectors.groupingBy(
                        ((moimScheduleAndUser1 -> moimScheduleAndUser1.getMoimSchedule())),
                        Collectors.mapping(
                                (moimScheduleAndUser1 ->
                                        moimAndUserDtoMap.get(moimScheduleAndUser1.getUser().getId())),
                                Collectors.toList()
                        )
                )
        );
    }
}
