package me.whiteship.ksug201811restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventDtoValidator {

    public void validate(EventDto eventDto, Errors errors) {
        int maxPrice = eventDto.getMaxPrice();
        if (maxPrice < eventDto.getBasePrice()) {
            errors.rejectValue("maxPrice", "wrong.value", "Max price가 base 보다 낮으면 안되요.");
        }

        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if (closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("closeEnrollmentDateTime", "wrong.value", "closeEnrollmentDateTime is wrong");
        }

    }

}
