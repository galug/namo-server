package com.example.namo2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupMemoImg {
    @Id
    @GeneratedValue
    @Column(name = "group_memo_img_id")
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_memo_location_idx")
    private GroupMemoLocation groupMemoLocation;
}
