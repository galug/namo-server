package com.example.namo2.schedule.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class GetScheduleRes {
    private Long scheduleId;
    private String name;

    private Long startDate;

    private Long endDate;

    private Long alarmDate;

    private PointDto point;

    private Long categoryId;

    private String categoryName;

    private String color;

    @QueryProjection
    public GetScheduleRes(Long scheduleId, String name, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate, Point point, Long categoryId, String categoryName, String color) {
        this.scheduleId = scheduleId;
        this.name = name;
        this.startDate = startDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        ;
        this.endDate = endDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.alarmDate = Optional.ofNullable(alarmDate)
                .map((date) -> date.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .getEpochSecond())
                .orElse(0L);
        this.point = new PointDto(point);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.color = color;
    }

    @Getter @NoArgsConstructor
    private static class PointDto {
        private Double x;
        private Double y;

        public PointDto(Point point) {
            if (point == null) {
                this.x = null;
                this.y = null;
                return;
            }
            this.x = point.getX();
            this.y = point.getY();
        }
    }
}
