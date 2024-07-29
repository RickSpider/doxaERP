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

import com.doxaerp.modelo.Deposito;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class DepositoVM extends TemplateViewModelLocal{
	
	private List<Object[]> lDepositos;
	private List<Object[]> lDepositosOri;
	private Deposito depositoSelected;
	
	private boolean opCrearDeposito;
	private boolean opEditarDeposito;
	private boolean opBorrarDeposito;

	private boolean editar = false;

	@Init(superclass = true)
	public void initDepositoVM() {

		this.cargarDepositos();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeDepositoVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearDeposito = this.operacionHabilitada(ParamsLocal.OP_CREAR_DEPOSITO);
		this.opEditarDeposito = this.operacionHabilitada(ParamsLocal.OP_EDITAR_DEPOSITO);
		this.opBorrarDeposito = this.operacionHabilitada(ParamsLocal.OP_BORRAR_DEPOSITO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lDepositos")
	public void filtrarDeposito() {

		this.lDepositos = this.filtrarListaObject(this.filtroColumns, this.lDepositosOri);

	}

	private void cargarDepositos() {

		String sql = this.um.getSql("deposito/listaDeposito.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lDepositos = this.reg.sqlNativo(sql);
		this.lDepositosOri = this.lDepositos;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalDepositoAgregar() {

		if (!this.opCrearDeposito)
			return;

		this.editar = false;
		this.modalDeposito(-1);

	}

	@Command
	public void modalDeposito(@BindingParam("depositoid") long depositoid) {

	

		if (depositoid != -1) {

			if (!this.opEditarDeposito)
				return;

			this.editar = true;

			this.depositoSelected = this.reg.getObjectById(Deposito.class.getName(), depositoid);

		} else {

			this.depositoSelected = new Deposito();
			this.depositoSelected.setEmpresa(getCurrentEmpresa());
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/depositoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lDepositos")
	public void guardar() {

		this.depositoSelected = this.save(this.depositoSelected);

		this.cargarDepositos();

		this.modal.detach();

		if (editar) {

			Notification.show("Deposito Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Deposito fueron agragados.");
		}

	}

	public List<Object[]> getlDepositos() {
		return lDepositos;
	}

	public void setlDepositos(List<Object[]> lDepositos) {
		this.lDepositos = lDepositos;
	}

	public Deposito getDepositoSelected() {
		return depositoSelected;
	}

	public void setDepositoSelected(Deposito depositoSelected) {
		this.depositoSelected = depositoSelected;
	}

	public boolean isOpCrearDeposito() {
		return opCrearDeposito;
	}

	public void setOpCrearDeposito(boolean opCrearDeposito) {
		this.opCrearDeposito = opCrearDeposito;
	}

	public boolean isOpEditarDeposito() {
		return opEditarDeposito;
	}

	public void setOpEditarDeposito(boolean opEditarDeposito) {
		this.opEditarDeposito = opEditarDeposito;
	}

	public boolean isOpBorrarDeposito() {
		return opBorrarDeposito;
	}

	public void setOpBorrarDeposito(boolean opBorrarDeposito) {
		this.opBorrarDeposito = opBorrarDeposito;
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
