package com.example.namo2.entity;

import com.example.namo2.entity.enumerate.CategoryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palette_id")
    private Palette palette;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20)
    private String name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean share;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @Builder
    public Category(Palette palette, User user, String name, Boolean share) {
        this.palette = palette;
        this.user = user;
        this.name = name;
        this.share = share;
        this.status = CategoryStatus.ACTIVE;
    }

    public void update(String name, Boolean share, Palette palette) {
        this.name = name;
        this.share = share;
        this.palette = palette;
    }

    public void delete() {
        this.status = CategoryStatus.DELETE;
    }
}
