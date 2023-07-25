package ru.practicum.user.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);


    @Query("select u from User u where ?1 is null or u.id in ?1")
    List<User> findByIdIn(Set<Long> ids, Pageable pageable);


}