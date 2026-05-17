package org.springframework.samples.petclinic.event;

import jakarta.validation.Valid;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Visit;

public record AddVisitEvent(Owner owner, Visit visit, Integer petId, int price) {
}
