package ru.practicum.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.dto.GetStatDto;
import ru.practicum.model.StatData;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class StatDataRepositoryTest {

    @Autowired
    private StatDataRepository statDataRepository;

    @BeforeEach
    void setUp() {
        StatData statData1 = new StatData();
        statData1.setApp("ewm-main-service");
        statData1.setUri("/events/1");
        statData1.setIp("192.163.0.1");
        statData1.setTimestamp(LocalDateTime.now().minusDays(1));

        StatData statData2 = new StatData();
        statData2.setApp("ewm-main-service");
        statData2.setUri("/events/1");
        statData2.setIp("192.163.0.1");
        statData2.setTimestamp(LocalDateTime.now().minusHours(1));

        StatData statData3 = new StatData();
        statData3.setApp("ewm-main-service");
        statData3.setUri("/events/2");
        statData3.setIp("192.163.0.1");
        statData3.setTimestamp(LocalDateTime.now().minusDays(1));

        statDataRepository.save(statData1);
        statDataRepository.save(statData2);
        statDataRepository.save(statData3);
    }

    @Test
    void testFindStatistics() {

        List<GetStatDto> getStatDtoList = statDataRepository.getStatisticForUris(LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                List.of("/events/1"));

        assertEquals(1, getStatDtoList.size());
        assertEquals("/events/1", getStatDtoList.get(0).getUri());
        assertEquals(2, getStatDtoList.get(0).getHits());

        getStatDtoList = statDataRepository.getStatisticForUris(LocalDateTime.now().minusDays(4),
                LocalDateTime.now(),
                List.of("/events/1", "/events/2"));


        assertEquals(2, getStatDtoList.size());
        assertEquals("/events/1", getStatDtoList.get(0).getUri());
        assertEquals("/events/2", getStatDtoList.get(1).getUri());
        assertEquals(2, getStatDtoList.get(0).getHits());
        assertEquals(1, getStatDtoList.get(1).getHits());
    }
}