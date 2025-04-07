package com.ecommerce.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * La clase CatalogApplication sirve como punto de entrada para la aplicación.
 * Está anotada con @SpringBootApplication para indicar que es una aplicación Spring Boot.
 * La anotación @EnableMongoAuditing habilita las capacidades de auditoría para entidades MongoDB.
 * La anotación @EnableMongoRepositories permite la creación de beans de repositorio MongoDB.
 * La aplicación se lanza con el método main, que inicializa la SpringApplication.
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CatalogApplication {
	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}
}
