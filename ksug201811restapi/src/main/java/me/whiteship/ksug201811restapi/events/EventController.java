package me.whiteship.ksug201811restapi.events;

import me.whiteship.ksug201811restapi.accounts.Account;
import me.whiteship.ksug201811restapi.accounts.AccountAdapter;
import me.whiteship.ksug201811restapi.common.ErrorResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EventDtoValidator eventDtoValidator;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid EventDto eventDto,
                                 Errors errors,
                                 @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        eventDtoValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setOwner(account);

        Event savedEvent = eventRepository.save(event);
        URI uri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(linkTo(EventController.class).withRel("events"));
        eventResource.add(linkTo(EventController.class).slash(savedEvent.getId()).withRel("update"));
        eventResource.add(new Link("/docs/index.html#resources-events-create", "profile"));
        return ResponseEntity.created(uri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity getEvents(Pageable pageable,
                                    PagedResourcesAssembler<Event> assembler,
                                    @CurrentUser Account account) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedResources<EventResource> pagedResources = assembler.toResource(page, e -> new EventResource(e));
        if (account != null) {
            pagedResources.add(linkTo(EventController.class).withRel("create"));
        }
        return ResponseEntity.ok(pagedResources);
    }


    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account account) {
        Optional<Event> byId = this.eventRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = byId.get();
        EventResource eventResource = new EventResource(event);

        if (event.getOwner().equals(account)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update"));
        }
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        eventDtoValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        Optional<Event> byId = this.eventRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event existingEvent = byId.get();
        modelMapper.map(eventDto, existingEvent);
        Event updatedEvent = this.eventRepository.save(existingEvent);
        return ResponseEntity.ok(new EventResource(updatedEvent));
    }


}
