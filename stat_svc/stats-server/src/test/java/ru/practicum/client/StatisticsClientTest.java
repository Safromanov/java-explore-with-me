package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.dto.ClientStatDto;
import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.dto.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class StatisticsClientTest {

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private StatisticsClient statisticsClient;
    private MockRestServiceServer mockServer;

    @Value("${stats-server.url}")
    private String serverUrl;

    @BeforeEach
    public void init(@Autowired RestTemplate restTemplate) {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

//    @Test
//    public void addHitTest() throws JsonProcessingException, URISyntaxException {
//        HitDto hitDto = new HitDto("ewm-main-service",
//                "/events/1",
//                "192.163.0.1",
//                LocalDateTime.now());
//
//        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
//
//        Mockito.when(request.getRequestURI()).thenReturn("/events/1");
//        Mockito.when(request.getRemoteAddr()).thenReturn("192.163.0.1");
//
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(serverUrl + "hit")))
//                .andExpect(method(HttpMethod.POST))
//                .andExpect(content().json(mapper.writeValueAsString(hitDto)))
//                .andRespond(withStatus(HttpStatus.CREATED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                );
//
//        statisticsClient.addHit("ewm-main-service",
//                request);
//    }

    @Test
    public void getStatsTest() throws URISyntaxException, JsonProcessingException {

        List<GetStatDto> getStatDto = List.of(new GetStatDto("ewm-main-service",
                "/events/1",
                101L));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(serverUrl + "stats")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(getStatDto))
                );
        ClientStatDto clientStatDto = ClientStatDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .uris(new String[]{"/events/1"})
                .unique(true)
                .build();
        ResponseEntity<Object> responseFromClient = statisticsClient.getStaticsForUri(clientStatDto);

        assertEquals(HttpStatus.OK, responseFromClient.getStatusCode());
    }
}
