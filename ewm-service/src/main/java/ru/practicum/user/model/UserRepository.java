package ru.practicum.user.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where :ids is null or u.id in :ids")
    Page<User> findByIdIn(@Param("ids") Set<Long> ids, Pageable pageable);


}