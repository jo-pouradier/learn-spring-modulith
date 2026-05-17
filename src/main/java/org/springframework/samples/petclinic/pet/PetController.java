/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.pet;

import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.samples.petclinic.event.AddPetEvent;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Wick Dynex
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final PetTypeRepository types;
	private final ApplicationEventPublisher eventPublisher;
	private final PetRepo petRepo;
	private final ApplicationEventPublisher applicationEventPublisher;

	PetController(PetTypeRepository types, ApplicationEventPublisher eventPublisher, PetRepo petRepo, ApplicationEventPublisher applicationEventPublisher) {
		this.types = types;
		this.eventPublisher = eventPublisher;
		this.petRepo = petRepo;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@ModelAttribute("types")
	Collection<PetType> populatePetTypes() {
		return this.types.findPetTypes();
	}


	@ModelAttribute("pet")
	Pet findPet(@PathVariable int ownerId, @PathVariable(required = false) Integer petId) {
		if (petId == null) {
			return new Pet();
		}

		return petRepo.findById(petId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));
	}

	@InitBinder("owner")
	void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@InitBinder("pet")
	void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@GetMapping("/pets/new")
	String initCreationForm(Owner owner, ModelMap model) {
		Pet pet = new Pet();
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/new")
	String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result,
							   RedirectAttributes redirectAttributes) {

		if (StringUtils.hasText(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		applicationEventPublisher.publishEvent(new AddPetEvent(owner, pet));

		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}


	@GetMapping("/pets/{petId}/edit")
	String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result,
							 RedirectAttributes redirectAttributes) {

		// checking if the pet name already exists for the owner
		var existingPet = owner.getPet(pet.getName(), false);
		if (existingPet != null && !Objects.equals(existingPet.getId(), pet.getId())) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		var currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		updatePetDetails(owner, pet); // TODO event

		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}

	/**
	 * Updates the pet details if it exists or adds a new pet to the owner.
	 *
	 * @param owner The owner of the pet
	 * @param pet   The pet with updated details
	 */
	private void updatePetDetails(Owner owner, Pet pet) {
		Integer id = pet.getId();
		Assert.state(id != null, "'pet.getId()' must not be null");
		Pet existingPet = owner.getPet(id);
		if (existingPet != null) {
			// Update existing pet's properties
			existingPet.setName(pet.getName());
			existingPet.setBirthDate(pet.getBirthDate());
			existingPet.setType(pet.getType());
			applicationEventPublisher.publishEvent(new AddPetEvent(owner, existingPet));
		} else {
			applicationEventPublisher.publishEvent(new AddPetEvent(owner, pet));
		}
	}

}
