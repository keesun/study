package me.whiteship.ksug201811restapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.whiteship.ksug201811restapi.accounts.Account;
import me.whiteship.ksug201811restapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account owner;

    public void update() {
        if (basePrice == 0 && maxPrice == 0) {
            this.free = true;
        }
    }
}
