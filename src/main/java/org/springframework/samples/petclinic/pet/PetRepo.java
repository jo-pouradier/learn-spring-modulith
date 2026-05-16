package org.springframework.samples.petclinic.pet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Pet;

interface PetRepo extends CrudRepository<Pet, Integer> {
}
