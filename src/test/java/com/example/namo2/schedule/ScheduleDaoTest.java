package com.example.namo2.schedule;

import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.User;
import com.example.namo2.user.UserDao;
import com.example.namo2.user.UserService;
import com.example.namo2.utils.Converter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ScheduleDaoTest {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;

    private final Converter converter;
    private final UserService userService;

    @Autowired
    public ScheduleDaoTest(ScheduleDao scheduleDao, UserDao userDao, Converter converter, UserService userService) {
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
        this.converter = converter;
        this.userService = userService;
    }

    @Test
    public void 스케줄_찾기_테스트() {
    }
}