package me.whiteship.ksug201811restapi.common;

import me.whiteship.ksug201811restapi.index.IndexController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ErrorResource extends Resource<Errors> {
    public ErrorResource(Errors content, Link... links) {
        super(content, links);
        add(linkTo(IndexController.class).withRel("index"));
    }
}
