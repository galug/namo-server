package com.example.namo2.entity.moimschedule;

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
import javax.persistence.Table;

import com.example.namo2.entity.BaseTimeEntity;
import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.schedule.Location;
import com.example.namo2.entity.schedule.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    @Builder
    public MoimSchedule(Long id, String name, Period period, Location location, Moim moim) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.location = location;
        this.moim = moim;
    }
}
