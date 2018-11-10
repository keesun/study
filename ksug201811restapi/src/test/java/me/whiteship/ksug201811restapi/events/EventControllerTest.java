package me.whiteship.ksug201811restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.whiteship.ksug201811restapi.common.TestDocConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestDocConfig.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        // Given
        Event event = Event.builder()
                .name("만나서 반갑습니다.")
                .description("부모님도 못뵈고 왔어요.")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 2, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 8, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 4, 8, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 5, 8, 0))
                .basePrice(0)
                .maxPrice(0)
                .location("네이버 D2 팩토리")
                .limitOfEnrollment(100)
                .eventStatus(EventStatus.ENROLLMENT_STARTED)
                .build();

        // When & then
        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("free").value(true))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links").hasJsonPath())
                .andExpect(jsonPath("_links.self").hasJsonPath())
                .andExpect(jsonPath("_links.events").hasJsonPath())
                .andExpect(jsonPath("_links.update").hasJsonPath())
                .andExpect(jsonPath("_links.profile").hasJsonPath())
                .andDo(document("create-event",
                    links(
                        linkWithRel("self").description("link to self"),
                        linkWithRel("events").description("link to events"),
                        linkWithRel("update").description("link to update"),
                        linkWithRel("profile").description("link to profile")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("name").description("name of the event"),
                        fieldWithPath("description").description("description of the event")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("id").description("identifier of the event"),
                        fieldWithPath("name").description("name of the event")
                    )
                ))
        ;
    }

    @Test
    public void createEvent404() throws Exception {
        EventDto eventDto = new EventDto();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent404_2() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("만나서 반갑습니다.")
                .description("부모님도 못뵈고 왔어요.")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 5, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 4, 8, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 3, 8, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 2, 8, 0))
                .basePrice(1000)
                .maxPrice(500)
                .location("네이버 D2 팩토리")
                .limitOfEnrollment(100)
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").hasJsonPath())
                .andExpect(jsonPath("$[0].rejectedValue").hasJsonPath())
                .andExpect(jsonPath("$[0].defaultMessage").hasJsonPath())
                .andExpect(jsonPath("$[0].objectName").hasJsonPath())
        ;
    }

    @Test
    public void getEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::saveEvent);

        // When & Then
        this.mockMvc.perform(get("/api/events")
                    .param("size", "10")
                    .param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links").hasJsonPath())
        ;
    }

    private Event saveEvent(int index) {
        Event event = Event.builder()
                .name("test event" + index)
                .build();
        return this.eventRepository.save(event);
    }

    @Test
    public void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/1983"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getEvent() throws Exception {
        Event event = this.saveEvent(100);

        this.mockMvc.perform(get("/api/events/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").hasJsonPath())
        ;
    }


}