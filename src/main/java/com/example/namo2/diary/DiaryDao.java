package com.example.namo2.diary;

import com.example.namo2.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryDao extends JpaRepository<Diary, Long> {
}
