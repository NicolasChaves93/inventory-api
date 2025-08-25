package com.example.inventory.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String code;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = true)
	private String description;
	
	@Column(nullable = false)
	private float price;
	
	/**
    * Relación uno-a-muchos: un producto puede estar en múltiples inventarios.
    * mappedBy = "product" hace referencia al campo en InventoryEntity.
    */
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryEntity> inventories = new ArrayList<>();
	
	
	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public List<InventoryEntity> getInventories() {
        return inventories;
    }

    public void setInventories(List<InventoryEntity> inventories) {
        this.inventories = inventories;
    }
    
    // ----- Métodos auxiliares -----
    public void addInventory(InventoryEntity inventory) {
        inventories.add(inventory);
        inventory.setProduct(this);
    }

    public void removeInventory(InventoryEntity inventory) {
        inventories.remove(inventory);
        inventory.setProduct(null);
    }
}
