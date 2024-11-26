package com.doxaerp.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventaspagos")
public class VentaPago extends ModeloERP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7425869397310319658L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ventapagoid;
	
	@ManyToOne
	@JoinColumn(name = "ventaid", nullable = false)
	private Venta venta;
	
	@ManyToOne
	@JoinColumn(name = "formapagotipoid", nullable = false)
	private Tipo formaPagoTipo;
	
	private double monto = 0;

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

	public Long getVentapagoid() {
		return ventapagoid;
	}

	public void setVentapagoid(Long ventapagoid) {
		this.ventapagoid = ventapagoid;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Tipo getFormaPagoTipo() {
		return formaPagoTipo;
	}

	public void setFormaPagoTipo(Tipo formaPagoTipo) {
		this.formaPagoTipo = formaPagoTipo;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	
	
}
