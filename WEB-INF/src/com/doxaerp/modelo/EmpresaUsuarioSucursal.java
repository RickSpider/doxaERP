package com.doxaerp.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Modelo;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="EmpresaUsuariosSucursales")
public class EmpresaUsuarioSucursal extends Modelo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2990542099356616414L;

	@EmbeddedId
	private EmpresaUsuarioSucursalPK empresausuariosucursalpk = new EmpresaUsuarioSucursalPK();
	
	private String puntoExpedicion;
	
	private boolean actual = false;

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
	
	public Sucursal getSucursal() {
		
		return this.getEmpresausuariosucursalpk().getSucursal();
		
	}

	public EmpresaUsuarioSucursalPK getEmpresausuariosucursalpk() {
		return empresausuariosucursalpk;
	}

	public void setEmpresausuariosucursalpk(EmpresaUsuarioSucursalPK empresausuariosucursalpk) {
		this.empresausuariosucursalpk = empresausuariosucursalpk;
	}

	public String getPuntoExpedicion() {
		return puntoExpedicion;
	}

	public void setPuntoExpedicion(String puntoExpedicion) {
		this.puntoExpedicion = puntoExpedicion;
	}

	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}

	
	
	
}
