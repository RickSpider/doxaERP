package com.doxaerp.sistema.administracion;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxaerp.modelo.Persona;
import com.doxaerp.modelo.Venta;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;

public class FacturacionVM extends TemplateViewModelLocal{
	
	private boolean opCrearFacturacion;
	private Venta ventaSelected;
	
	@Init(superclass = true)
	public void initFacturacionVM() {
		
		this.limpiarVenta();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeFacturacionVM() {

	}

	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearFacturacion = this.operacionHabilitada(ParamsLocal.OP_CREAR_FACTURACION);
			
		
	}
	
	@Command
	@NotifyChange("*")
	public void limpiarVenta() {
		
		this.ventaSelected = new Venta();
		
	}
	
	//seccion finder
	
	private FinderModel personaFinder;	
	private FinderModel comprobanteTipoFinder;
	private FinderModel condicionVentaTipoFinder;
	private FinderModel monedaTipoTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlComprobanteTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_COMPROBANTE );
		comprobanteTipoFinder = new FinderModel("Comprobante", sqlComprobanteTipo);
		
		String sqlCondicionVentatipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_CONDICIONVENTA );
		condicionVentaTipoFinder = new FinderModel("CondicionVenta", sqlCondicionVentatipo);
		
		String sqlMonedaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_MONEDA );
		monedaTipoTipoFinder = new FinderModel("Moneda", sqlMonedaTipo);
		
		String sqlPersonaBuscar = this.um.getSql("persona/buscarPersona.sql");
		personaFinder = new FinderModel("Persona", sqlPersonaBuscar);
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.condicionVentaTipoFinder.getNameFinder()) == 0) {

			this.condicionVentaTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.condicionVentaTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.monedaTipoTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.monedaTipoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.condicionVentaTipoFinder.getNameFinder()) == 0) {

			this.condicionVentaTipoFinder.setListFinder(this.filtrarListaObject(filter, this.condicionVentaTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.condicionVentaTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.monedaTipoTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.monedaTipoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.monedaTipoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.condicionVentaTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setCondicionVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.monedaTipoTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setMonedaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {
			
			Persona p = this.reg.getObjectById(Persona.class.getName(), id);

			this.ventaSelected.setPersona(p);
			
			this.ventaSelected.setRazonSocial(p.getRazonSocial());
			this.ventaSelected.setPersonaDocumento(p.getDocumentoNum());
			
			if (p.getDireccion() != null) {
				
				this.ventaSelected.setDireccion(p.getDireccion());
				
			}
			
			return;
		}


	}

	public boolean isOpCrearFacturacion() {
		return opCrearFacturacion;
	}

	public void setOpCrearFacturacion(boolean opCrearFacturacion) {
		this.opCrearFacturacion = opCrearFacturacion;
	}

	public Venta getVentaSelected() {
		return ventaSelected;
	}

	public void setVentaSelected(Venta ventaSelected) {
		this.ventaSelected = ventaSelected;
	}

	public FinderModel getComprobanteTipoFinder() {
		return comprobanteTipoFinder;
	}

	public void setComprobanteTipoFinder(FinderModel comprobanteTipoFinder) {
		this.comprobanteTipoFinder = comprobanteTipoFinder;
	}

	public FinderModel getCondicionVentaTipoFinder() {
		return condicionVentaTipoFinder;
	}

	public void setCondicionVentaTipoFinder(FinderModel condicionVentaTipoFinder) {
		this.condicionVentaTipoFinder = condicionVentaTipoFinder;
	}

	public FinderModel getMonedaTipoTipoFinder() {
		return monedaTipoTipoFinder;
	}

	public void setMonedaTipoTipoFinder(FinderModel monedaTipoTipoFinder) {
		this.monedaTipoTipoFinder = monedaTipoTipoFinder;
	}
	
	

}
