package com.example.namo2;

import com.example.namo2.category.CategoryDao;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Palette;
import com.example.namo2.entity.User;
import com.example.namo2.palette.PaletteDao;
import com.example.namo2.schedule.ScheduleDao;
import com.example.namo2.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final UserDao userDao;
        private final ScheduleDao scheduleDao;
        private final CategoryDao categoryDao;
        private final PaletteDao paletteDao;

        public void dbInit() {
            User user1 = User.builder()
                    .name("kim")
                    .email("email")
                    .build();
            userDao.save(user1);

            Palette palette = Palette.builder()
                    .belong("기본 팔레트")
                    .color("red")
                    .build();
            paletteDao.save(palette);

            Category c1 = Category.builder()
                    .name("카테고리")
                    .share(Boolean.FALSE)
                    .palette(palette)
                    .build();
            categoryDao.save(c1);
        }
    }
}