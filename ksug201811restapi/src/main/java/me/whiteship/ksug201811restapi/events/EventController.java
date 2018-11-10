package me.whiteship.ksug201811restapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EventDtoValidator eventDtoValidator;

    @PostMapping("/api/events")
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto,
                                 Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventDtoValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();

        Event savedEvent = eventRepository.save(event);
        URI uri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(linkTo(EventController.class).withRel("events"));
        eventResource.add(linkTo(EventController.class).slash(savedEvent.getId()).withRel("update"));
        eventResource.add(new Link("/docs/index.html#resources-events-create", "profile"));
        return ResponseEntity.created(uri).body(eventResource);
    }

}
