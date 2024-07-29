package com.doxaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;

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
@Table(name = "Ventas")
public class Venta extends ModeloERP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6611501212907701237L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ventaid;
	
	@ManyToOne
	@JoinColumn(name = "cajaid", nullable = false)
	private Caja caja;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "personaid", nullable = false)
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name = "comprobantetipoid", nullable = false)
	private Tipo comprobanteTipo;
	
	@ManyToOne
	@JoinColumn(name = "condicionventatipoid", nullable = false)
	private Tipo condicionVentaTipo;	
	
	private String comprobanteNum;
	private Long timbrado;

	@Temporal(TemporalType.DATE)
	private Date timbradoIncio;

	@Temporal(TemporalType.DATE)
	private Date timbradoFin;	
	
	private String RazonSocial;
	private String personaDocumento;
	private String direccion;
	
	private String cdc;
	private String qr;

	@ManyToOne
	@JoinColumn(name = "monedatipoid", nullable = false)
	private Tipo monedaTipo;
	
	private double monedaCambio;
	
	private double total10;
	private double total5;
	private double totalExcento;
	
	@Temporal(TemporalType.DATE)
	private Date fechaAnulacion;
	private	String anulacionUser;
	private boolean anulado = false;
	
	@Column(columnDefinition="TEXT")
	private String motivoAnulacion;
	
	public double getTotal10() {
		return total10;
	}

	public void setTotal10(double total10) {
		this.total10 = total10;
	}

	public double getTotal5() {
		return total5;
	}

	public void setTotal5(double total5) {
		this.total5 = total5;
	}

	public double getTotalExcento() {
		return totalExcento;
	}

	public void setTotalExcento(double totalExcento) {
		this.totalExcento = totalExcento;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getAnulacionUser() {
		return anulacionUser;
	}

	public void setAnulacionUser(String anulacionUser) {
		this.anulacionUser = anulacionUser;
	}

	public boolean isAnulado() {
		return anulado;
	}

	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
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

	public Long getVentaid() {
		return ventaid;
	}

	public void setVentaid(Long ventaid) {
		this.ventaid = ventaid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}

	public Tipo getCondicionVentaTipo() {
		return condicionVentaTipo;
	}

	public void setCondicionVentaTipo(Tipo condicionVentaTipo) {
		this.condicionVentaTipo = condicionVentaTipo;
	}

	public String getComprobanteNum() {
		return comprobanteNum;
	}

	public void setComprobanteNum(String comprobanteNum) {
		this.comprobanteNum = comprobanteNum;
	}

	public Long getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Long timbrado) {
		this.timbrado = timbrado;
	}

	public Date getTimbradoIncio() {
		return timbradoIncio;
	}

	public void setTimbradoIncio(Date timbradoIncio) {
		this.timbradoIncio = timbradoIncio;
	}

	public Date getTimbradoFin() {
		return timbradoFin;
	}

	public void setTimbradoFin(Date timbradoFin) {
		this.timbradoFin = timbradoFin;
	}

	public String getRazonSocial() {
		return RazonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		RazonSocial = razonSocial;
	}

	public String getPersonaDocumento() {
		return personaDocumento;
	}

	public void setPersonaDocumento(String personaDocumento) {
		this.personaDocumento = personaDocumento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Tipo getMonedaTipo() {
		return monedaTipo;
	}

	public void setMonedaTipo(Tipo monedaTipo) {
		this.monedaTipo = monedaTipo;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public double getMonedaCambio() {
		return monedaCambio;
	}

	public void setMonedaCambio(double monedaCambio) {
		this.monedaCambio = monedaCambio;
	}

	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
	
	
	
	
}
