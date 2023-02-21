package com.example.namo2.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean hasDiary;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "schedule")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Schedule(Long id, String name, Period period, Point point, User user, Category category) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.point = point;
        this.user = user;
        this.category = category;
        hasDiary = false;
    }

    public void updateSchedule(String name, Period period, Point point, Category category) {
        this.name = name;
        this.period.updatePeriod(period.getStartDate(), period.getEndDate(), period.getAlarmDate());
        this.point = point;
        this.category = category;
    }

    public void updateDiaryContents(String contents) {
        this.hasDiary = Boolean.TRUE;
        this.contents = contents;
    }

    public void deleteDiary() {
        this.hasDiary = Boolean.FALSE;
        this.contents = null;
    }
}
