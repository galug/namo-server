package com.example.namo2.entity.moimmemo;

import com.example.namo2.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moim_memo_location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @OneToMany(mappedBy = "moimMemoLocation", fetch =  FetchType.LAZY)
    private List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = new ArrayList<>();

    @OneToMany(mappedBy = "moimMemoLocation", fetch = FetchType.LAZY)
    private List<MoimMemoLocationImg> moimMemoLocationImgs = new ArrayList<>();

    @Builder
    public MoimMemoLocation(Long id, String name, Integer totalAmount, MoimMemo moimMemo) {
        this.id = id;
        this.name = name;
        this.totalAmount = totalAmount;
        this.moimMemo = moimMemo;
    }

    public void update(String name, Integer totalAmount) {
        this.name = name;
        this.totalAmount = totalAmount;
    }
}
