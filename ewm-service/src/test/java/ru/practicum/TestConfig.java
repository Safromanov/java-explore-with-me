package ru.practicum;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@Configuration
@Transactional
public class TestConfig {

    @Bean
    public TestEntityManager testEntityManager(EntityManagerFactory entityManagerFactory) {
        var entityManager = new TestEntityManager(entityManagerFactory);
        initTwoUsers(entityManager);
        initThreeCategory(entityManager);
        initTwoEvent(entityManager);
        return entityManager;
    }

    private void initTwoUsers(TestEntityManager entityManager) {
        User user1 = new User(1L, "testUser", "test@user.net");
        entityManager.merge(user1);
        User user2 = new User(2L, "test2User", "test2@user.net");
        entityManager.merge(user2);
        entityManager.flush();
    }

    private void initThreeCategory(TestEntityManager entityManager) {
        Category category1 = new Category(1L, "testCategory");
        entityManager.merge(category1);
        Category category2 = new Category(2L, "test2Category2");
        entityManager.merge(category2);
        Category category3 = new Category(3L, "test2Category3");
        entityManager.merge(category3);
        entityManager.flush();
    }

    private void initTwoEvent(TestEntityManager entityManager) {
        User initiator1 = entityManager.find(User.class, 1L);
        User requester = entityManager.find(User.class, 2L);
        Category category1 = entityManager.find(Category.class, 1L);
        Category category2 = entityManager.find(Category.class, 2L);
        String annotation = "testAnnotationForBestTestInWorld";
        String description = "testDescriptionForBestTestInWorld";
        String title = "testTitleForBestTestInWorld";
        LocalDateTime now = LocalDateTime.now();
        Location loca = new Location();
        loca.setLat(1F);
        loca.setLat(0F);


        Event event1 = new Event(1L, initiator1, annotation, category1, description + "FIND_ME", now.minusDays(2), now.plusMonths(1),
                loca, true, 1, true, title, State.PUBLISHED);
        Event event2 = new Event(2L, initiator1, annotation, category2, description, now.minusDays(2), now.plusMonths(1),
                loca, true, 1, true, title, State.PENDING);

        event1 = entityManager.merge(event1);
        event2 = entityManager.merge(event2);
        EventRequest eventRequest = new EventRequest(1L, requester, event1, now, Status.PENDING);
        EventRequest eventRequest2 = new EventRequest(2L, requester, event1, now, Status.PENDING);
        entityManager.merge(eventRequest);
        entityManager.merge(eventRequest2);
        entityManager.flush();
    }
}
