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
@Table(name = "ventasdetalles")
public class VentaDetalle extends ModeloERP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4270179914720067777L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ventadetalleid;
	
	@ManyToOne
	@JoinColumn(name = "ventaid", nullable = false)
	private Venta venta;
	
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name = "depositoid")
	private Deposito deposito;
	
	private double descuento = 0;
	
	private String productoDescripcion;
	private double precio;
	private double cantidad = 1;
	
	@ManyToOne
	@JoinColumn(name = "suscripcionid")
	private Suscripcion suscripcion;	
	
	@ManyToOne
	@JoinColumn(name = "ivatipoid", nullable = false)
	private Tipo ivaTipo;
	
	@ManyToOne
	@JoinColumn(name = "unidadmedidatipoid" , nullable = false)
	private Tipo unidadMedidaTipo;

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
	public Long getVentadetalleid() {
		return ventadetalleid;
	}
	public void setVentadetalleid(Long ventadetalleid) {
		this.ventadetalleid = ventadetalleid;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public String getProductoDescripcion() {
		return productoDescripcion;
	}
	public void setProductoDescripcion(String productoDescripcion) {
		this.productoDescripcion = productoDescripcion;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public Tipo getIvaTipo() {
		return ivaTipo;
	}
	public void setIvaTipo(Tipo ivaTipo) {
		this.ivaTipo = ivaTipo;
	}
	public Tipo getUnidadMedidaTipo() {
		return unidadMedidaTipo;
	}
	public void setUnidadMedidaTipo(Tipo unidadMedidaTipo) {
		this.unidadMedidaTipo = unidadMedidaTipo;
	}
	public Deposito getDeposito() {
		return deposito;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public Suscripcion getSuscripcion() {
		return suscripcion;
	}
	public void setSuscripcion(Suscripcion suscripcion) {
		this.suscripcion = suscripcion;
	}

	
	
	
}
