package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimAndUserRepository extends JpaRepository<MoimAndUser, Long> {

    @Query(value = "select gu from MoimAndUser gu join fetch gu.moim where gu.user= :user")
    List<MoimAndUser> findMoimAndUserByUser(User user);

    @Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim= :moim")
    List<MoimAndUser> findMoimAndUserByMoim(Moim moim);

    Optional<MoimAndUser> findMoimAndUserByUserAndMoim(User user, Moim moim);

    Integer countMoimAndUserByMoim(Moim moim);

    boolean existsMoimAndUserByMoimAndUser(Moim moim, User user);
}
