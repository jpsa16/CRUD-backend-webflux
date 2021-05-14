package com.springboot.webflux.app;

import java.util.Date;

import com.springboot.webflux.app.models.documents.Tarea;
import com.springboot.webflux.app.models.services.TareaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApirestApplication implements CommandLineRunner {

    @Autowired
    private TareaService service;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApirestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApirestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("tareas").subscribe();

        (
                Flux.just(
                        new Tarea("TareaTestUnitario", true)
                )
                        .flatMap(tarea -> {
                            tarea.setFechaCreacion(new Date());
                            return service.save(tarea);
                        })
        )
                .subscribe(tarea -> log.info("Insert: " + tarea.getId() + " " + tarea.getDescripcion()));

    }

}
