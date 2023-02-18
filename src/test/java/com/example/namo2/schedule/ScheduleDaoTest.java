package com.example.namo2.schedule;

import com.example.namo2.schedule.dto.DiaryDto;
import com.example.namo2.entity.User;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.Converter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ScheduleDaoTest {
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;

    private final Converter converter;
    @Autowired
    public ScheduleDaoTest(ScheduleDao scheduleDao, UserDao userDao, Converter converter) {
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
        this.converter = converter;
    }


    @Test
    public void 스케줄_찾기_테스트() {
        User user = userDao.findById(1L).get();

        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime("2023,2");

        List<DiaryDto> scheduleDiaryByMonthDto = scheduleDao.findScheduleDiaryByMonthDtoWithNotPaging(user, localDateTimes.get(0), localDateTimes.get(1));
        for (DiaryDto diaryDto : scheduleDiaryByMonthDto) {
            System.out.println("" + diaryDto.getScheduleId() + diaryDto.getName() + diaryDto.getTexts());
        }
    }
}