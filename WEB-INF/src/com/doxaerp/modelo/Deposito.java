package com.doxaerp.modelo;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Depositos")
public class Deposito extends ModeloERP implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4265308763095168120L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long depositoid;
	private String nombre;

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

	public Long getDepositoid() {
		return depositoid;
	}

	public void setDepositoid(Long depositoid) {
		this.depositoid = depositoid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
