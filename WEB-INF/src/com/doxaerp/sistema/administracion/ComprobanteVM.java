package com.doxaerp.sistema.administracion;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.doxaerp.modelo.Comprobante;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class ComprobanteVM extends TemplateViewModelLocal{
	
	private List<Object[]> lComprobantes;
	private List<Object[]> lComprobantesOri;
	private Comprobante comprobanteSelected;
	
	private boolean opCrearComprobante;
	private boolean opEditarComprobante;
	private boolean opBorrarComprobante;

	private boolean editar = false;

	@Init(superclass = true)
	public void initComprobanteVM() {

		this.cargarComprobantes();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeComprobanteVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearComprobante = this.operacionHabilitada(ParamsLocal.OP_CREAR_COMPROBANTE);
		this.opEditarComprobante = this.operacionHabilitada(ParamsLocal.OP_EDITAR_COMPROBANTE);
		this.opBorrarComprobante = this.operacionHabilitada(ParamsLocal.OP_BORRAR_COMPROBANTE);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[9];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lComprobantes")
	public void filtrarComprobante() {

		this.lComprobantes = this.filtrarListaObject(this.filtroColumns, this.lComprobantesOri);

	}

	private void cargarComprobantes() {

		String sql = this.um.getSql("comprobante/listaComprobante.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lComprobantes = this.reg.sqlNativo(sql);
		this.lComprobantesOri = this.lComprobantes;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalComprobanteAgregar() {

		if (!this.opCrearComprobante)
			return;

		this.editar = false;
		this.modalComprobante(-1);

	}

	@Command
	public void modalComprobante(@BindingParam("comprobanteid") long comprobanteid) {

	

		if (comprobanteid != -1) {

			if (!this.opEditarComprobante)
				return;

			this.editar = true;

			this.comprobanteSelected = this.reg.getObjectById(Comprobante.class.getName(), comprobanteid);

		} else {

			this.comprobanteSelected = new Comprobante();
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/comprobanteModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lComprobantes")
	public void guardar() {

		this.comprobanteSelected = this.save(this.comprobanteSelected);

		this.cargarComprobantes();

		this.modal.detach();

		if (editar) {

			Notification.show("Comprobante Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Comprobante fueron agragados.");
		}

	}

	public List<Object[]> getlComprobantes() {
		return lComprobantes;
	}

	public void setlComprobantes(List<Object[]> lComprobantes) {
		this.lComprobantes = lComprobantes;
	}

	public Comprobante getComprobanteSelected() {
		return comprobanteSelected;
	}

	public void setComprobanteSelected(Comprobante comprobanteSelected) {
		this.comprobanteSelected = comprobanteSelected;
	}

	public boolean isOpCrearComprobante() {
		return opCrearComprobante;
	}

	public void setOpCrearComprobante(boolean opCrearComprobante) {
		this.opCrearComprobante = opCrearComprobante;
	}

	public boolean isOpEditarComprobante() {
		return opEditarComprobante;
	}

	public void setOpEditarComprobante(boolean opEditarComprobante) {
		this.opEditarComprobante = opEditarComprobante;
	}

	public boolean isOpBorrarComprobante() {
		return opBorrarComprobante;
	}

	public void setOpBorrarComprobante(boolean opBorrarComprobante) {
		this.opBorrarComprobante = opBorrarComprobante;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}
	
	

}
