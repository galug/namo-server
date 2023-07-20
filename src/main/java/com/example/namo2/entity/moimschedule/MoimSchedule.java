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
import javax.persistence.Table;

import com.example.namo2.entity.BaseTimeEntity;
import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.schedule.Period;
import org.springframework.data.geo.Point;

@Entity
@Table(name = "moim_schedule")
public class MoimSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_schedule_id")
    private Long id;

    private String name;

    @Embedded
    Period period;

    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;
}
