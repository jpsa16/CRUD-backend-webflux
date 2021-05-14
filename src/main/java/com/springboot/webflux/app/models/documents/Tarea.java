package com.springboot.webflux.app.models.documents;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection="tareas")
public class Tarea {
	
	@Id
	@ApiModelProperty(value = "identificador de la tarea", required = false)
	private String id;
	
	@NotEmpty
	@ApiModelProperty(value = "descripcion de la tarea", required = true)
	private String descripcion;

	@NotNull
	@ApiModelProperty(value = "vigencia de la tarea", required = true)
	private Boolean vigente;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "fecha de creacion de la tarea", required = false)
	private Date fechaCreacion;

	public Tarea() {}

	public Tarea(String descripcion, Boolean vigente) {
		this.descripcion = descripcion;
		this.vigente = vigente;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}
}
