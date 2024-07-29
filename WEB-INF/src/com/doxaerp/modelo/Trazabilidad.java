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
@Table(name = "Trazabilidades")
public class Trazabilidad extends ModeloERP implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6461398043666911431L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long trazabilidadid;
	
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name = "depositoid", nullable = false)
	private Deposito deposito;
	
	private double cantidad;
	
	private String Comentario;
	
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

	public Long getTrazabilidadid() {
		return trazabilidadid;
	}

	public void setTrazabilidadid(Long trazabilidadid) {
		this.trazabilidadid = trazabilidadid;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getComentario() {
		return Comentario;
	}

	public void setComentario(String comentario) {
		Comentario = comentario;
	}

	
}
