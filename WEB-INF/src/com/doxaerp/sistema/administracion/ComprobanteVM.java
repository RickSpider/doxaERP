package com.doxaerp.sistema.administracion;

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
import com.doxaerp.modelo.Comprobante;
import com.doxaerp.modelo.Sucursal;
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

		this.inicializarFiltros();
		this.cargarComprobantes();
		

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

		this.filtrarComprobante();
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

		this.inicializarFinders();

		if (comprobanteid != -1) {

			if (!this.opEditarComprobante)
				return;

			this.editar = true;

			this.comprobanteSelected = this.reg.getObjectById(Comprobante.class.getName(), comprobanteid);
			

		} else {

			this.comprobanteSelected = new Comprobante();
		//	this.comprobanteSelected.setEmpresa(this.getCurrentEmpresa());
		//	this.comprobanteSelected.setSucursal(this.getCurrentSucursal());

		}

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/comprobanteModal.zul", this.mainComponent, null);
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
	
	private FinderModel sucursalFinder;
	private FinderModel comprobanteTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlSucursal = this.um.getSql("sucursal/buscarSucursal.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		sucursalFinder = new FinderModel("Sucursal", sqlSucursal);

		String sqlComprobanteTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_COMPROBANTE );
		comprobanteTipoFinder = new FinderModel("Comprobantetipo", sqlComprobanteTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.sucursalFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalFinder.setListFinder(this.filtrarListaObject(filter, this.sucursalFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.sucursalFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.comprobanteSelected.setSucursal(this.reg.getObjectById(Sucursal.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
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

	public FinderModel getSucursalFinder() {
		return sucursalFinder;
	}

	public void setSucursalFinder(FinderModel sucursalFinder) {
		this.sucursalFinder = sucursalFinder;
	}

	public FinderModel getComprobanteTipoFinder() {
		return comprobanteTipoFinder;
	}

	public void setComprobanteTipoFinder(FinderModel comprobanteTipoFinder) {
		this.comprobanteTipoFinder = comprobanteTipoFinder;
	}
	
	

}
