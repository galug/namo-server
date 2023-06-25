package com.example.namo2.moim;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.Group;
import com.example.namo2.entity.GroupAndUser;
import com.example.namo2.entity.User;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimService {
    private final MoimRepository moimRepository;
    private final MoimAndUserRepository moimAndUserRepository;
    private final UserDao userDao;
    private final FileUtils fileUtils;

    public Long create(Long userId, String groupName, MultipartFile img, String color) {
        User user = userDao.findById(userId)
                .orElseThrow(() ->  new BaseException(BaseResponseStatus.NOT_FOUND_USER_FAILURE));
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
}
