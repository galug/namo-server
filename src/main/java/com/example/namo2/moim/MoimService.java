package com.example.namo2.moim;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.Group;
import com.example.namo2.entity.GroupAndUser;
import com.example.namo2.entity.User;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.GetMoimUserRes;
import com.example.namo2.moim.dto.PatchMoimName;
import com.example.namo2.user.UserDao;
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
    private final UserDao userDao;
    private final FileUtils fileUtils;
    private final EntityManager em;

    @Transactional(readOnly = false)
    public Long create(Long userId, String groupName, MultipartFile img, String color) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        String url = fileUtils.uploadImage(img);
        Group group = Group.builder()
                .name(groupName)
                .imgUrl(url)
                .build();
        Group savedGroup = moimRepository.save(group);
        GroupAndUser groupAndUser = GroupAndUser.builder()
                .user(user)
                .group(savedGroup)
                .groupCustomName(groupName)
                .color(color)
                .build();
        moimAndUserRepository.save(groupAndUser);

        return savedGroup.getId();
    }

    public List<GetMoimRes> findMoims(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
        List<GroupAndUser> groupAndUsers = moimAndUserRepository.findGroupAndUserByUser(user);

        List<GetMoimRes> moims = new ArrayList<>();

        for (GroupAndUser groupAndUser : groupAndUsers) {
            Group group = groupAndUser.getGroup();
            List<GroupAndUser> groupUsers = moimAndUserRepository.findGroupAndUserByGroup(group);
            List<GetMoimUserRes> moimUsers = groupUsers.stream()
                    .filter((groupUser) -> groupUser.getUser() != user)
                    .map((groupUser) -> new GetMoimUserRes(groupUser.getUser().getId(), groupUser.getUser().getName()))
                    .collect(Collectors.toList());
            moims.add(new GetMoimRes(group.getId(), groupAndUser.getGroupCustomName(),
                    group.getImgUrl(), group.getCode(), moimUsers));
        }
        return moims;
    }

    @Transactional(readOnly = false)
    public Long patchMoimName(PatchMoimName patchMoimName, Long userId) {
        User user = em.getReference(User.class, userId);
        Group moim = em.getReference(Group.class, patchMoimName.getMoimId());
        GroupAndUser groupAndUser = moimAndUserRepository.findGroupAndUserByUserAndGroup(user, moim)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
        groupAndUser.updateCustomName(patchMoimName.getMoimName());
        return moim.getId();
    }
}
