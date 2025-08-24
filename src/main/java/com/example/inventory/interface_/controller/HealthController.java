package com.example.inventory.interface_.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para gestionar la salud del servicio.
 */
@RestController
@Tag(name = "Salud del Servicio", description = "Verifica el estado de salud del servicio")
public class HealthController {
	
	@Operation(summary = "Verificar salud del servicio", description = "Devuelve el estado de salud del servicio.")
	@GetMapping("/health")
	public ResponseEntity<?> health(){
		return ResponseEntity.ok(Map.of("status", "OK"));
	}

}
