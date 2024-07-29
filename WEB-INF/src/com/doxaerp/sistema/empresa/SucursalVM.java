package com.doxaerp.sistema.empresa;

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

import com.doxaerp.modelo.Sucursal;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class SucursalVM extends TemplateViewModelLocal{
	
	private List<Object[]> lSucursales;
	private List<Object[]> lSucursalesOri;
	private Sucursal sucursalSelected;
	
	private boolean opCrearSucursal;
	private boolean opEditarSucursal;
	private boolean opBorrarSucursal;

	private boolean editar = false;

	@Init(superclass = true)
	public void initSucursalVM() {

		this.cargarSucursales();
		this.inicializarFiltros();
	}

	@AfterCompose(superclass = true)
	public void afterComposeSucursalVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearSucursal = this.operacionHabilitada(ParamsLocal.OP_CREAR_SUCURSAL);
		this.opEditarSucursal = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SUCURSAL);
		this.opBorrarSucursal = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SUCURSAL);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lSucursales")
	public void filtrarSucursal() {

		this.lSucursales = this.filtrarListaObject(this.filtroColumns, this.lSucursalesOri);

	}

	private void cargarSucursales() {

		String sql = this.um.getSql("sucursal/listaSucursal.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lSucursales = this.reg.sqlNativo(sql);
		this.lSucursalesOri = this.lSucursales;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalSucursalAgregar() {

		if (!this.opCrearSucursal)
			return;

		this.editar = false;
		this.modalSucursal(-1);

	}

	@Command
	public void modalSucursal(@BindingParam("sucursalid") long sucursalid) {

	

		if (sucursalid != -1) {

			if (!this.opEditarSucursal)
				return;

			this.editar = true;

			this.sucursalSelected = this.reg.getObjectById(Sucursal.class.getName(), sucursalid);

		} else {

			this.sucursalSelected = new Sucursal();
			this.sucursalSelected.setEmpresa(getCurrentEmpresa());

		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/sucursalModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lSucursales")
	public void guardar() {

		this.sucursalSelected = this.save(this.sucursalSelected);

		this.cargarSucursales();

		this.modal.detach();

		if (editar) {

			Notification.show("Sucursal Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Sucursal fueron agragados.");
		}

	}

	public List<Object[]> getlSucursales() {
		return lSucursales;
	}

	public void setlSucursales(List<Object[]> lSucursales) {
		this.lSucursales = lSucursales;
	}

	public Sucursal getSucursalSelected() {
		return sucursalSelected;
	}

	public void setSucursalSelected(Sucursal sucursalSelected) {
		this.sucursalSelected = sucursalSelected;
	}

	public boolean isOpCrearSucursal() {
		return opCrearSucursal;
	}

	public void setOpCrearSucursal(boolean opCrearSucursal) {
		this.opCrearSucursal = opCrearSucursal;
	}

	public boolean isOpEditarSucursal() {
		return opEditarSucursal;
	}

	public void setOpEditarSucursal(boolean opEditarSucursal) {
		this.opEditarSucursal = opEditarSucursal;
	}

	public boolean isOpBorrarSucursal() {
		return opBorrarSucursal;
	}

	public void setOpBorrarSucursal(boolean opBorrarSucursal) {
		this.opBorrarSucursal = opBorrarSucursal;
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
