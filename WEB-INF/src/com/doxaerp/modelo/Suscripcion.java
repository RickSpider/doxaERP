package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "suscripciones")
public class Suscripcion extends ModeloERP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3203533929966121035L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long suscripcionid;
	
	@ManyToOne
	@JoinColumn(name = "personaid", nullable = false)
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name = "servicioid", nullable = false)
	private Producto servicio;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date inicio;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date vencimiento;
	
	@ManyToOne
	@JoinColumn(name = "sucripciontipoid", nullable = false)
	private Tipo suscripcionTipo;
	
	private boolean activo = true;
	
	
	
	public Long getSuscripcionid() {
		return suscripcionid;
	}

	public void setSuscripcionid(Long suscripcionid) {
		this.suscripcionid = suscripcionid;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Producto getServicio() {
		return servicio;
	}

	public void setServicio(Producto servicio) {
		this.servicio = servicio;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
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

	public Tipo getSuscripcionTipo() {
		return suscripcionTipo;
	}

	public void setSuscripcionTipo(Tipo suscripcionTipo) {
		this.suscripcionTipo = suscripcionTipo;
	}
	
	
	
	
	
}
