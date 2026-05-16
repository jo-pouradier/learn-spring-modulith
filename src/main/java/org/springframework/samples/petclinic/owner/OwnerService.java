package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.AddPetEvent;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

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

	@TransactionalEventListener
	public void addPet(AddPetEvent event) {
		event.owner().addPet(event.pet());
		ownerRepository.save(event.owner());
	}

	public Optional<Owner> findById(Integer ownerId) {
		return ownerRepository.findById(ownerId);
	}

	public void save(Owner owner) {
		ownerRepository.save(owner);
	}
}
