package com.doxaerp.sistema.administracion;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.doxaerp.modelo.Agendamiento;
import com.doxaerp.modelo.Producto;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class AgendamientoVM extends TemplateViewModelLocal{
	
	private List<Object[]> lAgendamientos;
	private List<Object[]> lAgendamientosOri;
	private Agendamiento agendamientoSelected;
	
	private boolean opCrearAgendamiento;
	private boolean opEditarAgendamiento;
	private boolean opBorrarAgendamiento;

	private boolean editar = false;

	@Init(superclass = true)
	public void initAgendamientoVM() {

		this.inicializarFiltros();
		this.cargarAgendamientos();
		

	}

	@AfterCompose(superclass = true)
	public void afterComposeAgendamientoVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearAgendamiento = this.operacionHabilitada(ParamsLocal.OP_CREAR_AGENDAMIENTO);
		this.opEditarAgendamiento = this.operacionHabilitada(ParamsLocal.OP_EDITAR_AGENDAMIENTO);
		this.opBorrarAgendamiento = this.operacionHabilitada(ParamsLocal.OP_BORRAR_AGENDAMIENTO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lAgendamientos")
	public void filtrarAgendamiento() {

		this.lAgendamientos = this.filtrarListaObject(this.filtroColumns, this.lAgendamientosOri);

	}

	private void cargarAgendamientos() {

		String sql = this.um.getSql("agendamiento/listaAgendamiento.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", this.getCurrentSucursal().getSucursalid()+"");
		
		this.lAgendamientos = this.reg.sqlNativo(sql);
		this.lAgendamientosOri = this.lAgendamientos;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalAgendamientoAgregar() {

		if (!this.opCrearAgendamiento)
			return;

		this.editar = false;
		this.modalAgendamiento(-1);

	}

	@Command
	public void modalAgendamiento(@BindingParam("agendamientoid") long agendamientoid) {

		if (agendamientoid != -1) {

			if (!this.opEditarAgendamiento)
				return;

			this.editar = true;

			this.agendamientoSelected = this.reg.getObjectById(Agendamiento.class.getName(), agendamientoid);

		} else {

			this.agendamientoSelected = new Agendamiento();
			this.agendamientoSelected.setSucursal(this.getCurrentSucursal());
			
			this.agendamientoSelected.setInicio(this.um.modificarHorasMinutosSegundos(new Date(), 9, 0, 0, 0));
			this.agendamientoSelected.setFin(this.um.modificarHorasMinutosSegundos(new Date(), 10, 0, 0, 0));
			this.agendamientoSelected.setSucursal(this.getCurrentSucursal());
			
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/agendamientoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lAgendamientos")
	public void guardar() {

		this.save(this.agendamientoSelected);

		this.modal.detach();

		if (editar) {

			Notification.show("Agendamiento Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos del Nuevo Agendamiento fueron agragados.");
		}
		
		this.cargarAgendamientos();

	}
	

	
	
	public List<Object[]> getlAgendamientos() {
		return lAgendamientos;
	}

	public void setlAgendamientos(List<Object[]> lAgendamientos) {
		this.lAgendamientos = lAgendamientos;
	}

	public Agendamiento getAgendamientoSelected() {
		return agendamientoSelected;
	}

	public void setAgendamientoSelected(Agendamiento agendamientoSelected) {
		this.agendamientoSelected = agendamientoSelected;
	}

	public boolean isOpCrearAgendamiento() {
		return opCrearAgendamiento;
	}

	public void setOpCrearAgendamiento(boolean opCrearAgendamiento) {
		this.opCrearAgendamiento = opCrearAgendamiento;
	}

	public boolean isOpEditarAgendamiento() {
		return opEditarAgendamiento;
	}

	public void setOpEditarAgendamiento(boolean opEditarAgendamiento) {
		this.opEditarAgendamiento = opEditarAgendamiento;
	}

	public boolean isOpBorrarAgendamiento() {
		return opBorrarAgendamiento;
	}

	public void setOpBorrarAgendamiento(boolean opBorrarAgendamiento) {
		this.opBorrarAgendamiento = opBorrarAgendamiento;
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
