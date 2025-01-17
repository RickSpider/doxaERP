package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.doxacore.modelo.Usuario;

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
@Table(name = "Cajas")
public class Caja extends ModeloERP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6411978827538862270L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cajaid;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date fechaApertura;
	private double montoApertura = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCierre;
	private Double montoCierre;
	
	@ManyToOne
	@JoinColumn(name = "usuariocajaid",  nullable = false, updatable = false)
	private Usuario usuarioCaja;
	
	@ManyToOne
	@JoinColumn(name = "usuariocierreid")
	private Usuario usuarioCierre;
	
	@ManyToOne
	@JoinColumn(name = "sucursalid", nullable = false, updatable = false)
	private Sucursal sucursal;
	

	public Long getCajaid() {
		return cajaid;
	}

	public void setCajaid(Long cajaid) {
		this.cajaid = cajaid;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public double getMontoApertura() {
		return montoApertura;
	}

	public void setMontoApertura(double montoApertura) {
		this.montoApertura = montoApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Double getMontoCierre() {
		return montoCierre;
	}

	public void setMontoCierre(Double montoCierre) {
		this.montoCierre = montoCierre;
	}

	public Usuario getUsuarioCaja() {
		return usuarioCaja;
	}

	public void setUsuarioCaja(Usuario usuarioCaja) {
		this.usuarioCaja = usuarioCaja;
	}

	public Usuario getUsuarioCierre() {
		return usuarioCierre;
	}

	public void setUsuarioCierre(Usuario usuarioCierre) {
		this.usuarioCierre = usuarioCierre;
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
