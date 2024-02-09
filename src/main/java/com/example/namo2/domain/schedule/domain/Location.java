package com.example.namo2.domain.schedule.domain;


import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {
    private Double x;
    private Double y;
    private String locationName;

    @Builder
    public Location(Double x, Double y, String locationName) {
        this.x = x;
        this.y = y;
        this.locationName = locationName;
    }

    public static Location create(Double x, Double y, String locationName) {
        return new Location(x, y, locationName);
    }

}
