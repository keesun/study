package me.whiteship.ksug201811restapi.index;

import me.whiteship.ksug201811restapi.events.EventController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport root() {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }

}
