//package com.example.namo2;
//
//import com.example.namo2.category.CategoryDao;
//import com.example.namo2.entity.Category;
//import com.example.namo2.entity.Palette;
//import com.example.namo2.entity.Period;
//import com.example.namo2.entity.Schedule;
//import com.example.namo2.entity.User;
//import com.example.namo2.palette.PaletteDao;
//import com.example.namo2.schedule.ScheduleDao;
//import com.example.namo2.user.UserDao;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.geo.Point;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class InitDB {
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//        private final UserDao userDao;
//        private final ScheduleDao scheduleDao;
//        private final CategoryDao categoryDao;
//        private final PaletteDao paletteDao;
//
//        public void dbInit() {
//            User user1 = User.builder()
//                    .name("kim")
//                    .email("email")
//                    .build();
//            userDao.save(user1);
//
//            Palette palette = Palette.builder()
//                    .belong("기본 팔레트")
//                    .color("red")
//                    .build();
//            paletteDao.save(palette);
//
//            Category c1 = Category.builder()
//                    .name("카테고리")
//                    .share(Boolean.FALSE)
//                    .palette(palette)
//                    .build();
//            categoryDao.save(c1);
//
//            List<Integer> months = List.of(1, 2, 3);
//            List<Integer> days = List.of(1, 11, 21);
//
//            List<Long> epochs = new ArrayList<>();
//            for (Integer month : months) {
//                for (Integer day : days) {
//                    LocalDateTime timestamp = LocalDateTime.of(2023, month, day, 3, 30);
//                    long epochSecond = timestamp.atZone(ZoneId.systemDefault())
//                            .toInstant()
//                            .getEpochSecond();
//                    epochs.add(epochSecond);
//                }
//            }
//
//            for (int i = 0; i < epochs.size() - 1; i++) {
//                Period period = new Period(epochs.get(i), epochs.get(i + 1), 0L);
//                Schedule build = Schedule.builder()
//                        .name("aaa" + i)
//                        .period(period)
//                        .point(new Point(2.234232, 3.4234242))
//                        .user(user1)
//                        .category(c1)
//                        .build();
//                scheduleDao.save(build);
//            }
//        }
//    }
//}