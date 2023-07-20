package com.example.namo2.entity.schedule;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import com.example.namo2.entity.BaseTimeEntity;
import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "event_id", nullable = false)
    private Long eventId;

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

    @Builder
    public Schedule(Long id, String name, Period period, User user, Category category, Double x, Double y, String locationName) {
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
}
