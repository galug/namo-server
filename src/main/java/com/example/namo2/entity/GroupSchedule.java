package com.example.namo2.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.data.geo.Point;

@Entity
public class GroupSchedule {
    @Id
    @GeneratedValue
    @Column(name = "group_schedule_id")
    private Long id;

    private String name;

    @Embedded
    Period period;

    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private Group group;
}
