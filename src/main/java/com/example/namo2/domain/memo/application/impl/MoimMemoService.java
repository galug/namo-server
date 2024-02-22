package com.example.namo2.domain.memo.application.impl;

import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationAndUserRepository;
import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationImgRepository;
import com.example.namo2.domain.memo.dao.repository.MoimMemoLocationRepository;
import com.example.namo2.domain.memo.dao.repository.MoimMemoRepository;
import com.example.namo2.domain.memo.ui.dto.MoimMemoRequest;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import com.example.namo2.domain.moim.dao.repository.MoimScheduleRepository;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.UserRepository;
import com.example.namo2.domain.schedule.dto.DiaryDto;
import com.example.namo2.domain.schedule.dto.SliceDiaryDto;
import com.example.namo2.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimMemoService {
    private final FileUtils fileUtils;
    private final MoimMemoRepository moimMemoRepository;
    private final MoimScheduleRepository moimScheduleRepository;
    private final MoimMemoLocationRepository moimMemoLocationRepository;
    private final MoimMemoLocationAndUserRepository moimMemoLocationAndUserRepository;
    private final MoimMemoLocationImgRepository moimMemoLocationImgRepository;

    public MoimMemoResponse.MoimMemoDto find(Long moimScheduleId) {
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndUsersByMoimSchedule(moimScheduleId);
        MoimMemoResponse.MoimMemoDto moimMemoDto = new MoimMemoResponse.MoimMemoDto(moimMemo);
        List<MoimMemoResponse.MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocationRepository.findMoimMemo(moimScheduleId);
        moimMemoDto.addMoimMemoLocationDto(moimMemoLocationDtos);
        return moimMemoDto;
    }

    @Transactional(readOnly = false)
    public void update(Long moimLocationId, MoimMemoRequest.LocationDto locationDto, List<MultipartFile> imgs) {
//        MoimMemoLocation moimMemoLocation = moimMemoLocationRepository.findMoimMemoLocationById(moimLocationId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_DIARY_FAILURE));
//        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
//        deleteMoimImgs(moimMemoLocation);
//        moimMemoLocation.update(locationDto.getName(), locationDto.getMoney());
//        createMoimMemoLocationAndUser(locationDto.getParticipants(), moimMemoLocation);
//        createMoimMemoLocationImgs(imgs, moimMemoLocation);
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
//        MoimMemoLocation moimMemoLocation = moimMemoLocationRepository.findMoimMemoLocationById(moimLocationId)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_DIARY_FAILURE));
//        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
//        deleteMoimImgs(moimMemoLocation);
//
//        MoimMemo moimMemo = moimMemoLocation.getMoimMemo();
//        if (moimMemo.isLastMoimMemoLocations()) {
//            moimMemoRepository.delete(moimMemo);
//            return;
//        }
//        moimMemoLocationRepository.delete(moimMemoLocation);
    }

    public SliceDiaryDto<DiaryDto> findMonth(Long userId, List<LocalDateTime> localDateTimes, Pageable pageable) {
        return moimScheduleRepository.findMoimScheduleMemoByMonth(userId, localDateTimes, pageable);
    }

    public Optional<MoimMemo> getMoimMemo(MoimSchedule moimSchedule) {
        return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule);
    }

    public void removeMoimMemo(MoimMemo moimMemo) {
        moimMemoRepository.delete(moimMemo);
    }

    public MoimMemo create(MoimMemo moimMemo) {
        return moimMemoRepository.save(moimMemo);
    }
}
