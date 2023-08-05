package com.example.namo2.moim;

import com.example.namo2.entity.moim.Moim;
import com.example.namo2.entity.moim.MoimAndUser;
import com.example.namo2.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimAndUserRepository extends JpaRepository<MoimAndUser, Long> {

    @Query(value = "select gu from MoimAndUser gu join fetch gu.moim where gu.user= :user")
    public List<MoimAndUser> findMoimAndUserByUser(User user);

    @Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim= :moim")
    public List<MoimAndUser> findMoimAndUserByMoim(Moim moim);

    public Optional<MoimAndUser> findMoimAndUserByUserAndMoim(User user, Moim moim);

    public Integer countMoimAndUserByMoim(Moim moim);

    public boolean existsMoimAndUserByMoimAndUser(Moim moim, User user);
}
