package com.example.namo2.entity.moimschedule;

import com.example.namo2.entity.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

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
@Table(name = "moim_schedule_alarm")
@Getter
@NoArgsConstructor
public class MoimScheduleAlarm {
    @Id @Column(name = "moim_schedule_alarm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "alarm_date")
    private Integer alarmDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_schedule_id")
    private MoimSchedule moimSchedule;

    @Builder
    public MoimScheduleAlarm(Long id, Integer alarmDate, MoimSchedule moimSchedule) {
        this.id = id;
        this.alarmDate = alarmDate;
        this.moimSchedule = moimSchedule;
        moimSchedule.getMoimScheduleAlarms().add(this);
    }
}
