package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Sucursales")
public class Sucursal extends ModeloERP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3991360098257704777L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sucursalid;
	
	private String nombre;
	private String establecimiento;
	private String direccion;
	
	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SucursalUsuario> usuarios;

	public Long getSucursalid() {
		return sucursalid;
	}

	public void setSucursalid(Long sucursalid) {
		this.sucursalid = sucursalid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	

	public List<SucursalUsuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SucursalUsuario> usuarios) {
		this.usuarios = usuarios;
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
