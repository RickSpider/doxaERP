package com.doxaerp.modelo;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class EmpresaUsuarioSucursalPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2895587488109271711L;

	@ManyToOne
	@JoinColumn(name="empresausuarioid")
	private EmpresaUsuario empresaUsuario;
	
	@ManyToOne
	@JoinColumn(name="sucursalid")
	private Sucursal sucursal;

	public EmpresaUsuario getEmpresaUsuario() {
		return empresaUsuario;
	}

	public void setEmpresaUsuario(EmpresaUsuario empresaUsuario) {
		this.empresaUsuario = empresaUsuario;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	

}
