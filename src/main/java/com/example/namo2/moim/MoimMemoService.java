package com.example.namo2.moim;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.moimmemo.MoimMemo;
import com.example.namo2.entity.moimmemo.MoimMemoLocation;
import com.example.namo2.entity.moimmemo.MoimMemoLocationAndUser;
import com.example.namo2.entity.moimmemo.MoimMemoLocationImg;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.user.User;
import com.example.namo2.moim.dto.LocationInfo;
import com.example.namo2.moim.dto.MoimMemoDto;
import com.example.namo2.moim.dto.MoimMemoLocationDto;
import com.example.namo2.auth.UserRepository;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public void create(Long moimScheduleId, List<LocationInfo> locationInfos, List<List<MultipartFile>> imgs) {
        MoimSchedule moimSchedule = moimScheduleRepository.getReferenceById(moimScheduleId);
        existsMoimMemo(moimSchedule);
        MoimMemo moimMemo = createMoimMemo(moimSchedule);
        for (int i = 0; i < locationInfos.size(); i++) {
            List<MultipartFile> locationImgs = imgs.get(i);
            LocationInfo locationInfo = locationInfos.get(i);
            MoimMemoLocation moimMemoLocation = createMoimMemoLocation(moimMemo, locationInfo);
            createMoimMemoLocationAndUser(locationInfo.getParticipants(), moimMemoLocation);
            createMoimMemoLocationImgs(locationImgs, moimMemoLocation);
        }
    }

    private void existsMoimMemo(MoimSchedule moimSchedule) {
        if (moimMemoRepository.existsMoimMemoByMoimSchedule(moimSchedule)) {
            throw new BaseException(BaseResponseStatus.DUPLICATE_MOIM_MEMO_FAILURE);
        }
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
        if (locationImgs != null) {
            List<String> urls = fileUtils.uploadImages(locationImgs);
            for (String url : urls) {
                MoimMemoLocationImg moimMemoLocationImg = MoimMemoLocationImg.builder()
                        .moimMemoLocation(moimMemoLocation)
                        .url(url)
                        .build();
                moimMemoLocationImgRepository.save(moimMemoLocationImg);
            }
        }
    }

    public MoimMemoDto find(Long moimScheduleId) {
        MoimMemo moimMemo = moimMemoRepository.findMoimMemoByMoimSchedule(moimScheduleId);
        MoimMemoDto moimMemoDto = new MoimMemoDto(moimMemo);
        List<MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocationRepository.findMoimMemo(moimScheduleId);
        moimMemoDto.addMoimMemoLocationDto(moimMemoLocationDtos);
        return moimMemoDto;
    }
}