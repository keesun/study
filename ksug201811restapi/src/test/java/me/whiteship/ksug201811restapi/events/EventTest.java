package me.whiteship.ksug201811restapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder() {
        String name = "test event";
        Event event = Event.builder()
                .name(name)
                .description("ksug")
                .build();

        assertThat(event.getName()).isEqualTo(name);
    }

    @Test
    public void javaBean() {
        String name = "keesun";
        Event event = new Event();
        event.setName(name);
        assertThat(event.getName()).isEqualTo(name);
    }

}