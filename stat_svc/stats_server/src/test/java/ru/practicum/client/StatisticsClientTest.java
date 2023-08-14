package ru.practicum.client;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

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

    @Value("${stats-server.url}")
    private String serverUrl;

    @BeforeEach
    public void init(@Autowired RestTemplate restTemplate) {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void addHitTest() throws URISyntaxException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getRequestURI()).thenReturn("/events/1");
        Mockito.when(request.getRemoteAddr()).thenReturn("192.163.0.1");

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(serverUrl + "hit")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        statisticsClient.addHit("ewm-main-service", request.getRequestURI(),
                request);
    }

//    @Test
//    public void getStatsTest() throws URISyntaxException, JsonProcessingException {
//
//        List<GetStatDto> getStatDto = List.of(new GetStatDto("ewm-main-service",
//                "/events/1",
//                101L));
//
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(serverUrl + "stats")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(getStatDto))
//                );
//        Map<String, Object> clientStatDto =Map.of(
//                "start", LocalDateTime.now().minusDays(1),
//                "end", LocalDateTime.now(),
//                "uris", List.of("/events/1"),
//                "unique", "true");
//        List<GetStatDto> staticsForUri = statisticsClient.getStaticsForUri(clientStatDto);
//        System.out.println(staticsForUri.get(0));
//        assertEquals(1, staticsForUri.size());
//    }
}
