package com.example.namo2.domain.moim.domain;

import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.entity.BaseTimeEntity;
import com.example.namo2.domain.category.domain.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
