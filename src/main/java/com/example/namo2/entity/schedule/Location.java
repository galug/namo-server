package com.example.namo2.entity.schedule;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {
    private Double x;
    private Double y;
    private String locationName;

    public Location(Double x, Double y, String locationName) {
        this.x = x;
        this.y = y;
        this.locationName = locationName;
    }
}
