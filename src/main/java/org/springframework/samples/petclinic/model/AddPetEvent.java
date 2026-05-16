package org.springframework.samples.petclinic.model;

public record AddPetEvent(Owner owner, Pet pet) {
}
