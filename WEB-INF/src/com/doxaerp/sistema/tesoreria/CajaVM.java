package com.doxaerp.sistema.tesoreria;

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

import com.doxaerp.modelo.Caja;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class CajaVM extends TemplateViewModelLocal{
	
	private List<Object[]> lCajas;
	private List<Object[]> lCajasOri;
	private Caja cajaSelected;
	
	private boolean opCrearCaja;
	private boolean opEditarCaja;
	private boolean opBorrarCaja;

	private boolean editar = false;

	@Init(superclass = true)
	public void initCajaVM() {

		this.cargarCajas();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeCajaVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearCaja = this.operacionHabilitada(ParamsLocal.OP_CREAR_CAJA);
		this.opEditarCaja = this.operacionHabilitada(ParamsLocal.OP_EDITAR_CAJA);
		this.opBorrarCaja = this.operacionHabilitada(ParamsLocal.OP_BORRAR_CAJA);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[9];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lCajas")
	public void filtrarCaja() {

		this.lCajas = this.filtrarListaObject(this.filtroColumns, this.lCajasOri);

	}

	private void cargarCajas() {

		String sql = this.um.getSql("caja/listaCaja.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lCajas = this.reg.sqlNativo(sql);
		this.lCajasOri = this.lCajas;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalCajaAgregar() {

		if (!this.opCrearCaja)
			return;

		this.editar = false;
		this.modalCaja(-1);

	}

	@Command
	public void modalCaja(@BindingParam("cajaid") long cajaid) {

	

		if (cajaid != -1) {

			if (!this.opEditarCaja)
				return;

			this.editar = true;

			this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);

		} else {

			this.cajaSelected = new Caja();
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/cajaModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lCajas")
	public void guardar() {

		this.cajaSelected = this.save(this.cajaSelected);

		this.cargarCajas();

		this.modal.detach();

		if (editar) {

			Notification.show("Caja Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Caja fueron agragados.");
		}

	}

	public List<Object[]> getlCajas() {
		return lCajas;
	}

	public void setlCajas(List<Object[]> lCajas) {
		this.lCajas = lCajas;
	}

	public Caja getCajaSelected() {
		return cajaSelected;
	}

	public void setCajaSelected(Caja cajaSelected) {
		this.cajaSelected = cajaSelected;
	}

	public boolean isOpCrearCaja() {
		return opCrearCaja;
	}

	public void setOpCrearCaja(boolean opCrearCaja) {
		this.opCrearCaja = opCrearCaja;
	}

	public boolean isOpEditarCaja() {
		return opEditarCaja;
	}

	public void setOpEditarCaja(boolean opEditarCaja) {
		this.opEditarCaja = opEditarCaja;
	}

	public boolean isOpBorrarCaja() {
		return opBorrarCaja;
	}

	public void setOpBorrarCaja(boolean opBorrarCaja) {
		this.opBorrarCaja = opBorrarCaja;
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
