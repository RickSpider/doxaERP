package com.doxaerp.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Personas")
public class Persona extends ModeloERP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7296818978299953444L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long personaid;
	
	@ManyToOne
	@JoinColumn(name = "documentotipoid", nullable = false)
	private Tipo documentoTipo;
	
	@Column(nullable = false)
	private String documentoNum;
	
	@Column(nullable = false)
	private String razonSocial;
	
	private String correo;
	
	@Column(columnDefinition="TEXT")
	private String direccion;
	
	public Tipo getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(Tipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Long getPersonaid() {
		return personaid;
	}

	public void setPersonaid(Long personaid) {
		this.personaid = personaid;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getDocumentoNum() {
		return documentoNum;
	}

	public void setDocumentoNum(String documentoNum) {
		this.documentoNum = documentoNum;
	}

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
	
}
