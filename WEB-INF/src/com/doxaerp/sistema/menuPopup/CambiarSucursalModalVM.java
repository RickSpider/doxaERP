package com.doxaerp.sistema.menuPopup;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.login.UsuarioCredencial;
import com.doxacore.modelo.Usuario;
import com.doxaerp.modelo.EmpresaUsuario;
import com.doxaerp.modelo.SucursalUsuario;
import com.doxaerp.util.TemplateMenuPopup;

public class CambiarSucursalModalVM extends TemplateMenuPopup {
	
	private SucursalUsuario sucursalUsuarioSelected;

	
	@Init(superclass = true)
	public void initTemplateMenuPopup() {
		
		this.inicializarFinders();
		this.sucursalUsuarioSelected = this.getCurrentSucursalUsuario();
	}
	
	@AfterCompose(superclass = true)
	public void afterComposeTemplateMenuPopup() {

	}

	private FinderModel sucursalFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		EmpresaUsuario eu = this.getCurrentEmpresaUsuario();
		
		String sqlSucursal = this.um.getSql("sucursal/buscarSucursalPorUsuario.sql")
				.replace("?1", eu.getEmpresa().getEmpresaid()+"")
				.replace("?2", eu.getUsuario().getUsuarioid()+"");
		sucursalFinder = new FinderModel("SucursalUsuario", sqlSucursal);
		
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.sucursalFinder, "listFinder");

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
		
	
	}

	@Command
	@NotifyChange({"sucursalUsuarioSelected"})
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalUsuarioSelected = this.reg.getObjectById(SucursalUsuario.class.getName(), id);
			return;
			
		}


	}
	
	@Command
	public void guardar() {
		
		SucursalUsuario suOld = this.getCurrentSucursalUsuario();
		suOld.setActual(false);
		this.reg.saveObject(suOld, suOld.getUsuario().getAccount());
		
		this.sucursalUsuarioSelected.setActual(true);
		this.reg.saveObject(sucursalUsuarioSelected, sucursalUsuarioSelected.getUsuario().getAccount());
		
		this.windowModal.detach();
		Clients.evalJavaScript("window.location.reload();");
	
	}
	
	@Command
	 public void cerrarModal() {
		
        this.windowModal.detach();
        
    }

	public SucursalUsuario getSucursalUsuarioSelected() {
		return sucursalUsuarioSelected;
	}

	public void setSucursalUsuarioSelected(SucursalUsuario sucursalUsuarioSelected) {
		this.sucursalUsuarioSelected = sucursalUsuarioSelected;
	}

	public FinderModel getSucursalFinder() {
		return sucursalFinder;
	}

	public void setSucursalFinder(FinderModel sucursalFinder) {
		this.sucursalFinder = sucursalFinder;
	}


	
	
	

}
