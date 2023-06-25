package com.example.namo2.moim;

import com.example.namo2.entity.GroupAndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimAndUserRepository extends JpaRepository<GroupAndUser, Long> {
}
