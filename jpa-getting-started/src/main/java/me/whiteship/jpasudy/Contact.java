package me.whiteship.jpasudy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDateTime;

@Entity(name = "Contact")
@Getter @Setter
public class Contact {

    @Id @GeneratedValue
    private Integer id;

    private Name name;

    @Basic(optional = false)
    private String notes;

    private URL website;

    private boolean starred;

    private String homeAddress;

    private LocalDateTime created;

}