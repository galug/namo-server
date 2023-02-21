package com.example.namo2.schedule;

import com.example.namo2.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.example.namo2.entity.QImage.image;

public class ImageDaoImpl implements ImageDaoCustom{
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ImageDaoImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public void deleteDiaryImages(Schedule schedule) {
        queryFactory
                .delete(image)
                .where(image.schedule.eq(schedule))
                .execute();
        em.flush();
        em.clear();
    }
}
