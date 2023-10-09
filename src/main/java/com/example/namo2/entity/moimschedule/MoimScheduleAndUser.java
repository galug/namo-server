package com.example.namo2.entity.moimschedule;

import com.example.namo2.entity.BaseTimeEntity;
import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "moim_schedule_and_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimScheduleAndUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_schedule_user_id")
    private Long id;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_schedule_id")
    private MoimSchedule moimSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public MoimScheduleAndUser(Long id, String memo, User user, MoimSchedule moimSchedule, Category category) {
        this.id = id;
        this.memo = memo;
        this.user = user;
        this.moimSchedule = moimSchedule;
        this.category = category;
        moimSchedule.getMoimScheduleAndUsers().add(this);
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateText(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "MoimScheduleAndUser{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", moimSchedule=" + moimSchedule.getId() +
                '}';
    }
}
