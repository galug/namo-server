package com.example.namo2.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {
    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    private String name;

    @Embedded
    Period period;

    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Schedule(Long id, String name, Period period, Point point, User user, Category category) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.point = point;
        this.user = user;
        this.category = category;
    }

    public static Schedule createSchedule(String name, Period period, Point point, User user, Category category) {
        return builder()
                .name(name)
                .period(period)
                .point(point)
                .user(user)
                .category(category)
                .build();
    }
}
