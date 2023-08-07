package com.example.namo2.entity.moimmemo;

import com.example.namo2.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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
@Table(name = "moim_memo_location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimMemoLocation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_memo_location_id")
    private Long id;

    private String name;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_memo_id")
    private MoimMemo moimMemo;

    @Builder
    public MoimMemoLocation(Long id, String name, Integer totalAmount, MoimMemo moimMemo) {
        this.id = id;
        this.name = name;
        this.totalAmount = totalAmount;
        this.moimMemo = moimMemo;
    }
}
