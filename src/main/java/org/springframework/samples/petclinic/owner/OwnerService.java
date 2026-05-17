package org.springframework.samples.petclinic.owner;

import org.springframework.modulith.NamedInterface;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.samples.petclinic.event.AddPetEvent;
import org.springframework.samples.petclinic.event.AddVisitEvent;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Service
@NamedInterface("OwnerService")
public class OwnerService {

	private final OwnerRepository ownerRepository;

	OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public void addPet(Owner owner, Pet pet) {
		owner.addPet(pet);
		ownerRepository.save(owner);
	}

	@ApplicationModuleListener
	public void addPet(AddPetEvent event) {
		event.owner().addPet(event.pet());
		ownerRepository.save(event.owner());
	}

	@ApplicationModuleListener
	public void addVisit(AddVisitEvent event) {
		event.owner().addVisit(event.petId(), event.visit());
		ownerRepository.save(event.owner());
	}

	public Optional<Owner> findById(Integer ownerId) {
		return ownerRepository.findById(ownerId);
	}

}
