package com.springboot.webflux.app.models.services;

import com.springboot.webflux.app.models.documents.Tarea;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TareaService {
	
	public Flux<Tarea> findAll();
	
	public Flux<Tarea> findAllConNombreUpperCase();
	
	public Flux<Tarea> findAllConNombreUpperCaseRepeat();
	
	public Mono<Tarea> findById(String id);
	
	public Mono<Tarea> save(Tarea tarea);
	
	public Mono<Void> delete(Tarea tarea);
	
	public Mono<Tarea> findByDescripcion(String descripcion);
	

}
