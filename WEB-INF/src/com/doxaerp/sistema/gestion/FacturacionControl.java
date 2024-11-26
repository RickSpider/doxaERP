package com.doxaerp.sistema.gestion;

import java.util.Date;

import com.doxacore.util.Control;
import com.doxacore.util.Register;
import com.doxaerp.modelo.Caja;
import com.doxaerp.modelo.Comprobante;
import com.doxaerp.modelo.SucursalUsuario;
import com.ibm.icu.text.SimpleDateFormat;

public class FacturacionControl extends Control {
	
	public FacturacionControl () {
		super();
	}
	
	public Comprobante getComprobante(SucursalUsuario su, Date fecha, long comprobantetipoid) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Comprobante comprobante = this.reg.getObjectByCondicion(Comprobante.class, 
				"empresa.empresaid = " + su.getEmpresa().getEmpresaid() + "\n"  
				+"and comprobanteTipo.tipoid = " +comprobantetipoid+" \n"
				+"and sucursal.sucursalid = "+su.getSucursal().getSucursalid() +"\n"
				+"and puntoExpedicion = '"+su.getPuntoExpedicion()+"' \n"
				+"and TO_CHAR(emision,'yyyy-MM-dd') <= '"+sdf.format(fecha)+"' \n"
				+"and TO_CHAR(vencimiento,'yyyy-MM-dd') >= '"+sdf.format(fecha)+"' \n"
				+"and activo = true \n");
		
		return comprobante;
		
	}
	
	public synchronized String getComprobanteNro(long comprobanteid) {
		
		Comprobante comprobante = this.reg.getObjectById(Comprobante.class.getName(), comprobanteid);
		
		if (comprobante == null) {
			
			return null;
			
		}
		
		String comprobanteNro = comprobante.getSucursal().getEstablecimiento()+"-"+comprobante.getPuntoExpedicion()+"-"+String.format("%07d", comprobante.getSiguiente());
		comprobante.setSiguiente(comprobante.getSiguiente()+1);
		
		this.reg.saveObject(comprobante,"sys");
		
		return comprobanteNro;
	}
	
	public synchronized String getComprobanteNro(SucursalUsuario su, Date fecha, long comprobantetipoid) {
		
		Comprobante comprobante = this.getComprobante(su, fecha, comprobantetipoid);
		
		if (comprobante == null) {
			
			return null;
			
		}
		
		String comprobanteNro = comprobante.getSucursal().getEstablecimiento()+"-"+comprobante.getPuntoExpedicion()+"-"+String.format("%07d", comprobante.getSiguiente());
		comprobante.setSiguiente(comprobante.getSiguiente()+1);
		
		this.reg.saveObject(comprobante,"sys");
		
		return comprobanteNro;
	}
	
	public Caja getCurrentCaja(SucursalUsuario su, Date fecha) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						
		 Caja caja = this.reg.getObjectByCondicion(Caja.class, 
				"empresa.empresaid = " + su.getEmpresa().getEmpresaid() + "\n"  
				+"and sucursal.sucursalid = "+su.getSucursal().getSucursalid() +"\n"
				+"and usuarioCaja.usuarioid = "+su.getUsuario().getUsuarioid() +"\n"
				+"and TO_CHAR(fechaApertura,'yyyy-MM-dd') =  '"+sdf.format(fecha)+"' \n"
				+"and fechaCierre is null");
		
		return caja;
		
	}

}
