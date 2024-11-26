package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Agendamientos")
public class Agendamiento extends ModeloERP implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1558949509519075985L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long agendamientoid;
	
	private String titulo;
	private String contenido;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fin = new Date();

	@Override
	public Object[] getArrayObjectDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getAgendamientoid() {
		return agendamientoid;
	}

	public void setAgendamientoid(Long agendamientoid) {
		this.agendamientoid = agendamientoid;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}
	
	

}
