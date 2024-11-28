package com.doxaerp.sistema.tesoreria;

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
import com.doxacore.modelo.Usuario;
import com.doxaerp.modelo.Caja;
import com.doxaerp.modelo.SucursalUsuario;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;
import com.ibm.icu.text.SimpleDateFormat;



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

		this.inicializarFiltros();
		this.cargarCajas();
		

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
		
		this.filtrarCaja();

	}
	
	public boolean existeCajaAbierta() {
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SucursalUsuario su = this.getCurrentSucursalUsuario();
		
		Caja caja =  this.reg.getObjectByCondicion(Caja.class,
						"empresa.empresaid = " + su.getEmpresa().getEmpresaid() + "\n"  
						+"and sucursal.sucursalid = "+su.getSucursal().getSucursalid() +"\n"
						+"and usuarioCaja.usuarioid = "+su.getUsuario().getUsuarioid() +"\n"
						+ "and fechaCierre is null");
		
		if (caja == null) {
			
			return false;
			
		}
		
		return true;
		
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
		
		if (this.existeCajaAbierta()) {
			
			this.mensajeInfo("El usuario tiene caja abierta, cierre la caja para crear otra.");
			
			return;
			
		}

		this.inicializarFinders();

		if (cajaid != -1) {

			if (!this.opEditarCaja)
				return;

			this.editar = true;

			this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);

		} else {

			this.cajaSelected = new Caja();
			
			SucursalUsuario su = this.getCurrentSucursalUsuario();
			
			this.cajaSelected.setEmpresa(su.getEmpresa());
			this.cajaSelected.setSucursal(su.getSucursal());
			this.cajaSelected.setUsuarioCaja(su.getUsuario());
			this.cajaSelected.setFechaApertura(new Date());
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/tesoreria/cajaModal.zul", this.mainComponent, null);
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
	
	@Command
	public void modalCerrar(@BindingParam("cajaid") long cajaid) {

		this.cajaSelected = this.reg.getObjectById(Caja.class.getName(), cajaid);

		if (this.cajaSelected.getFechaCierre() != null) {

			this.mensajeInfo("La caja ya esta cerrada.");
			return;

		}

		modal = (Window) Executions.createComponents("/sistema/zul/tesoreria/cerrarCajaModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}

	@Command
	@NotifyChange("*")
	public void cerrarCaja() {

		if (!this.opCrearCaja) {

			this.mensajeInfo("No tienes privilegios para cerrar la caja");
			return;
		}

		if (this.cajaSelected.getFechaCierre() != null) {

			this.mensajeInfo("La caja ya esta cerrada.");
			return;

		}

		this.cajaSelected.setFechaCierre(new Date());
		this.cajaSelected.setUsuarioCierre(this.getCurrentUser());

		this.save(this.cajaSelected);

		this.cargarCajas();

		this.modal.detach();

	}
	
	private FinderModel usuarioFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		SucursalUsuario su = this.getCurrentSucursalUsuario();
		
		String sqlUsuario = this.um.getSql("sucursalUsuario/buscarUsuarioPorSucursal.sql")
				.replace("?1", su.getEmpresa().getEmpresaid()+"" )
				.replace("?2", su.getSucursal().getSucursalid()+"");
		usuarioFinder = new FinderModel("Usuario", sqlUsuario);
		
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.usuarioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.usuarioFinder, "listFinder");

			return;
		}
		

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.usuarioFinder.setListFinder(this.filtrarListaObject(filter, this.usuarioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.usuarioFinder, "listFinder");

			return;
		}
	
	}

	@Command
	@NotifyChange({"usuarioSelected"})
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.usuarioFinder.getNameFinder()) == 0) {

			this.cajaSelected.setUsuarioCaja(this.reg.getObjectById(Usuario.class.getName(), id));
			return;
			
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

	public FinderModel getUsuarioFinder() {
		return usuarioFinder;
	}

	public void setUsuarioFinder(FinderModel usuarioFinder) {
		this.usuarioFinder = usuarioFinder;
	}
	
	

}
