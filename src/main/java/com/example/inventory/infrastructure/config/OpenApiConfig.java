package com.example.inventory.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/** 
 * Configuración de OpenAPI para la documentación de la API REST.
 */
@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Inventory API")
						.version("1.0")
						.description("API para la gestión de inventario de productos")
						.contact(new Contact()
								.name("Soporte")
								.email("nicolas.chaves93@hotmail.com")));
	}

}
