package com.doxaerp.sistema.gestion;

import java.util.Calendar;
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
import com.doxaerp.modelo.Suscripcion;
import com.doxaerp.modelo.Venta;
import com.doxaerp.modelo.VentaDetalle;
import com.doxaerp.modelo.VentaPago;
import com.doxaerp.modelo.Caja;
import com.doxaerp.modelo.Comprobante;
import com.doxaerp.modelo.Persona;
import com.doxaerp.modelo.Producto;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;



public class SuscripcionVM extends TemplateViewModelLocal{
	
	private List<Object[]> lSuscripciones;
	private List<Object[]> lSuscripcionesOri;
	private Suscripcion suscripcionSelected;
	
	private boolean opCrearSuscripcion;
	private boolean opEditarSuscripcion;
	private boolean opBorrarSuscripcion;

	private boolean editar = false;
	
	private Venta ventaSelected;
	private VentaDetalle ventaDetalleSelected;
	private VentaPago ventaPagoSelected;
	private FacturacionControl fc;
	
	private Date vencimiento;

	
	@Init(superclass = true)
	public void initSuscripcionVM() {

		this.cargarSuscripciones();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeSuscripcionVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearSuscripcion = this.operacionHabilitada(ParamsLocal.OP_CREAR_SUSCRIPCION);
		this.opEditarSuscripcion = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SUSCRIPCION);
		this.opBorrarSuscripcion = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SUSCRIPCION);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lSuscripciones")
	public void filtrarSuscripcion() {

		this.lSuscripciones = this.filtrarListaObject(this.filtroColumns, this.lSuscripcionesOri);

	}

	private void cargarSuscripciones() {

		String sql = this.um.getSql("suscripcion/listaSuscripcion.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lSuscripciones = this.reg.sqlNativo(sql);
		this.lSuscripcionesOri = this.lSuscripciones;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalSuscripcionAgregar() {

		if (!this.opCrearSuscripcion)
			return;

		this.editar = false;
		this.modalSuscripcion(-1);

	}

	@Command
	public void modalSuscripcion(@BindingParam("suscripcionid") long suscripcionid) {

		this.inicializarFinders();
		
		if (suscripcionid != -1) {

			if (!this.opEditarSuscripcion)
				return;

			this.editar = true;

			this.suscripcionSelected = this.reg.getObjectById(Suscripcion.class.getName(), suscripcionid);

		} else {

			this.suscripcionSelected = new Suscripcion();
			//this.suscripcionSelected.setEmpresa(getCurrentEmpresa());
			this.suscripcionSelected.setInicio(new Date());
			this.suscripcionSelected.setVencimiento(this.suscripcionSelected.getInicio());
			this.suscripcionSelected.setSuscripcionTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_SUSCRIPCION_MENSUAL));
		

		}

		modal = (Window) Executions.createComponents("/sistema/zul/gestion/suscripcionModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lSuscripciones")
	public void guardar() {

		this.suscripcionSelected = this.save(this.suscripcionSelected);

		this.cargarSuscripciones();

		this.modal.detach();

		if (editar) {

			Notification.show("Suscripcion Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Suscripcion fueron agragados.");
		}

	}

	private FinderModel personaFinder;
	private FinderModel servicioFinder;
	private FinderModel suscripcionTipoFinder;
	
	private FinderModel comprobanteTipoFinder;
	private FinderModel formaPagoTipoFinder;
	
	private FinderModel documentoTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlPersona = this.um.getSql("persona/buscarPersona.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		
		personaFinder = new FinderModel("Persona", sqlPersona);
		
		String sqlServicio = this.um.getSql("servicio/buscarServicio.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		
		servicioFinder = new FinderModel("Servicio", sqlServicio);
		
		String buscarTiposPorSiglaTipotipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql");
		
		String sqlSuscripconTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_SUSCRIPCION );
		suscripcionTipoFinder = new FinderModel("SuscripcionTipo", sqlSuscripconTipo);
		
		String sqlComprobanteTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_COMPROBANTE );
		comprobanteTipoFinder = new FinderModel("Comprobante", sqlComprobanteTipo);
		
		String sqlFormaPagoTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_FORMAPAGO );
		formaPagoTipoFinder = new FinderModel("FormaPago", sqlFormaPagoTipo);
		
		String sqlDocumentoTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_DOCUMENTO );
		documentoTipoFinder = new FinderModel("DocumentoTipo", sqlDocumentoTipo);

		
	
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.servicioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.servicioFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.suscripcionTipoFinder.getNameFinder()) == 0) {

			this.suscripcionTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.suscripcionTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.formaPagoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.formaPagoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.documentoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.documentoTipoFinder, "listFinder");

			return;
		}
		
		

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.servicioFinder.setListFinder(this.filtrarListaObject(filter, this.servicioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.servicioFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.suscripcionTipoFinder.getNameFinder()) == 0) {

			this.suscripcionTipoFinder.setListFinder(this.filtrarListaObject(filter, this.suscripcionTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.suscripcionTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.formaPagoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.formaPagoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.formaPagoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.documentoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.documentoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.formaPagoTipoFinder, "listFinder");

			return;
		}
	
	}

	@Command
	@NotifyChange({"suscripcionSelected", "ventaSelected", "ventaDetalleSelected", "ventaPagoSelected"})
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.suscripcionSelected.setPersona(this.reg.getObjectById(Persona.class.getName(), id));
			return;
			
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.suscripcionSelected.setServicio(this.reg.getObjectById(Producto.class.getName(), id));
			return;
			
		}

		if (finder.compareTo(this.suscripcionTipoFinder.getNameFinder()) == 0) {
			
			int calendarTipo;

			this.suscripcionSelected.setSuscripcionTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			/*if (this.suscripcionSelected.getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_ANUAL) == 0) {
				
				//this.suscripcionSelected.setVencimiento(this.um.calcularFecha(this.suscripcionSelected.getInicio(),Calendar.YEAR, 1));
				
				calendarTipo = Calendar.YEAR;
				
			}else if (this.suscripcionSelected.getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_MENSUAL) == 0) {
				
				//this.suscripcionSelected.setVencimiento(this.um.calcularFecha(this.suscripcionSelected.getInicio(),Calendar.MONTH, 1));
				calendarTipo = Calendar.MONTH;
			}
			
			this.suscripcionSelected.setVencimiento(this.um.calcularFecha(this.suscripcionSelected.getInicio(),Calendar.MONTH, 1));*/
			
			
			return;
			
		}
		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {
			
			Comprobante comprobante = fc.getComprobante(getCurrentSucursalUsuario(), new Date(), id);
			
			if (comprobante == null) {
				
				this.mensajeError("No existe numeracion activa para este tipo de comprante.");
				return;
			
			}
			
			if(comprobante.getTimbrado() != null) {
				
				this.ventaSelected.setTimbrado(comprobante.getTimbrado());
				
			}else {
				
				this.ventaSelected.setTimbrado(null);
				
			}
			
			this.ventaSelected.setTimbradoEmision(comprobante.getEmision());
			this.ventaSelected.setTimbradoVencimiento(comprobante.getVencimiento());
			
			this.ventaSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.ventaPagoSelected.setFormaPagoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
		
			return;
		}

		
		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setPersonaDocumentoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
		
			return;
		}
	}
	
	
	
	@Command
	public void SuscripcionPagarModal(@BindingParam("suscripcionid") long suscripcionid) {
		
		this.inicializarFinders();
		
		this.suscripcionSelected = this.reg.getObjectById(Suscripcion.class.getName(), suscripcionid);
		
		if (!this.suscripcionSelected.isActivo()) {
			
			this.mensajeInfo("La suscripcion esta inactiva no se puede pagar");
			return;
			
		}
		
		fc = new FacturacionControl();
		
		Caja caja = fc.getCurrentCaja(getCurrentSucursalUsuario(), new Date());
		
		if (caja == null) {
			
			this.mensajeInfo("No hay caja habilitada");
			
			return;
			
		}

		//this.suscripcionSelected.setVencimiento(this.um.calcularFecha(this.suscripcionSelected.getVencimiento(), Calendar.MONTH, (int) ventaDetalleSelected.getCantidad()));
		
		ventaSelected = new Venta();
		//ventaSelected.setEmpresa(this.getCurrentEmpresa());
		ventaSelected.setSucursal(this.getCurrentSucursal());
		ventaSelected.setCaja(caja);
		
		Persona p = this.suscripcionSelected.getPersona();
		
		ventaSelected.setPersona(p);
		ventaSelected.setPersonaDocumentoTipo(p.getDocumentoTipo());
		ventaSelected.setRazonSocial(p.getRazonSocial());
		ventaSelected.setPersonaDocumento(p.getDocumentoNum());
		ventaSelected.setDireccion(p.getDireccion());
		
		ventaSelected.setComprobanteTipo(ventaSelected.getSucursal().getComprobanteTipo());
		ventaSelected.setCondicionVentaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_CONDICIONVENTA_CONTADO));
		ventaSelected.setMonedaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_MONEDA_GUARANIES));
		
		
		
		Comprobante comprobante = fc.getComprobante(getCurrentSucursalUsuario(), new Date(), this.ventaSelected.getComprobanteTipo().getTipoid());
		
		
		if (comprobante == null) {
			
			this.mensajeError("No existe numeracion activa para este tipo de comprante.");
			
		}
		
		this.ventaSelected.setTimbrado(comprobante.getTimbrado());
		this.ventaSelected.setTimbradoEmision(comprobante.getEmision());
		this.ventaSelected.setTimbradoVencimiento(comprobante.getVencimiento());
		
		ventaDetalleSelected = new VentaDetalle();
		ventaDetalleSelected.setEmpresa(getCurrentEmpresa());
		ventaDetalleSelected.setProducto(this.suscripcionSelected.getServicio());
		ventaDetalleSelected.setPrecio(this.suscripcionSelected.getServicio().getPrecioVenta());
		ventaDetalleSelected.setProductoDescripcion("Suscripcion: "+this.suscripcionSelected.getServicio().getNombre());
		ventaDetalleSelected.setIvaTipo(this.suscripcionSelected.getServicio().getIvaTipo());
		ventaDetalleSelected.setUnidadMedidaTipo(this.suscripcionSelected.getServicio().getUnidadMedidaTipo());
		ventaDetalleSelected.setSuscripcion(this.suscripcionSelected);
				
		ventaPagoSelected = new VentaPago();
		ventaPagoSelected.setEmpresa(getCurrentEmpresa());
		ventaPagoSelected.setFormaPagoTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_FORMAPAGO_EFECTIVO));
		ventaPagoSelected.setMonto(this.suscripcionSelected.getServicio().getPrecioVenta());
		
		this.actualizarVencimiento();

		modal = (Window) Executions.createComponents("/sistema/zul/gestion/suscripcionPagarModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	@NotifyChange("*")
	@Command
	public void actualizarVencimiento() {
		
		
		if (this.suscripcionSelected.getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_ANUAL) == 0) {
			
			this.vencimiento = this.um.calcularFecha(this.suscripcionSelected.getVencimiento(), Calendar.YEAR, (int) ventaDetalleSelected.getCantidad());
			
		}else if (this.suscripcionSelected.getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_MENSUAL) == 0){
			
			this.vencimiento = this.um.calcularFecha(this.suscripcionSelected.getVencimiento(), Calendar.MONTH, (int) ventaDetalleSelected.getCantidad());
		}
		

		this.actualizarTotal();		
	}
	
	@Command
	@NotifyChange({"ventaPagoSelected"})
	public void actualizarTotal() {
		
		double monto= (this.suscripcionSelected.getServicio().getPrecioVenta()*this.ventaDetalleSelected.getCantidad()) - this.ventaDetalleSelected.getDescuento();
		
		this.ventaPagoSelected.setMonto(monto);
		
		if (this.suscripcionSelected.getServicio().getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_10)==0){
			
			this.ventaSelected.setTotal10(monto);
			
		}else if (this.suscripcionSelected.getServicio().getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_5)==0) {
			
			this.ventaSelected.setTotal5(monto);
			
		}else if (this.suscripcionSelected.getServicio().getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_EXENTO)==0) {
			
			this.ventaSelected.setTotalExento(monto);
			
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void generarPago() {
		
		if (this.ventaSelected.getComprobanteTipo() == null) {
			
			this.mensajeError("Debes seleccionar un tipo de comprobante");
			
			return;
			
		}
		
	
		
		this.suscripcionSelected.setVencimiento(this.vencimiento);
		this.save(this.suscripcionSelected);
		
		this.ventaDetalleSelected.setVenta(this.ventaSelected);
		this.ventaPagoSelected.setVenta(this.ventaSelected);
		this.ventaSelected.getDetalles().add(this.ventaDetalleSelected);
		this.ventaSelected.getPagos().add(this.ventaPagoSelected);
		
		if (this.getVentaSelected().getFecha() == null) {
			
			this.ventaSelected.setFecha(new Date());
			
		}
		
		
		
		this.ventaSelected.setComprobanteNum(fc.getComprobanteNro(getCurrentSucursalUsuario(), new Date(), this.ventaSelected.getComprobanteTipo().getTipoid()));
		
		
		
		this.save(ventaSelected);
		
		this.modal.detach();
		
		this.cargarSuscripciones();
		
		Notification.show("Suscripcion pagada");
		
	}
	
	
	private List<Object[]> lPagos;
	
	@Command
	public void modalListaPagos(@BindingParam("suscripcionid") long suscripcionid) {

		String sql = this.um.getSql("suscripcion/listaPagosSuscripcion.sql").replace("?1",suscripcionid+"");
		
		this.lPagos = this.reg.sqlNativo(sql);

		modal = (Window) Executions.createComponents("/sistema/zul/gestion/suscripcionListaPagosModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	

	public List<Object[]> getlPagos() {
		return lPagos;
	}

	public void setlPagos(List<Object[]> lPagos) {
		this.lPagos = lPagos;
	}

	public List<Object[]> getlSuscripciones() {
		return lSuscripciones;
	}

	public void setlSuscripciones(List<Object[]> lSuscripciones) {
		this.lSuscripciones = lSuscripciones;
	}

	public Suscripcion getSuscripcionSelected() {
		return suscripcionSelected;
	}

	public void setSuscripcionSelected(Suscripcion suscripcionSelected) {
		this.suscripcionSelected = suscripcionSelected;
	}

	public boolean isOpCrearSuscripcion() {
		return opCrearSuscripcion;
	}

	public void setOpCrearSuscripcion(boolean opCrearSuscripcion) {
		this.opCrearSuscripcion = opCrearSuscripcion;
	}

	public boolean isOpEditarSuscripcion() {
		return opEditarSuscripcion;
	}

	public void setOpEditarSuscripcion(boolean opEditarSuscripcion) {
		this.opEditarSuscripcion = opEditarSuscripcion;
	}

	public boolean isOpBorrarSuscripcion() {
		return opBorrarSuscripcion;
	}

	public void setOpBorrarSuscripcion(boolean opBorrarSuscripcion) {
		this.opBorrarSuscripcion = opBorrarSuscripcion;
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

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	public FinderModel getSuscripcionTipoFinder() {
		return suscripcionTipoFinder;
	}

	public void setSuscripcionTipoFinder(FinderModel suscripcionTipoFinder) {
		this.suscripcionTipoFinder = suscripcionTipoFinder;
	}

	public FinderModel getServicioFinder() {
		return servicioFinder;
	}

	public void setServicioFinder(FinderModel servicioFinder) {
		this.servicioFinder = servicioFinder;
	}

	public Venta getVentaSelected() {
		return ventaSelected;
	}

	public void setVentaSelected(Venta ventaSelected) {
		this.ventaSelected = ventaSelected;
	}

	public VentaDetalle getVentaDetalleSelected() {
		return ventaDetalleSelected;
	}

	public void setVentaDetalleSelected(VentaDetalle ventaDetalleSelected) {
		this.ventaDetalleSelected = ventaDetalleSelected;
	}

	public FinderModel getComprobanteTipoFinder() {
		return comprobanteTipoFinder;
	}

	public void setComprobanteTipoFinder(FinderModel comprobanteTipoFinder) {
		this.comprobanteTipoFinder = comprobanteTipoFinder;
	}

	public VentaPago getVentaPagoSelected() {
		return ventaPagoSelected;
	}

	public void setVentaPagoSelected(VentaPago ventaPagoSelected) {
		this.ventaPagoSelected = ventaPagoSelected;
	}

	public FinderModel getFormaPagoTipoFinder() {
		return formaPagoTipoFinder;
	}

	public void setFormaPagoTipoFinder(FinderModel formaPagoTipoFinder) {
		this.formaPagoTipoFinder = formaPagoTipoFinder;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public FinderModel getDocumentoTipoFinder() {
		return documentoTipoFinder;
	}

	public void setDocumentoTipoFinder(FinderModel documentoTipoFinder) {
		this.documentoTipoFinder = documentoTipoFinder;
	}

	
	

}
