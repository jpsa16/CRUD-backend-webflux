package com.springboot.webflux.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.springboot.webflux.app.models.documents.Tarea;
import com.springboot.webflux.app.models.services.TareaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService service;

    @Value("${config.uploads.path}")
    private String path;

    @GetMapping
    @ApiOperation(value = "Permite obtener una lista de tareas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resulado exitoso"),
            @ApiResponse(code = 404, message = "No encontrado") })
    public Mono<ResponseEntity<Flux<Tarea>>> lista() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.findAll())
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Permite obtener una tarea mediante un identificador", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resulado exitoso"),
            @ApiResponse(code = 404, message = "No encontrado") })
    public Mono<ResponseEntity<Tarea>> ver(@PathVariable String id) {
        return service.findById(id).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Permite crear una tareas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resulado exitoso"),
            @ApiResponse(code = 400, message = "Parámetros inválidos en la petición"),
            @ApiResponse(code = 404, message = "No encontrado") })
    public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Tarea> tarea) {

        Map<String, Object> respuesta = new HashMap<String, Object>();

        return tarea.flatMap(tareaMap -> {
            if (tareaMap.getFechaCreacion() == null) {
                tareaMap.setFechaCreacion(new Date());
            }

            return service.save(tareaMap).map(p -> {
                respuesta.put("tarea", p);
                respuesta.put("mensaje", "Producto creado con éxito");
                respuesta.put("timestamp", new Date());
                return ResponseEntity
                        .created(URI.create("/api/productos/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(respuesta);
            });

        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        respuesta.put("errors", list);
                        respuesta.put("timestamp", new Date());
                        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });
        });
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Permite editar una tareas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resulado exitoso"),
            @ApiResponse(code = 400, message = "Parámetros inválidos en la petición"),
            @ApiResponse(code = 404, message = "No encontrado") })
    public Mono<ResponseEntity<Map<String, Object>>> editar(@Valid @RequestBody Mono<Tarea> tarea, @PathVariable String id) {
        Map<String, Object> respuesta = new HashMap<String, Object>();
        return tarea.flatMap(tareaMono ->
                service.findById(id).flatMap(p -> {
                    p.setDescripcion(tareaMono.getDescripcion());
                    p.setVigente(tareaMono.getVigente());
                    return service.save(p);
                }).map(p -> {
                    respuesta.put("tarea", p);
                    respuesta.put("mensaje", "Producto actualizado con éxito");
                    respuesta.put("timestamp", new Date());
                    return ResponseEntity
                            .created(URI.create("/api/productos/".concat(p.getId())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(respuesta);
                })
        ).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        respuesta.put("errors", list);
                        respuesta.put("timestamp", new Date());
                        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });

        });
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Permite eliminar una tareas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resulado exitoso"),
            @ApiResponse(code = 404, message = "No encontrado") })
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id) {
        return service.findById(id).flatMap(p -> {
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

}
