package org.springframework.samples.petclinic.model;

public record AddPetEvent(Pet pet, Owner owner) {
}
