package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.Modulithic;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@Modulithic
class PetClinicApplicationTest {
	@Test
	void writeDocumentationSnippets() {

		var modules = ApplicationModules.of(PetClinicApplicationTest.class).verify();

		new Documenter(modules)
			.writeModulesAsPlantUml()
			.writeIndividualModulesAsPlantUml();
	}
}
