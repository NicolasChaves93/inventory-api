package com.example.inventory.interface_.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para gestionar la salud del servicio.
 */
@RestController
public class HealthController {
	@GetMapping("/health")
	public ResponseEntity<?> health(){
		return ResponseEntity.ok(Map.of("status", "OK"));
	}

}
