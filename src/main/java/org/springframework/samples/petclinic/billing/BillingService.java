package org.springframework.samples.petclinic.billing;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.samples.petclinic.event.AddVisitEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Service
class BillingService {

	private final BillingRepository billingRepository;

	BillingService(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}

	@ApplicationModuleListener
	void MoreMoney(AddVisitEvent event) {
		var bill = new Bill()
			.setOwner(event.owner())
			.setPrice(event.price());
		billingRepository.save(bill);
	}
}
