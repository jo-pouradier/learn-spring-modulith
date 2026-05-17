package org.springframework.samples.petclinic.billing;

import org.springframework.data.repository.CrudRepository;

interface BillingRepository extends CrudRepository<Bill, Integer> {
}
