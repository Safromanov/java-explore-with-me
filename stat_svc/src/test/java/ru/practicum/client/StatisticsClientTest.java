package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.model.dto.GetDto;
import ru.practicum.model.dto.HitDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class StatisticsClientTest {
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private StatisticsClient statisticsClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(statisticsClient.getRest());
    }

    @Test
    public void addHitTest() throws JsonProcessingException, URISyntaxException {
        HitDto hitDto = new HitDto("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.now());

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:8080/hit")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(hitDto)))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        statisticsClient.addHit("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                hitDto.getTimestamp());
    }

    @Test
    public void getStatsTest() throws URISyntaxException, JsonProcessingException {

        List<GetDto> getDto = List.of(new GetDto("ewm-main-service",
                "/events/1",
                101L));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://localhost:8080/stats")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(getDto))
                );

        ResponseEntity<Object> responseFromClient = statisticsClient.getStaticsForUri(LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                new String[]{"/events/1"},
                true);

        assertEquals(HttpStatus.OK, responseFromClient.getStatusCode());
    }
}
