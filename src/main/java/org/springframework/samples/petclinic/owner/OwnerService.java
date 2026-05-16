package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;

	OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public void addPet(Owner owner, Pet pet) {
		owner.addPet(pet);
		ownerRepository.save(owner);
	}

	public Optional<Owner> findById(Integer ownerId) {
		return ownerRepository.findById(ownerId);
	}

	public void save(Owner owner) {
		ownerRepository.save(owner);
	}
}
