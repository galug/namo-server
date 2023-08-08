package com.example.namo2.moim;

import com.example.namo2.category.CategoryRepository;
import com.example.namo2.category.dto.MoimCategoryDto;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.moim.MoimAndUser;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import com.example.namo2.entity.schedule.Location;
import com.example.namo2.entity.schedule.Period;
import com.example.namo2.entity.user.User;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.GetMoimUserRes;
import com.example.namo2.moim.dto.PatchMoimName;
import com.example.namo2.moim.dto.PostMoimScheduleReq;
import com.example.namo2.moim.dto.MoimScheduleRes;
import com.example.namo2.schedule.ScheduleRepository;
import com.example.namo2.user.UserRepository;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimService {
    private final static int[] MOIM_USERS_COLOR = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    private final MoimRepository moimRepository;
    private final MoimAndUserRepository moimAndUserRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final FileUtils fileUtils;
    private final EntityManager em;

    @Transactional(readOnly = false)
    public Long create(Long userId, String groupName, MultipartFile img) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        String url = fileUtils.uploadImage(img);
        Moim moim = Moim.builder()
                .name(groupName)
                .imgUrl(url)
                .build();
        Moim savedMoim = moimRepository.save(moim);

        Integer color = MOIM_USERS_COLOR[0];
        MoimAndUser moimAndUser = MoimAndUser.builder()
                .user(user)
                .moim(savedMoim)
                .moimCustomName(groupName)
                .color(color)
                .build();
        moimAndUserRepository.save(moimAndUser);

        return savedMoim.getId();
    }

    public List<GetMoimRes> findMoims(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        List<MoimAndUser> moimAndUsers = moimAndUserRepository.findMoimAndUserByUser(user);

        List<GetMoimRes> moims = new ArrayList<>();

        for (MoimAndUser moimAndUser : moimAndUsers) {
            Moim moim = moimAndUser.getMoim();
            List<MoimAndUser> groupUsers = moimAndUserRepository.findMoimAndUserByMoim(moim);
            List<GetMoimUserRes> moimUsers = groupUsers.stream()
                    .map((groupUser) -> new GetMoimUserRes(groupUser.getUser().getId(), groupUser.getUser().getName(), groupUser.getColor()))
                    .collect(Collectors.toList());
            moims.add(new GetMoimRes(moim.getId(), moimAndUser.getMoimCustomName(),
                    moim.getImgUrl(), moim.getCode(), moimUsers));
        }
        return moims;
    }

    @Transactional(readOnly = false)
    public Long patchMoimName(PatchMoimName patchMoimName, Long userId) {
        User user = em.getReference(User.class, userId);
        Moim moim = em.getReference(Moim.class, patchMoimName.getMoimId());
        MoimAndUser moimAndUser = moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        moimAndUser.updateCustomName(patchMoimName.getMoimName());
        return moim.getId();
    }

    @Transactional(readOnly = false)
    public Long participate(Long userId, String code) {
        Moim moim = moimRepository.findMoimByCode(code)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
        User user = userRepository.getReferenceById(userId);
        duplicateParticipate(moim, user);
        Integer numberOfMoimer = moimAndUserRepository.countMoimAndUserByMoim(moim);
        MoimAndUser moimAndUser = MoimAndUser.builder()
                .user(user)
                .moim(moim)
                .moimCustomName(moim.getName())
                .color(MOIM_USERS_COLOR[numberOfMoimer])
                .build();
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
        MoimAndUser moimAndUser = moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        moimAndUserRepository.delete(moimAndUser);
    }

    @Transactional(readOnly = false)
    public Long createSchedule(PostMoimScheduleReq scheduleReq) {
        Moim moim = moimRepository.getReferenceById(scheduleReq.getMoimId());
        Period period = Period.builder()
                .startDate(scheduleReq.getStartDate())
                .endDate(scheduleReq.getEndDate())
                .dayInterval(scheduleReq.getInterval())
                .build();
        Location location = Location.create(scheduleReq.getX(), scheduleReq.getY(), scheduleReq.getLocationName());
        MoimSchedule moimSchedule = MoimSchedule.builder()
                .name(scheduleReq.getName())
                .moim(moim)
                .location(location)
                .period(period)
                .build();

        MoimSchedule moimScheduleEntity = moimScheduleRepository.save(moimSchedule);

        Map<Long, Long> moimCategoryMap = categoryRepository
                .findMoimCategoriesByUsers(scheduleReq.getUsers())
                .stream().collect(Collectors.toMap(MoimCategoryDto::getUserId, MoimCategoryDto::getCategoryId));
        for (Long userId : scheduleReq.getUsers()) {
            User moimUsers = userRepository.getReferenceById(userId);
            Long categoryId = moimCategoryMap.get(moimUsers.getId());
            Category category = categoryRepository.getReferenceById(categoryId);
            MoimScheduleAndUser moimScheduleAndUser = MoimScheduleAndUser
                    .builder()
                    .moimSchedule(moimScheduleEntity)
                    .user(moimUsers)
                    .category(category)
                    .build();
            moimScheduleAndUserRepository.save(moimScheduleAndUser);
        }
        return moimScheduleEntity.getId();

    }

    public List<MoimScheduleRes> findMoimSchedules(Long moimId, List<LocalDateTime> localDateTimes) {
        return scheduleRepository.findScheduleInMoim(moimId, localDateTimes.get(0), localDateTimes.get(1));
    }
}
