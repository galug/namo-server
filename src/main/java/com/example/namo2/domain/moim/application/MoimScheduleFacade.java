package com.example.namo2.domain.moim.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.memo.MoimMemoService;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.moim.application.converter.MoimAndUserConverter;
import com.example.namo2.domain.moim.application.converter.MoimScheduleConverter;
import com.example.namo2.domain.moim.application.converter.MoimScheduleResponseConverter;
import com.example.namo2.domain.moim.application.impl.MoimAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimScheduleAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimScheduleService;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.schedule.ScheduleService;
import com.example.namo2.domain.schedule.domain.Location;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class MoimScheduleFacade {
    private final UserService userService;
    private final MoimService moimService;
    private final MoimAndUserService moimAndUserService;
    private final MoimScheduleService moimScheduleService;
    private final MoimScheduleAndUserService moimScheduleAndUserService;
    private final MoimMemoService moimMemoService;
    private final ScheduleService scheduleService;
    private final CategoryService categoryService;

    /**
     * 버그 발생 우려;
     * categories 수정시 모임과 기본 카테고리에 대해서는 수정이 불가능하게 해야함
     */
    @Transactional(readOnly = false)
    public Long createSchedule(MoimScheduleRequest.PostMoimScheduleDto moimScheduleDto) {
        Moim moim = moimService.getMoim(moimScheduleDto.getMoimId());
        Period period = MoimScheduleConverter.toPeriod(moimScheduleDto);
        Location location = MoimScheduleConverter.toLocation(moimScheduleDto);
        MoimSchedule moimSchedule = MoimScheduleConverter
                .toMoimSchedule(moim, period, location, moimScheduleDto);
        MoimSchedule savedMoimSchedule = moimScheduleService.create(moimSchedule);

        createMoimScheduleAndUsers(moimScheduleDto.getUsers(), savedMoimSchedule);

        return savedMoimSchedule.getId();
    }

    private void createMoimScheduleAndUsers(List<Long> usersId, MoimSchedule savedMoimSchedule) {
        List<User> users = userService.getUsers(usersId);
        List<Category> categories = categoryService
                .getMoimUsersCategories(users);
        List<MoimScheduleAndUser> moimScheduleAndUsers = MoimScheduleConverter
                .toMoimScheduleAndUsers(categories, savedMoimSchedule, users);
        moimScheduleAndUserService.createAll(moimScheduleAndUsers);
    }

    @Transactional(readOnly = false)
    public void modifyMoimSchedule(MoimScheduleRequest.PatchMoimScheduleDto moimScheduleDto) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleDto.getMoimScheduleId());
        Period period = MoimScheduleConverter.toPeriod(moimScheduleDto);
        Location location = MoimScheduleConverter.toLocation(moimScheduleDto);
        moimSchedule.update(moimScheduleDto.getName(), period, location);
        moimScheduleAndUserService.removeMoimScheduleAndUser(moimSchedule);
        createMoimScheduleAndUsers(moimScheduleDto.getUsers(), moimSchedule);
    }

    @Transactional(readOnly = false)
    public void modifyMoimScheduleCategory(MoimScheduleRequest.PatchMoimScheduleCategoryDto scheduleCategoryDto, Long userId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(scheduleCategoryDto.getMoimScheduleId());
        User user = userService.getUser(userId);
        Category category = categoryService.getCategory(scheduleCategoryDto.getCategoryId());
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
        moimScheduleAndUser.updateCategory(category);
    }

    /**
     * 모임 스케줄 삭제는 모임 메모와 연관된 요소 있으므로 나중에
     */
    @Transactional(readOnly = false)
    public void removeMoimSchedule(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        MoimMemo moimMemo = moimMemoService.getMoimMemo(moimSchedule);
        List<MoimScheduleAndUser> moimScheduleAndUsers = moimScheduleService.getMoimScheduleAndUsers(moimSchedule);

//         모임 메모가 있는 경우 모임 메모 장소를 모두 삭제 후 모임 메모 삭제
        if (moimMemo != null) {
            moimMemo.getMoimMemoLocations().stream().forEach((moimMemoLocation -> moimMemoService.removeMoimMemoLocation(moimMemoLocation.getId())));
            moimMemoService.removeMoimMemo(moimMemo);
        }

        moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUsers);
        moimScheduleAndUserService.removeMoimScheduleAndUser(moimSchedule);
        moimScheduleService.remove(moimSchedule);
    }

    @Transactional(readOnly = false)
    public void createMoimScheduleAlarm(MoimScheduleRequest.PostMoimScheduleAlarmDto moimScheduleAlarmDto, Long userId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleAlarmDto.getMoimScheduleId());
        User user = userService.getUser(userId);
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);

        for (Integer alarmDate : moimScheduleAlarmDto.getAlarmDates()) {
            MoimScheduleAlarm moimScheduleAlarm = MoimScheduleConverter.toMoimScheduleAlarm(moimScheduleAndUser, alarmDate);
            moimScheduleAndUserService.createMoimScheduleAlarm(moimScheduleAlarm);
        }
    }

    @Transactional(readOnly = false)
    public void modifyMoimScheduleAlarm(MoimScheduleRequest.PostMoimScheduleAlarmDto moimScheduleAlarmDto, Long userId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleAlarmDto.getMoimScheduleId());
        User user = userService.getUser(userId);
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
        moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUser);

        for (Integer alarmDate : moimScheduleAlarmDto.getAlarmDates()) {
            MoimScheduleAlarm moimScheduleAlarm = MoimScheduleConverter.toMoimScheduleAlarm(moimScheduleAndUser, alarmDate);
            moimScheduleAndUserService.createMoimScheduleAlarm(moimScheduleAlarm);
        }
    }

    @Transactional(readOnly = true)
    public List<MoimScheduleResponse.MoimScheduleDto> getMoimSchedules(Long moimId, List<LocalDateTime> localDateTimes) {
        Moim moim = moimService.getMoim(moimId);
        List<MoimAndUser> moimAndUsersInMoim = moimAndUserService.getMoimAndUsers(moim);
        List<User> users = MoimAndUserConverter.toUsers(moimAndUsersInMoim);

        List<Schedule> indivisualsSchedules = scheduleService.getSchedules(users);
        List<MoimScheduleAndUser> moimScheduleAndUsers = moimScheduleService
                .getMoimSchedules(localDateTimes, users);
        return MoimScheduleResponseConverter
                .toMoimScheduleDtos(indivisualsSchedules, moimScheduleAndUsers, moimAndUsersInMoim);
    }
}
