package com.example.namo2.domain.schedule.domain;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.common.entity.BaseTimeEntity;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    Period period;

    @Embedded
    private Location location;

    @Column(name = "has_diary", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean hasDiary;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    public Schedule(Long id, String name, Period period, User user, Category category, Double x, Double y, String locationName, Integer eventId) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.location = new Location(x, y, locationName);
        this.user = user;
        this.category = category;
        hasDiary = false;
    }

    public void updateSchedule(String name, Period period, Category category, Double x, Double y, String locationName) {
        this.name = name;
        this.period = period;
        this.location = new Location(x, y, locationName);
        this.category = category;
    }

    public void updateDiaryContents(String contents) {
        this.hasDiary = Boolean.TRUE;
        this.contents = contents;
    }

    public void deleteDiary() {
        existDairy();
        this.hasDiary = Boolean.FALSE;
        this.contents = null;
    }

    public void existDairy() {
        if (!hasDiary) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_DIARY_FAILURE);
        }
    }

    public void setImgs(List<Image> imgs){
        this.images = imgs;
    }
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }
    public void addAlarms(List<Alarm> alarmList) {
        this.alarms.addAll(alarmList);
    }


    public List<Integer> findAlarms() {
        return alarms.stream()
                .map((alarm -> alarm.getAlarmDate()))
                .collect(Collectors.toList());
    }

    public void clearAlarm() {
        alarms.clear();
    }
}
