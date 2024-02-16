package com.example.namo2.domain.moim.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.ui.dto.MoimCategoryDto;
import com.example.namo2.domain.memo.MoimMemoRepository;
import com.example.namo2.domain.memo.MoimMemoService;
import com.example.namo2.domain.moim.dao.repository.MoimAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAlarmRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleAndUserRepository;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleRequest;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Location;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleDto;
import com.example.namo2.domain.schedule.ScheduleRepository;
import com.example.namo2.domain.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimService {
    private final static int[] MOIM_USERS_COLOR = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    private final MoimRepository moimRepository;
    private final MoimAndUserRepository moimAndUserRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
    private final MoimScheduleAlarmRepository moimScheduleAlarmRepository;
    private final MoimMemoRepository moimMemoRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;

    private final MoimMemoService moimMemoService;
    private final EntityManager em;

    public Moim create(Moim moim) {
        return moimRepository.save(moim);
    }

    @Transactional(readOnly = false)
    public Long patchMoimName(MoimRequest.PatchMoimNameDto patchMoimNameDto, Long userId) {
        User user = em.getReference(User.class, userId);
        Moim moim = em.getReference(Moim.class, patchMoimNameDto.getMoimId());
        MoimAndUser moimAndUser = moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        moimAndUser.updateCustomName(patchMoimNameDto.getMoimName());
        return moim.getId();
    }

    @Transactional(readOnly = false)
    public Long participate(Long userId, String code) {
        Moim moim = moimRepository.findMoimByCode(code).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
        User user = userRepository.getReferenceById(userId);
        duplicateParticipate(moim, user);
        Integer numberOfMoimer = moimAndUserRepository.countMoimAndUserByMoim(moim);
        MoimAndUser moimAndUser = MoimAndUser.builder().user(user).moim(moim).moimCustomName(moim.getName()).color(MOIM_USERS_COLOR[numberOfMoimer]).build();
        moimAndUserRepository.save(moimAndUser);
        return moim.getId();
    }

    private void duplicateParticipate(Moim moim, User user) {
        if (moimAndUserRepository.existsMoimAndUserByMoimAndUser(moim, user)) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_PARTICIPATE_FAILURE);
        }
    }

    @Transactional(readOnly = false)
    public void withdraw(Long userId, Long moimId) {
        User user = em.getReference(User.class, userId);
        Moim moim = em.getReference(Moim.class, moimId);
        MoimAndUser moimAndUser = moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        moimAndUserRepository.delete(moimAndUser);
    }

    @Transactional(readOnly = false)
    public Long createSchedule(MoimScheduleRequest.PostMoimScheduleDto scheduleReq) {
        Moim moim = moimRepository.getReferenceById(scheduleReq.getMoimId());
        Period period = Period.builder().startDate(scheduleReq.getStartDate()).endDate(scheduleReq.getEndDate()).dayInterval(scheduleReq.getInterval()).build();
        Location location = Location.create(scheduleReq.getX(), scheduleReq.getY(), scheduleReq.getLocationName());
        MoimSchedule moimSchedule = MoimSchedule.builder().name(scheduleReq.getName()).moim(moim).location(location).period(period).build();

        MoimSchedule moimScheduleEntity = moimScheduleRepository.save(moimSchedule);

        unionScheduleParticipant(scheduleReq.getUsers(), moimScheduleEntity);
        return moimScheduleEntity.getId();
    }

    private void unionScheduleParticipant(List<Long> users, MoimSchedule moimScheduleEntity) {
        Map<Long, Long> moimCategoryMap = categoryRepository.findMoimCategoriesByUsers(users).stream().collect(Collectors.toMap(MoimCategoryDto::getUserId, MoimCategoryDto::getCategoryId));
        for (Long userId : users) {
            User moimUsers = userRepository.getReferenceById(userId);
            Long categoryId = moimCategoryMap.get(moimUsers.getId());
            Category category = categoryRepository.getReferenceById(categoryId);
            MoimScheduleAndUser moimScheduleAndUser = MoimScheduleAndUser.builder().moimSchedule(moimScheduleEntity).user(moimUsers).category(category).build();
            moimScheduleAndUserRepository.save(moimScheduleAndUser);
        }
    }

    @Transactional(readOnly = false)
    public void updateSchedule(MoimScheduleRequest.PatchMoimScheduleDto scheduleReq) {
        MoimSchedule moimSchedule = moimScheduleRepository.findById(scheduleReq.getMoimScheduleId()).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_SCHEDULE_FAILURE));
        Period period = Period.builder().startDate(scheduleReq.getStartDate()).endDate(scheduleReq.getEndDate()).dayInterval(scheduleReq.getInterval()).build();
        Location location = Location.builder().locationName(scheduleReq.getLocationName()).x(scheduleReq.getX()).y(scheduleReq.getY()).build();
        moimSchedule.update(scheduleReq.getName(), period, location);
        moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
        unionScheduleParticipant(scheduleReq.getUsers(), moimSchedule);
    }

    @Transactional(readOnly = false)
    public void updateScheduleCategory(
            MoimScheduleRequest.PatchMoimScheduleCategoryDto scheduleReq,
            Long userId) {
        MoimSchedule moimSchedule = moimScheduleRepository.getReferenceById(scheduleReq.getMoimScheduleId());
        User user = userRepository.getReferenceById(userId);
        Category category = categoryRepository.findById(scheduleReq.getCategoryId()).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE));
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user).orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
        moimScheduleAndUser.updateCategory(category);
    }

    public List<MoimScheduleDto> findMoimSchedules(Long moimId, List<LocalDateTime> localDateTimes) {
        return scheduleRepository.findMonthScheduleInMoim(moimId, localDateTimes.get(0), localDateTimes.get(1));
    }

    @Transactional(readOnly = false)
    public void createScheduleAlarm(MoimScheduleRequest.PostMoimScheduleAlarmDto scheduleAlarmDto) {
        MoimSchedule moimSchedule = moimScheduleRepository.getReferenceById(scheduleAlarmDto.getMoimScheduleId());
        for (Integer alarmDate : scheduleAlarmDto.getAlarmDates()) {
            MoimScheduleAlarm moimScheduleAlarm = MoimScheduleAlarm.builder().alarmDate(alarmDate).moimSchedule(moimSchedule).build();
            moimScheduleAlarmRepository.save(moimScheduleAlarm);
        }
    }

    @Transactional(readOnly = false)
    public void updateScheduleAlarm(MoimScheduleRequest.PostMoimScheduleAlarmDto scheduleAlarmDto) {
        MoimSchedule moimSchedule = moimScheduleRepository.findById(scheduleAlarmDto.getMoimScheduleId()).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimSchedule(moimSchedule);
        for (Integer alarmDate : scheduleAlarmDto.getAlarmDates()) {
            MoimScheduleAlarm moimScheduleAlarm = MoimScheduleAlarm.builder().alarmDate(alarmDate).moimSchedule(moimSchedule).build();
            moimScheduleAlarmRepository.save(moimScheduleAlarm);
        }
    }

    @Transactional(readOnly = false)
    public void deleteSchedule(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndLocationsByMoimSchedule(moimSchedule);

//         모임 메모가 있는 경우 모임 메모 장소를 모두 삭제 후 모임 메모 삭제
        if (moimMemo != null) {
            moimMemo.getMoimMemoLocations().stream().forEach((moimMemoLocation -> moimMemoService.delete(moimMemoLocation.getId())));
            moimMemoRepository.delete(moimMemo);
        }

        moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
        moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimSchedule(moimSchedule);
        moimScheduleRepository.delete(moimSchedule);
    }

    @Transactional(readOnly = false)
    public void createMoimScheduleText(Long moimScheduleId,
                                       Long userId,
                                       MoimScheduleRequest.PostMoimScheduleTextDto moimScheduleText) {
        MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user).orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
        moimScheduleAndUser.updateText(moimScheduleText.getText());
    }
}
