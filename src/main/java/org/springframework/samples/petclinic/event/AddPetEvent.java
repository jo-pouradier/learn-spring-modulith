package org.springframework.samples.petclinic.event;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

public record AddPetEvent(Owner owner, Pet pet) {
}
