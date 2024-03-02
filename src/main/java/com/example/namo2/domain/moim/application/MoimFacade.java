package com.example.namo2.domain.moim.application;

import com.example.namo2.domain.moim.application.converter.MoimAndUserConverter;
import com.example.namo2.domain.moim.application.converter.MoimConverter;
import com.example.namo2.domain.moim.application.converter.MoimResponseConverter;
import com.example.namo2.domain.moim.application.impl.MoimAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimService;
import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimRequest;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;
import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MoimFacade {
    private final static int[] MOIM_USERS_COLOR = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    private final MoimService moimService;
    private final UserService userService;
    private final MoimAndUserService moimAndUserService;
    private final FileUtils fileUtils;

    @Transactional(readOnly = false)
    public MoimResponse.MoimIdDto createMoim(Long userId, String groupName, MultipartFile img) {
        User user = userService.getUser(userId);
        String url = fileUtils.uploadImage(img);
        Moim moim = MoimConverter.toMoim(groupName, url);
        moimService.create(moim);

        MoimAndUser moimAndUser = MoimAndUserConverter
                .toMoimAndUser(groupName, MOIM_USERS_COLOR[0], user, moim);
        moimAndUserService.create(moimAndUser);
        return MoimResponseConverter.toMoimIdDto(moim);
    }

    @Transactional(readOnly = true)
    public List<MoimResponse.MoimDto> getMoims(Long userId) {
        User user = userService.getUser(userId);
        List<Moim> moimsInUser = moimAndUserService.getMoimAndUsers(user)
                .stream().map(MoimAndUser::getMoim)
                .collect(Collectors.toList());

        List<MoimAndUser> moimAndUsersInMoims = moimAndUserService
                .getMoimAndUsers(moimsInUser);
        return MoimResponseConverter.toMoimDtos(moimAndUsersInMoims);
    }

    @Transactional(readOnly = false)
    public Long modifyMoimName(MoimRequest.PatchMoimNameDto patchMoimNameDto, Long userId) {
        User user = userService.getUser(userId);
        Moim moim = moimService.getMoim(patchMoimNameDto.getMoimId());
        MoimAndUser moimAndUser = moimAndUserService.getMoimAndUser(moim, user);
        moimAndUser.updateCustomName(patchMoimNameDto.getMoimName());
        return moim.getId();
    }

    @Transactional(readOnly = false)
    public Long createMoimAndUser(Long userId, String code) {
        User user = userService.getUser(userId);
        Moim moim = moimService.getMoim(code);

        moimAndUserService.validateExistsMoimAndUser(moim, user);

        Integer numberOfMoimMembers = moimAndUserService.getMoimMemberSize(moim);
        MoimAndUser moimAndUser = MoimAndUserConverter
                .toMoimAndUser(moim.getName(), MOIM_USERS_COLOR[numberOfMoimMembers], user, moim);

        moimAndUserService.create(moimAndUser);
        return moim.getId();
    }

    /**
     * 마지막 사람이 지워질 시
     * 모임에 대한 삭제 로직이 필요할 듯 합니다.
     * 고민점
     * 1. 모임이라는 로직에 엮여 있는 테이블이 많다보니 연쇄적으로 많은 테이블에 있는 많은
     *    row 들이 지워지는데 성능상 문제가 없을지 고민해볼 필요가 있을 것 같습니다.
     * 2. S3에서 지금까지는 모임 관련 메모들을 지우면 사진을 모두 지우는 중인데
     *    S3에서 이미지 파일을 지우면서 얻는 메모리적 이점이 클지
     *    아니면 S3에서 이미지 파일을 냅두면서 얻을 수 있는 시간적 이점이 클지 고민해볼 필요가 있을 듯합니다.
     *    그런데 사실 개인 정보 관련 부분이라 지우는게 맞는듯하긴할것같아요.
     */
    @Transactional(readOnly = false)
    public void removeMoimAndUser(Long userId, Long moimId) {
        User user = userService.getUser(userId);
        Moim moim = moimService.getMoim(moimId);
        MoimAndUser moimAndUser = moimAndUserService.getMoimAndUser(moim, user);
        moimAndUserService.removeMoimAndUser(moimAndUser);
    }
}
