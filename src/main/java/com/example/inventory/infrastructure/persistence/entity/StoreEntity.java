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
@Table(name = "stores")
public class StoreEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(nullable = false, unique = true)
		private String name;
		
		@Column(nullable = true)
		private String address;
		
		@Column(nullable = false)
		private boolean status;
		
		/**
	     * Relación uno-a-muchos: una tienda puede tener múltiples inventarios.
	     * - mappedBy = "store" hace referencia al atributo "store" en InventoryEntity.
	     * - cascade = ALL asegura que las operaciones sobre Store se propaguen a Inventories.
	     * - orphanRemoval = true elimina inventarios huérfanos si se quitan de la lista.
	     */
	    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<InventoryEntity> inventories = new ArrayList<>();

		// Getters and Setters
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}
		
		public List<InventoryEntity> getInventories() {
	        return inventories;
	    }

	    public void setInventories(List<InventoryEntity> inventories) {
	        this.inventories = inventories;
	    }
	    
	    // Métodos auxiliares para mantener consistencia bidireccional
	    public void addInventory(InventoryEntity inventory) {
	        inventories.add(inventory);
	        inventory.setStore(this);
	    }

	    public void removeInventory(InventoryEntity inventory) {
	        inventories.remove(inventory);
	        inventory.setStore(null);
	    }

}
