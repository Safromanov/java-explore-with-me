package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatDataRepository extends JpaRepository<StatData, Long> {

    @Query("SELECT new ru.practicum.GetDto(s.app, s.uri,  count(s.id)) " +
            "FROM StatData s where s.uri in ?3  " +
            "and  s.timestamp > ?1 and s.timestamp < ?2 " +
            "GROUP BY s.uri")
    List<GetDto> getStatisticForUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.GetDto(s.app, s.uri,  count(s.id)) " +
            "FROM StatData s where s.uri in ?3  " +
            "and  s.timestamp > ?1 and s.timestamp < ?2 " +
            "GROUP BY s.uri, s.ip")
    List<GetDto> getStatisticForUrisWithUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);
}