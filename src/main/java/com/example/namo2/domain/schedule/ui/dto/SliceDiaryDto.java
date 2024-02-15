package com.example.namo2.domain.schedule.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@NoArgsConstructor
public class SliceDiaryDto<T> {
    private List<T> content;
    private int currentPage;
    private int size;
    private boolean first;
    private boolean last;

    public SliceDiaryDto(Slice<T> slice) {
        this.content = slice.getContent();
        this.currentPage = slice.getNumber();
        this.size = content.size();
        this.first = slice.isFirst();
        this.last = slice.isLast();
    }
}
