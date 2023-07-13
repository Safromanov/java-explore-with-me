package ru.practicum.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.StatData;

import java.time.LocalDateTime;
import java.util.List;

public interface StatDataRepository extends JpaRepository<StatData, Long> {

    @Query("SELECT new ru.practicum.model.dto.GetStatDto(s.app, s.uri,  count(s.id)) " +
            "FROM StatData s where s.uri in ?3  " +
            "and  s.timestamp > ?1 and s.timestamp < ?2 " +
            "GROUP BY s.uri")
    List<GetStatDto> getStatisticForUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.model.dto.GetStatDto(s.app, s.uri,  count(distinct s.ip)) " +
            "FROM StatData s where s.uri in ?3  " +
            "and  s.timestamp > ?1 and s.timestamp < ?2 " +
            "GROUP BY s.uri")
    List<GetStatDto> getStatisticForUrisWithUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);
}