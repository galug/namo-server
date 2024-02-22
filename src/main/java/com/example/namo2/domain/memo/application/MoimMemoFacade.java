package com.example.namo2.domain.memo.application;

import com.example.namo2.domain.memo.application.converter.MoimMemoConverter;
import com.example.namo2.domain.memo.application.converter.MoimMemoLocationConverter;
import com.example.namo2.domain.memo.application.impl.MoimMemoLocationService;
import com.example.namo2.domain.memo.application.impl.MoimMemoService;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.memo.ui.dto.MoimMemoRequest;
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

    private MoimMemoLocation createMoimMemoLocation(MoimMemo moimMemo, MoimMemoRequest.LocationDto locationDto) {
        MoimMemoLocation moimMemoLocation = MoimMemoLocationConverter.toMoimMemoLocation(moimMemo, locationDto);
        return moimMemoLocationService.createMoimMemoLocation(moimMemoLocation);
    }

    private MoimMemo getMoimMemo(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        return moimMemoService.getMoimMemo(moimSchedule)
                .orElse(
                        moimMemoService.create(MoimMemoConverter.toMoimMemo(moimSchedule))
                );
    }

    private void createMoimMemoLocationAndUsers(MoimMemoRequest.LocationDto locationDto, MoimMemoLocation savedMoimMemoLocation) {
        List<User> users = userService.getUsers(locationDto.getParticipants());
        List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = MoimMemoLocationConverter
                .toMoimMemoLocationLocationAndUsers(savedMoimMemoLocation, users);
        moimMemoLocationService.createMoimMemoLocationAndUsers(moimMemoLocationAndUsers);
    }

    /**
     * TODO: 적절한 validation: 처리 필요
     */
    private void createMoimMemoLocationImgs(List<MultipartFile> imgs, MoimMemoLocation moimMemoLocation) {
        if (imgs == null) {
            return;
        }
        // imgs 가 3을 넘어갈 시 적절한 경고 Exception 날리기
        List<String> urls = fileUtils.uploadImages(imgs);
        for (String url : urls) {
            MoimMemoLocationImg moimMemoLocationImg = MoimMemoLocationConverter
                    .toMoimMemoLocationLocationImg(moimMemoLocation, url);
            moimMemoLocationService.createMoimMemoLocationImg(moimMemoLocationImg);
        }
    }
}
