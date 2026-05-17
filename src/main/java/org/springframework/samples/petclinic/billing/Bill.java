package org.springframework.samples.petclinic.billing;

import jakarta.persistence.*;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Owner;

@Entity
class Bill {

	@Id
	@GeneratedValue
	private Integer id;

	@OneToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	private int price;

	public Bill() {
	}

	public Owner getOwner() {
		return owner;
	}

	public Bill setOwner(Owner owner) {
		this.owner = owner;
		return this;
	}

	public int getPrice() {
		return price;
	}

	public Bill setPrice(int price) {
		this.price = price;
		return this;
	}

	@Override
	public String toString() {
		return "Bill{" +
			   "id=" + getId() +
			   ", owner=" + owner.getId() +
			   ", price=" + price +
			   '}';
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
