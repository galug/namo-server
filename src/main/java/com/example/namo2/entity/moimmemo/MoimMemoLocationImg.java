package com.example.namo2.entity.moimmemo;

import com.example.namo2.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Table(name = "moim_memo_location_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimMemoLocationImg extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_memo_location_img_id")
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_memo_location_id")
    private MoimMemoLocation moimMemoLocation;

    @Builder
    public MoimMemoLocationImg(Long id, String url, MoimMemoLocation moimMemoLocation) {
        this.id = id;
        this.url = url;
        this.moimMemoLocation = moimMemoLocation;
        this.moimMemoLocation.getMoimMemoLocationImgs().add(this);
    }
}
