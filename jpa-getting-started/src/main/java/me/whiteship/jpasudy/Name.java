package me.whiteship.jpasudy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Name {

    private String first;

    private String middle;

    private String last;

}