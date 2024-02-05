package com.example.namo2.entity.moimschedule;

import com.example.namo2.entity.BaseTimeEntity;
import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.moimmemo.MoimMemo;
import com.example.namo2.entity.schedule.Location;
import com.example.namo2.entity.schedule.Period;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "moimSchedule", fetch = FetchType.LAZY)
    private List<MoimScheduleAlarm> moimScheduleAlarms = new ArrayList<>();

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
