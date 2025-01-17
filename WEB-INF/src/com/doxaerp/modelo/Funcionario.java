package com.doxaerp.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionario extends ModeloERP implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6195824852079768299L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long funcionarioid;
	
	private String nombre;
	private String apellido;
	
	@ManyToOne
	@JoinColumn(name = "documentotipoid", nullable = false)
	private Tipo documentoTipo;
	private String documentoNum;
	
	@ManyToOne
	@JoinColumn(name = "funcionarioTipoId", nullable = false)
	private Tipo funcionarioTipo;
	
	@ManyToOne
	@JoinColumn(name = "usuarioid")
	private Usuario usuario;

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

	public Long getFuncionarioid() {
		return funcionarioid;
	}

	public void setFuncionarioid(long funcionarioid) {
		this.funcionarioid = funcionarioid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Tipo getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(Tipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public String getDocumentoNum() {
		return documentoNum;
	}

	public void setDocumentoNum(String documentoNum) {
		this.documentoNum = documentoNum;
	}

	public Tipo getFuncionarioTipo() {
		return funcionarioTipo;
	}

	public void setFuncionarioTipo(Tipo funcionarioTipo) {
		this.funcionarioTipo = funcionarioTipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
