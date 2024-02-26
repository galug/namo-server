package com.example.namo2.domain.memo.application;

import com.example.namo2.domain.memo.application.converter.MoimMemoConverter;
import com.example.namo2.domain.memo.application.converter.MoimMemoLocationConverter;
import com.example.namo2.domain.memo.application.converter.MoimMemoResponseConverter;
import com.example.namo2.domain.memo.application.impl.MoimMemoLocationService;
import com.example.namo2.domain.memo.application.impl.MoimMemoService;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.memo.ui.dto.MoimMemoRequest;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import com.example.namo2.domain.moim.application.impl.MoimScheduleService;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.user.UserService;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoimMemoFacade {
    private final MoimScheduleService moimScheduleService;
    private final MoimMemoService moimMemoService;
    private final MoimMemoLocationService moimMemoLocationService;
    private final UserService userService;

    private final FileUtils fileUtils;

    @Transactional(readOnly = false)
    public void create(Long moimScheduleId, MoimMemoRequest.LocationDto locationDto, List<MultipartFile> imgs) {
        MoimMemo moimMemo = getMoimMemo(moimScheduleId);
        MoimMemoLocation moimMemoLocation = createMoimMemoLocation(moimMemo, locationDto);

        createMoimMemoLocationAndUsers(locationDto, moimMemoLocation);
        createMoimMemoLocationImgs(imgs, moimMemoLocation);
    }

    private MoimMemo getMoimMemo(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        return moimMemoService.getMoimMemoOrNull(moimSchedule)
                .orElse(
                        moimMemoService.create(MoimMemoConverter.toMoimMemo(moimSchedule))
                );
    }

    private MoimMemoLocation createMoimMemoLocation(MoimMemo moimMemo, MoimMemoRequest.LocationDto locationDto) {
        MoimMemoLocation moimMemoLocation = MoimMemoLocationConverter.toMoimMemoLocation(moimMemo, locationDto);
        return moimMemoLocationService.createMoimMemoLocation(moimMemoLocation);
    }

    private void createMoimMemoLocationAndUsers(MoimMemoRequest.LocationDto locationDto, MoimMemoLocation moimMemoLocation) {
        List<User> users = userService.getUsers(locationDto.getParticipants());
        List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = MoimMemoLocationConverter
                .toMoimMemoLocationLocationAndUsers(moimMemoLocation, users);
        moimMemoLocationService.createMoimMemoLocationAndUsers(moimMemoLocationAndUsers);
    }

    /**
     * TODO: 적절한 validation: 처리 필요
     */
    private void createMoimMemoLocationImgs(List<MultipartFile> imgs, MoimMemoLocation moimMemoLocation) {
        if (imgs == null) {
            return;
        }
        /**
         * imgs 에 대한 validation 처리 필요
         * 값이 3개 이상일 경우 OVER_IMAGES_FAILURE 필요
         */
        List<String> urls = fileUtils.uploadImages(imgs);
        for (String url : urls) {
            MoimMemoLocationImg moimMemoLocationImg = MoimMemoLocationConverter
                    .toMoimMemoLocationLocationImg(moimMemoLocation, url);
            moimMemoLocationService.createMoimMemoLocationImg(moimMemoLocationImg);
        }
    }

    @Transactional(readOnly = false)
    public void modifyMoimMemoLocation(Long memoLocationId, MoimMemoRequest.LocationDto locationDto, List<MultipartFile> imgs) {
        MoimMemoLocation moimMemoLocation = moimMemoLocationService.getMoimMemoLocation(memoLocationId);
        moimMemoLocation.update(locationDto.getName(), locationDto.getMoney());

        moimMemoLocationService.removeMoimMemoLocationAndUsers(moimMemoLocation);
        createMoimMemoLocationAndUsers(locationDto, moimMemoLocation);

        removeMoimMemoLocationImgs(moimMemoLocation);
        createMoimMemoLocationImgs(imgs, moimMemoLocation);
    }

    private void removeMoimMemoLocationImgs(MoimMemoLocation moimMemoLocation) {
        List<String> urls = moimMemoLocation.getMoimMemoLocationImgs()
                .stream()
                .map(MoimMemoLocationImg::getUrl)
                .collect(Collectors.toList());
        fileUtils.deleteImages(urls);
        moimMemoLocationService.removeMoimMemoLocationImgs(moimMemoLocation);
    }

    @Transactional(readOnly = false)
    public void removeMoimMemoLocation(Long memoLocationId) {
        MoimMemoLocation moimMemoLocation = moimMemoLocationService.getMoimMemoLocation(memoLocationId);

        moimMemoLocationService.removeMoimMemoLocationAndUsers(moimMemoLocation);
        removeMoimMemoLocationImgs(moimMemoLocation);
        moimMemoLocationService.removeMoimMemoLocation(moimMemoLocation);
    }

    @Transactional(readOnly = false)
    public MoimMemoResponse.MoimMemoDto getMoimMemoWithLocations(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        MoimMemo moimMemo = moimMemoService.getMoimMemoWithUsers(moimSchedule);
        List<MoimMemoLocation> moimMemoLocations = moimMemoLocationService.getMoimMemoLocations(moimSchedule);
        List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = moimMemoLocationService.getMoimMemoLocationAndUsers(moimMemoLocations);
        return MoimMemoResponseConverter.toMoimMemoDto(moimMemo, moimMemoLocations, moimMemoLocationAndUsers);
    }
}
