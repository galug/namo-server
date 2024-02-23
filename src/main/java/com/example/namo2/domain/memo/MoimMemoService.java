package com.example.namo2.domain.memo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.ui.dto.LocationInfo;
import com.example.namo2.domain.moim.ui.dto.MoimMemoDto;
import com.example.namo2.domain.moim.ui.dto.MoimMemoLocationDto;
import com.example.namo2.domain.schedule.ui.dto.DiaryDto;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.example.namo2.domain.schedule.ui.dto.SliceDiaryDto;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimMemoService {
    private final FileUtils fileUtils;
    private final MoimMemoRepository moimMemoRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimMemoLocationRepository moimMemoLocationRepository;
    private final MoimMemoLocationAndUserRepository moimMemoLocationAndUserRepository;
    private final MoimMemoLocationImgRepository moimMemoLocationImgRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public void create(Long moimScheduleId, LocationInfo locationInfo, List<MultipartFile> imgs) {
        MoimSchedule moimSchedule = moimScheduleRepository.getReferenceById(moimScheduleId);
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule);
        MoimMemoLocation moimMemoLocation = createMoimMemoLocation(moimMemo, locationInfo);
        createMoimMemoLocationAndUser(locationInfo.getParticipants(), moimMemoLocation);
        createMoimMemoLocationImgs(imgs, moimMemoLocation);
    }

    private MoimMemo createMoimMemo(MoimSchedule moimSchedule) {
        MoimMemo moimMemo = MoimMemo.builder()
                .moimSchedule(moimSchedule)
                .build();
        return moimMemoRepository.save(moimMemo);
    }

    private MoimMemoLocation createMoimMemoLocation(MoimMemo moimMemo, LocationInfo locationInfo) {
        MoimMemoLocation moimMemoLocation = MoimMemoLocation.builder()
                .moimMemo(moimMemo)
                .name(locationInfo.getName())
                .totalAmount(locationInfo.getMoney())
                .build();
        return moimMemoLocationRepository.save(moimMemoLocation);
    }

    private void createMoimMemoLocationAndUser(List<Long> participants, MoimMemoLocation moimMemoLocation) {
        for (Long participant : participants) {
            User user = userRepository.getReferenceById(participant);
            MoimMemoLocationAndUser moimMemoLocationAndUser = MoimMemoLocationAndUser.builder()
                    .moimMemoLocation(moimMemoLocation)
                    .user(user)
                    .build();
            moimMemoLocationAndUserRepository.save(moimMemoLocationAndUser);
        }
    }

    private void createMoimMemoLocationImgs(List<MultipartFile> locationImgs, MoimMemoLocation moimMemoLocation) {
        if (locationImgs == null) {
            return;
        }
        List<String> urls = fileUtils.uploadImages(locationImgs);
        for (String url : urls) {
            MoimMemoLocationImg moimMemoLocationImg = MoimMemoLocationImg.builder()
                    .moimMemoLocation(moimMemoLocation)
                    .url(url)
                    .build();
            moimMemoLocationImgRepository.save(moimMemoLocationImg);
        }
    }

    public MoimMemoDto find(Long moimScheduleId) {
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndUsersByMoimSchedule(moimScheduleId);
        MoimMemoDto moimMemoDto = new MoimMemoDto(moimMemo);
        List<MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocationRepository.findMoimMemo(moimScheduleId);
        moimMemoDto.addMoimMemoLocationDto(moimMemoLocationDtos);
        return moimMemoDto;
    }

    @Transactional(readOnly = false)
    public void update(Long moimLocationId, LocationInfo locationInfo, List<MultipartFile> imgs) {
        MoimMemoLocation moimMemoLocation = moimMemoLocationRepository.findMoimMemoLocationById(moimLocationId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_DIARY_FAILURE));
        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
        deleteMoimImgs(moimMemoLocation);
        moimMemoLocation.update(locationInfo.getName(), locationInfo.getMoney());
        createMoimMemoLocationAndUser(locationInfo.getParticipants(), moimMemoLocation);
        createMoimMemoLocationImgs(imgs, moimMemoLocation);
    }

    private void deleteMoimImgs(MoimMemoLocation moimMemoLocation) {
        List<String> deleteUrls = moimMemoLocation.getMoimMemoLocationImgs()
                .stream()
                .map(MoimMemoLocationImg::getUrl)
                .collect(Collectors.toList());
        moimMemoLocationImgRepository.deleteMoimMemoLocationImgByMoimMemoLocation(moimMemoLocation);
        fileUtils.deleteImages(deleteUrls);
    }

    @Transactional(readOnly = false)
    public void removeMoimMemoLocation(Long moimLocationId) {
        MoimMemoLocation moimMemoLocation = moimMemoLocationRepository.findMoimMemoLocationById(moimLocationId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_DIARY_FAILURE));
        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
        deleteMoimImgs(moimMemoLocation);

        MoimMemo moimMemo = moimMemoLocation.getMoimMemo();
        if (moimMemo.isLastMoimMemoLocations()) {
            moimMemoRepository.delete(moimMemo);
            return;
        }
        moimMemoLocationRepository.delete(moimMemoLocation);
    }

    public ScheduleResponse.SliceDiaryDto findMonth(Long userId, List<LocalDateTime> localDateTimes, Pageable pageable) {
        return moimScheduleRepository.findMoimScheduleMemoByMonth(userId, localDateTimes, pageable);
    }

    public MoimMemo getMoimMemo(MoimSchedule moimSchedule) {
        return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule);
    }

    public void removeMoimMemo(MoimMemo moimMemo) {
        moimMemoRepository.delete(moimMemo);
    }
}
