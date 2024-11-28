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

import com.doxacore.modelo.Tipo;
import com.doxaerp.modelo.Deposito;
import com.doxaerp.modelo.Empresa;
import com.doxaerp.modelo.EmpresaUsuario;
import com.doxaerp.modelo.SucursalUsuario;
import com.doxaerp.modelo.Sucursal;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class EmpresaVM extends TemplateViewModelLocal{
	
	private List<Object[]> lEmpresas;
	private List<Object[]> lEmpresasOri;
	private Empresa empresaSelected;
	
	private boolean opCrearEmpresa;
	private boolean opEditarEmpresa;
	private boolean opBorrarEmpresa;

	private boolean editar = false;

	@Init(superclass = true)
	public void initEmpresaVM() {

		this.inicializarFiltros();
		this.cargarEmpresas();
		

	}

	@AfterCompose(superclass = true)
	public void afterComposeEmpresaVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearEmpresa = this.operacionHabilitada(ParamsLocal.OP_CREAR_EMPRESA);
		this.opEditarEmpresa = this.operacionHabilitada(ParamsLocal.OP_EDITAR_EMPRESA);
		this.opBorrarEmpresa = this.operacionHabilitada(ParamsLocal.OP_BORRAR_EMPRESA);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lEmpresas")
	public void filtrarEmpresa() {

		this.lEmpresas = this.filtrarListaObject(this.filtroColumns, this.lEmpresasOri);

	}

	private void cargarEmpresas() {

		String sql = this.um.getSql("empresa/listaEmpresa.sql");
		this.lEmpresas = this.reg.sqlNativo(sql);
		this.lEmpresasOri = this.lEmpresas;
		
		this.filtrarEmpresa();

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalEmpresaAgregar() {

		if (!this.opCrearEmpresa)
			return;

		this.editar = false;
		this.modalEmpresa(-1);

	}

	@Command
	public void modalEmpresa(@BindingParam("empresaid") long empresaid) {

	

		if (empresaid != -1) {

			if (!this.opEditarEmpresa)
				return;

			this.editar = true;

			this.empresaSelected = this.reg.getObjectById(Empresa.class.getName(), empresaid);

		} else {

			this.empresaSelected = new Empresa();
		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/empresaModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lEmpresas")
	public void guardar() {

		this.empresaSelected = this.save(this.empresaSelected);

		if (editar) {

			Notification.show("Empresa Actualizada.");

			this.editar = false;

		} else {
			
			EmpresaUsuario eu = new EmpresaUsuario();
			eu.setEmpresa(this.empresaSelected);
			eu.setUsuario(getCurrentUser());

			eu = this.save(eu);
			
			Sucursal s = new Sucursal();
			s.setEmpresa(this.empresaSelected);
			s.setNombre(this.empresaSelected.getRazonSocial()+" Matriz");
			s.setEstablecimiento("001");
			s.setComprobanteTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_COMPROBANTE_FACTURA));
		
			s = this.save(s);
			
			Deposito d = new Deposito();
			d.setEmpresa(this.empresaSelected);
			d.setNombre(this.empresaSelected.getRazonSocial()+" - Deposito Matriz");
			d.setSucursal(s);
			
			this.save(d);

			SucursalUsuario eus = new SucursalUsuario();
			eus.setEmpresa(eu.getEmpresa());
			eus.setUsuario(eu.getUsuario());
			eus.setActual(true);
			eus.setSucursal(s);
			
			eus = this.save(eus);
			
			
			
			Notification.show("Los datos de la Nueva Empresa fueron agragadas.");
		}
		
		this.cargarEmpresas();

		this.modal.detach();

	}

	public List<Object[]> getlEmpresas() {
		return lEmpresas;
	}

	public void setlEmpresas(List<Object[]> lEmpresas) {
		this.lEmpresas = lEmpresas;
	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	public boolean isOpCrearEmpresa() {
		return opCrearEmpresa;
	}

	public void setOpCrearEmpresa(boolean opCrearEmpresa) {
		this.opCrearEmpresa = opCrearEmpresa;
	}

	public boolean isOpEditarEmpresa() {
		return opEditarEmpresa;
	}

	public void setOpEditarEmpresa(boolean opEditarEmpresa) {
		this.opEditarEmpresa = opEditarEmpresa;
	}

	public boolean isOpBorrarEmpresa() {
		return opBorrarEmpresa;
	}

	public void setOpBorrarEmpresa(boolean opBorrarEmpresa) {
		this.opBorrarEmpresa = opBorrarEmpresa;
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
