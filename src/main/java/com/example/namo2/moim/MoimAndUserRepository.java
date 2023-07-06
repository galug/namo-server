package com.example.namo2.moim;

import com.example.namo2.entity.Group;
import com.example.namo2.entity.GroupAndUser;
import com.example.namo2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimAndUserRepository extends JpaRepository<GroupAndUser, Long> {

    @Query(value = "select gu from GroupAndUser gu join fetch gu.group where gu.user= :user")
    public List<GroupAndUser> findGroupAndUserByUser(User user);

    @Query(value = "select gu from GroupAndUser gu join fetch gu.user where gu.group= :group")
    public List<GroupAndUser> findGroupAndUserByGroup(Group group);

//    @Query(value = "select gu from GroupAndUser gu join fetch gu.group g join fetch g. where gu.group = :user")
}
