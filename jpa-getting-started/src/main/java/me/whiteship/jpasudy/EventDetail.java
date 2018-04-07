package me.whiteship.jpasudy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
public class EventDetail {

    @Id
    @GeneratedValue
    private Long id;

    @Lob @Basic(fetch = FetchType.LAZY)
    private String content;

}
