package com.example.namo2.domain.moim.application;

import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.moim.application.converter.MoimScheduleConverter;
import com.example.namo2.domain.moim.application.impl.MoimScheduleAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimScheduleService;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.domain.schedule.domain.Location;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.user.UserService;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimScheduleFacade {
    private final UserService userService;
    private final MoimService moimService;
    private final MoimScheduleService moimScheduleService;
    private final MoimScheduleAndUserService moimScheduleAndUserService;
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

    @Transactional
    public void modifyMoimSchedule(MoimScheduleRequest.PatchMoimScheduleDto moimScheduleDto) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleDto.getMoimScheduleId());
        Period period = MoimScheduleConverter.toPeriod(moimScheduleDto);
        Location location = MoimScheduleConverter.toLocation(moimScheduleDto);
        moimSchedule.update(moimScheduleDto.getName(), period, location);
        moimScheduleAndUserService.removeMoimScheduleAndUser(moimSchedule);
        createMoimScheduleAndUsers(moimScheduleDto.getUsers(), moimSchedule);
    }
}
