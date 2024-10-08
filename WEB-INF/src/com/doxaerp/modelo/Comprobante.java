package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Comprobantes")
public class Comprobante extends ModeloERP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8220414985277835505L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long comprobanteid;
	
	@ManyToOne
	@JoinColumn(name = "comprobantetipoid", nullable = false)
	private Tipo comprobanteTipo;
	
	private Long timbrado;
	private Date emision;
	private Date vencimiento;
	
	private boolean activo = false;
	
	@ManyToOne
	@JoinColumn(name = "sucursalid", nullable = false)
	private Sucursal sucursal;
	private String puntoExpedicion;
	
	private long inicio;
	private long fin;
	private long siguiente;
	
	public Long getComprobanteid() {
		return comprobanteid;
	}

	public void setComprobanteid(Long comprobanteid) {
		this.comprobanteid = comprobanteid;
	}

	public Long getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Long timbrado) {
		this.timbrado = timbrado;
	}

	public Date getEmision() {
		return emision;
	}

	public void setEmision(Date emision) {
		this.emision = emision;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public long getInicio() {
		return inicio;
	}

	public void setInicio(long inicio) {
		this.inicio = inicio;
	}

	public long getFin() {
		return fin;
	}

	public void setFin(long fin) {
		this.fin = fin;
	}

	

	public long getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(long siguiente) {
		this.siguiente = siguiente;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getPuntoExpedicion() {
		return puntoExpedicion;
	}

	public void setPuntoExpedicion(String puntoExpedicion) {
		this.puntoExpedicion = puntoExpedicion;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
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
