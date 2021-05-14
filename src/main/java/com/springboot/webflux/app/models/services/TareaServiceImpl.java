package com.springboot.webflux.app.models.services;

import com.springboot.webflux.app.models.documents.Tarea;
import com.springboot.webflux.app.models.dao.TareaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TareaServiceImpl implements TareaService {

	@Autowired
	private TareaDao dao;
	
	@Override
	public Flux<Tarea> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Tarea> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Tarea> save(Tarea tarea) {
		return dao.save(tarea);
	}

	@Override
	public Mono<Void> delete(Tarea tarea) {
		return dao.delete(tarea);
	}

	@Override
	public Flux<Tarea> findAllConNombreUpperCase() {
		return dao.findAll().map(producto -> {
			producto.setDescripcion(producto.getDescripcion().toUpperCase());
			return producto;
		});
	}

	@Override
	public Flux<Tarea> findAllConNombreUpperCaseRepeat() {
		return findAllConNombreUpperCase().repeat(5000);
	}


	@Override
	public Mono<Tarea> findByDescripcion(String descripcion) {
		return dao.obtenerPorDescripcion(descripcion);
	}



}
