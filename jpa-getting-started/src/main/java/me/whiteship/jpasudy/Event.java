package me.whiteship.jpasudy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EventDetail eventDetail;

}
