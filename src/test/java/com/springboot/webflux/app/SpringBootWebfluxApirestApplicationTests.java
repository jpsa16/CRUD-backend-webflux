package com.springboot.webflux.app;

import java.util.Collections;
import java.util.List;

import com.springboot.webflux.app.models.documents.Tarea;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.webflux.app.models.services.TareaService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private TareaService service;
	
	@Value("${config.base.endpoint}")
	private String url;

	@Test
	public void listarTest() {
		
		client.get()
		.uri(url)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Tarea.class)
		.consumeWith(response -> {
			List<Tarea> tareas = response.getResponseBody();
			tareas.forEach(p -> {
				System.out.println(p.getDescripcion());
			});
			
			Assertions.assertThat(tareas.size()>0).isTrue();
		});
		//.hasSize(9);
	}
	
	@Test
	public void crearTest() {
		Tarea tarea = new Tarea("tarea1", true);
		client.post().uri(url)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(tarea), Tarea.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.tarea.id").isNotEmpty()
		.jsonPath("$.tarea.descripcion").isEqualTo("tarea1");
	}


	@Test
	public void editarTest() {
		
		Tarea tarea = service.findByDescripcion("TareaTestUnitario").block();

		Tarea tareaEditado = new Tarea("TareaTestUnitario", false);
		
		client.put().uri(url + "/{id}", Collections.singletonMap("id", tarea.getId()))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(tareaEditado), Tarea.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.tarea.id").isNotEmpty()
		.jsonPath("$.tarea.vigente").isEqualTo(false);
	}
	
	@Test
	public void eliminarTest() {
		Tarea tarea = service.findByDescripcion("TareaTestUnitario").block();
		client.delete()
		.uri(url + "/{id}", Collections.singletonMap("id", tarea.getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody()
		.isEmpty();
		
		client.get()
		.uri(url + "/{id}", Collections.singletonMap("id", tarea.getId()))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody()
		.isEmpty();
	}
}
