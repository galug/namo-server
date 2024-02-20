package com.example.namo2.domain.moim.domain;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.schedule.domain.Location;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moim_schedule")
@Getter
@NoArgsConstructor
public class MoimSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_schedule_id")
    private Long id;

    private String name;

    @Embedded
    Period period;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @OneToMany(mappedBy = "moimSchedule", fetch = FetchType.LAZY)
    private List<MoimScheduleAndUser> moimScheduleAndUsers = new ArrayList<>();

    @OneToOne(mappedBy = "moimSchedule", fetch = FetchType.LAZY)
    private MoimMemo moimMemo;

    @Builder
    public MoimSchedule(Long id, String name, Period period, Location location, Moim moim) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.location = location;
        this.moim = moim;
    }

    public void registerMemo(MoimMemo moimMemo) {
        this.moimMemo = moimMemo;
    }

    public void update(String name, Period period, Location location) {
        this.name = name;
        this.period = period;
        this.location = location;
    }

    public Boolean isLastScheduleMember() {
        return moimScheduleAndUsers.size() == 1;
    }
}
