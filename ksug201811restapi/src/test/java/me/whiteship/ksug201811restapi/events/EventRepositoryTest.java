package me.whiteship.ksug201811restapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    public void crud() {
        Event event = Event.builder()
                .name("test event")
                .build();

        eventRepository.save(event);

        List<Event> all = eventRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

}
