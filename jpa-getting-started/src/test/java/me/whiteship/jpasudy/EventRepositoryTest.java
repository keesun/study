package me.whiteship.jpasudy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void addEvent() {
        Event event1 = new Event();
        event1.setTitle("new title1");

        EventDetail eventDetail1 = new EventDetail();
        eventDetail1.setContent("new content1");
        event1.setEventDetail(eventDetail1);

        Event event2 = new Event();
        event2.setTitle("new title");

        EventDetail eventDetail2 = new EventDetail();
        eventDetail2.setContent("new content");
        event2.setEventDetail(eventDetail2);

        // 2 Inserts
        Event savedEvent1 = eventRepository.saveAndFlush(event1);
        assertThat(savedEvent1.getId()).isNotNull();

        Event savedEvent2 = eventRepository.saveAndFlush(event2);
        assertThat(savedEvent2.getId()).isNotNull();

        System.out.println("find by id");
        entityManager.clear();

        Event eventOnly = eventRepository.findById(savedEvent1.getId()).orElseThrow(() -> new RuntimeException());// Select

        System.out.println("get detail content");
        assertThat(eventOnly.getEventDetail().getContent()).isEqualTo("new content1"); // Select detail

        System.out.println("find all");
        entityManager.clear();
        eventRepository.findAll(); // Select
    }

}