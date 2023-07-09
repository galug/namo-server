package com.example.namo2.moim;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.Moim;
import com.example.namo2.entity.MoimAndUser;
import com.example.namo2.entity.User;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.GetMoimUserRes;
import com.example.namo2.moim.dto.PatchMoimName;
import com.example.namo2.user.UserRepository;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimService {
    private final MoimRepository moimRepository;
    private final MoimAndUserRepository moimAndUserRepository;
    private final UserRepository userDao;
    private final FileUtils fileUtils;
    private final EntityManager em;

    @Transactional(readOnly = false)
    public Long create(Long userId, String groupName, MultipartFile img, String color) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        String url = fileUtils.uploadImage(img);
        Moim moim = Moim.builder()
                .name(groupName)
                .imgUrl(url)
                .build();
        Moim savedMoim = moimRepository.save(moim);
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
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        List<MoimAndUser> moimAndUsers = moimAndUserRepository.findMoimAndUserByUser(user);

        List<GetMoimRes> moims = new ArrayList<>();

        for (MoimAndUser moimAndUser : moimAndUsers) {
            Moim moim = moimAndUser.getMoim();
            List<MoimAndUser> groupUsers = moimAndUserRepository.findMoimAndUserByMoim(moim);
            List<GetMoimUserRes> moimUsers = groupUsers.stream()
                    .filter((groupUser) -> groupUser.getUser() != user)
                    .map((groupUser) -> new GetMoimUserRes(groupUser.getUser().getId(), groupUser.getUser().getName()))
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
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        MoimAndUser moimAndUser = MoimAndUser.builder()
                .user(user)
                .moim(moim)
                .moimCustomName(moim.getName())
                .color("#42451f")
                .build();
        moimAndUserRepository.save(moimAndUser);
        return moim.getId();
    }

    @Transactional(readOnly = false)
    public void withdraw(Long userId, Long moimId) {
        User user = em.getReference(User.class, userId);
        Moim moim = em.getReference(Moim.class, moimId);
        MoimAndUser moimAndUser = moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        moimAndUserRepository.delete(moimAndUser);
    }
}
