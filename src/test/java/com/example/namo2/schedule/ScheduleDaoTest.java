package com.example.namo2.schedule;

import com.example.namo2.entity.User;
import com.example.namo2.schedule.dto.GetScheduleRes;
import com.example.namo2.user.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ScheduleDaoTest {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;

    @Autowired
    public ScheduleDaoTest(ScheduleDao scheduleDao, UserDao userDao) {
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
    }

    @Test
    public void 스케줄_찾기_테스트() {
        User user = userDao.findById(1L).get();
        List<GetScheduleRes> schedulesByUserId = scheduleDao.findSchedulesByUserId(user);
        for (GetScheduleRes getScheduleRes : schedulesByUserId) {
            System.out.println("getScheduleRes = " + getScheduleRes.getScheduleId() + getScheduleRes.getCategoryName());
        }
    }
}