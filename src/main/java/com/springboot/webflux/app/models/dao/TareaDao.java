package com.springboot.webflux.app.models.dao;

import com.springboot.webflux.app.models.documents.Tarea;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface TareaDao extends ReactiveMongoRepository<Tarea, String>{

	public Mono<Tarea> findByDescripcion(String descripcion);
	
	@Query("{ 'descripcion': ?0 }")
	public Mono<Tarea> obtenerPorDescripcion(String descripcion);
}
