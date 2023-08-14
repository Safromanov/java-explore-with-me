package ru.practicum.compilations.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation c where ?1 is null or c.pinned = ?1")
    List<Compilation> findByParams(@Nullable Boolean pinned, Pageable pageable);
}