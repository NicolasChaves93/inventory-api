package com.example.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "inventories",
	    uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "product_id"})
	)
public class InventoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		    name = "store_id", 
		    nullable = false,
		    foreignKey = @ForeignKey(name = "fk_inventory_store")
	)
	private StoreEntity store;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		    name = "product_id", 
		    nullable = false,
		    foreignKey = @ForeignKey(name = "fk_inventory_product")
		)
	private ProductEntity product;
	
	@Column(nullable = false)
	private Integer quantity;
	
	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StoreEntity getStore() {
		return store;
	}

	public void setStore(StoreEntity store) {
		this.store = store;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
