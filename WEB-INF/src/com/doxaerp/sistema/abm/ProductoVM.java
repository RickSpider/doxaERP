package com.doxaerp.sistema.abm;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxaerp.modelo.Producto;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class ProductoVM extends TemplateViewModelLocal{
	
	private List<Object[]> lProductos;
	private List<Object[]> lProductosOri;
	private Producto productoSelected;
	
	private Tipo tipoProducto;
	
	private boolean opCrearProducto;
	private boolean opEditarProducto;
	private boolean opBorrarProducto;

	private boolean editar = false;

	@Init(superclass = true)
	public void initProductoVM() {

		this.tipoProducto = this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_PRODUCTO_PRODUCTO);
		
		this.cargarProductos();
		this.inicializarFiltros();
	}

	@AfterCompose(superclass = true)
	public void afterComposeProductoVM() {
		
	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearProducto = this.operacionHabilitada(ParamsLocal.OP_CREAR_PRODUCTO);
		this.opEditarProducto = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PRODUCTO);
		this.opBorrarProducto = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PRODUCTO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lProductos")
	public void filtrarProducto() {

		this.lProductos = this.filtrarListaObject(this.filtroColumns, this.lProductosOri);

	}

	private void cargarProductos() {

		String sql = this.um.getSql("producto/listaProducto.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", this.tipoProducto.getTipoid()+"");;
		this.lProductos = this.reg.sqlNativo(sql);
		this.lProductosOri = this.lProductos;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalProductoAgregar() {

		if (!this.opCrearProducto)
			return;

		this.editar = false;
		this.modalProducto(-1);

	}

	@Command
	public void modalProducto(@BindingParam("productoid") long productoid) {
		
		this.inicializarFinders();

		if (productoid != -1) {

			if (!this.opEditarProducto)
				return;

			this.editar = true;

			this.productoSelected = this.reg.getObjectById(Producto.class.getName(), productoid);

		} else {

			this.productoSelected = new Producto();
			this.productoSelected.setProductoTipo(this.tipoProducto);
			this.productoSelected.setEmpresa(getCurrentEmpresa());
			this.productoSelected.setMonedaVentaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_MONEDA_GUARANIES));
			this.productoSelected.setIvaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_IVA_10));

		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/productoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lProductos")
	public void guardar() {

		this.productoSelected = this.save(this.productoSelected);

		this.cargarProductos();

		this.modal.detach();

		if (editar) {

			Notification.show("Producto Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos deL Producto fueron agragados.");
		}

	}
	
	private FinderModel ivaTipoFinder;
	private FinderModel monedaTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlIvaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_IVA );
		ivaTipoFinder = new FinderModel("IVA", sqlIvaTipo);
		
		String sqlmonedaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_MONEDA );
		monedaTipoFinder = new FinderModel("MONEDA", sqlmonedaTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.ivaTipoFinder.getNameFinder()) == 0) {

			this.ivaTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.ivaTipoFinder, "listFinder");

			return;
		}
		

		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.monedaTipoFinder, "listFinder");

			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.ivaTipoFinder.getNameFinder()) == 0) {

			this.ivaTipoFinder.setListFinder(this.filtrarListaObject(filter, this.ivaTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.ivaTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoFinder.setListFinder(this.filtrarListaObject(filter, this.monedaTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.monedaTipoFinder, "listFinder");

			return;
		}

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.ivaTipoFinder.getNameFinder()) == 0) {

			this.productoSelected.setIvaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}

		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.productoSelected.setMonedaVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}

	}


	
	public FinderModel getIvaTipoFinder() {
		return ivaTipoFinder;
	}

	public void setIvaTipoFinder(FinderModel ivaTipoFinder) {
		this.ivaTipoFinder = ivaTipoFinder;
	}

	public List<Object[]> getlProductos() {
		return lProductos;
	}

	public void setlProductos(List<Object[]> lProductos) {
		this.lProductos = lProductos;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public boolean isOpCrearProducto() {
		return opCrearProducto;
	}

	public void setOpCrearProducto(boolean opCrearProducto) {
		this.opCrearProducto = opCrearProducto;
	}

	public boolean isOpEditarProducto() {
		return opEditarProducto;
	}

	public void setOpEditarProducto(boolean opEditarProducto) {
		this.opEditarProducto = opEditarProducto;
	}

	public boolean isOpBorrarProducto() {
		return opBorrarProducto;
	}

	public void setOpBorrarProducto(boolean opBorrarProducto) {
		this.opBorrarProducto = opBorrarProducto;
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

	public FinderModel getMonedaTipoFinder() {
		return monedaTipoFinder;
	}

	public void setMonedaTipoFinder(FinderModel monedaTipoFinder) {
		this.monedaTipoFinder = monedaTipoFinder;
	}
	
	

}
