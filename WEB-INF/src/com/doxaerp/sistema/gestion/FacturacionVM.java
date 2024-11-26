package com.doxaerp.sistema.gestion;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Notification;

import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxaerp.modelo.Caja;
import com.doxaerp.modelo.Comprobante;
import com.doxaerp.modelo.Deposito;
import com.doxaerp.modelo.Persona;
import com.doxaerp.modelo.Producto;
import com.doxaerp.modelo.ProductoDeposito;
import com.doxaerp.modelo.SucursalUsuario;
import com.doxaerp.modelo.Suscripcion;
import com.doxaerp.modelo.Venta;
import com.doxaerp.modelo.VentaDetalle;
import com.doxaerp.modelo.VentaPago;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;


public class FacturacionVM extends TemplateViewModelLocal{
	
	private boolean opCrearFacturacion;
	private Venta ventaSelected;
	private VentaDetalle ventaDetalleSelected;
	private VentaPago ventaPagoSelected;
	private double vuelto;
	private FacturacionControl fc;
	private Deposito depositoSelected;
	
	private boolean disabledBotonCobrar = false;
	
	private Tipo tipoCondicionDefault;
	private Tipo tipoMonedaDefault;
	private Tipo tipoPagoEfectivoDefault;
	
	@Init(superclass = true)
	public void initFacturacionVM() {
		
		this.fc = new FacturacionControl();
		this.defaultDatos();
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
	public void onChangeFecha() {
		
		Caja caja = fc.getCurrentCaja(getCurrentSucursalUsuario(), this.ventaSelected.getFecha());
		
		if (caja == null) {
			
			this.mensajeError("No hay caja abierta para la fecha.");
			this.ventaSelected.setFecha(null);
			return;
		}
		
		this.ventaSelected.setCaja(caja);
		
	}
	
	private void defaultDatos() {
		
		this.tipoCondicionDefault  = this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_CONDICIONVENTA_CONTADO);
		this.tipoMonedaDefault= this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_MONEDA_GUARANIES);
		this.tipoPagoEfectivoDefault = this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_FORMAPAGO_EFECTIVO);
		
	}
	
	@Command
	@NotifyChange("*")
	public void limpiarVenta() {
		
		Optional<Deposito> oDeposito = this.getCurrentSucursal().getDepositos().stream()
                .filter(Deposito::isFacturacion) 
                .findFirst();
		
		if (!oDeposito.isPresent()) {
			
			Notification.show("No hay deposito de facturación asignado a esta sucursal." ,true);
			
		}else {
			
			this.depositoSelected = oDeposito.get();
			
		}
		
		
				
		//Tipo tipoComprobante = this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_COMPROBANTE_FACTURA);
		
		
		this.ventaDetalleSelected = null;
		
		
		
		this.ventaSelected = new Venta();
		//this.ventaSelected.setEmpresa(getCurrentEmpresa());
		SucursalUsuario su = this.getCurrentSucursalUsuario();
		this.ventaSelected.setSucursal(su.getSucursal());
		this.ventaSelected.setComprobanteTipo(su.getSucursal().getComprobanteTipo());
		
		this.ventaSelected.setCondicionVentaTipo(tipoCondicionDefault);
		this.ventaSelected.setMonedaTipo(tipoMonedaDefault);
		this.ventaSelected.setMonedaCambio(1);
		
		Comprobante comprobante = fc.getComprobante(su, new Date(), this.ventaSelected.getComprobanteTipo().getTipoid());
		
		if (comprobante == null) {
			
			this.mensajeError("No existe numeracion activa para este tipo de comprante.");
			
		}
		
		this.ventaSelected.setTimbrado(comprobante.getTimbrado());
		this.ventaSelected.setTimbradoEmision(comprobante.getEmision());
		this.ventaSelected.setTimbradoVencimiento(comprobante.getVencimiento());
		
		Caja caja = fc.getCurrentCaja(su, new Date());

		if (caja == null) {
			
			Notification.show("No hay caja abierta." ,true);
			this.disabledBotonCobrar = true;
		}else {
			
			this.disabledBotonCobrar = false;
			this.ventaSelected.setCaja(caja);
		}
		
		this.ventaPagoSelected = new VentaPago();
		this.ventaPagoSelected.setEmpresa(getCurrentEmpresa());
		//this.ventaPagoSelected.setVenta(this.ventaSelected);
		this.ventaPagoSelected.setFormaPagoTipo(tipoPagoEfectivoDefault);
		
		this.inicializarFinders();
	}
	
	//seccion finder
	
	private FinderModel personaFinder;	
	private FinderModel comprobanteTipoFinder;
	private FinderModel condicionVentaTipoFinder;
	private FinderModel monedaTipoFinder;
	
	private FinderModel productoFinder;
	private FinderModel suscripcionFinder;
	private FinderModel formaPagoTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String buscarTiposPorSiglaTipotipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql");

		String sqlComprobanteTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_COMPROBANTE );
		comprobanteTipoFinder = new FinderModel("Comprobante", sqlComprobanteTipo);
		
		String sqlCondicionVentatipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_CONDICIONVENTA );
		condicionVentaTipoFinder = new FinderModel("CondicionVenta", sqlCondicionVentatipo);
		
		String sqlMonedaTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_MONEDA );
		monedaTipoFinder = new FinderModel("Moneda", sqlMonedaTipo);
		
		String sqlPersonaBuscar = this.um.getSql("persona/buscarPersona.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		personaFinder = new FinderModel("Persona", sqlPersonaBuscar);
		
		String sqlProducto = this.um.getSql("producto/buscarProductoServicio.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		productoFinder = new FinderModel("Producto", sqlProducto);
		
		String sqlFormaPagoTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_FORMAPAGO );
		formaPagoTipoFinder = new FinderModel("FormaPago", sqlFormaPagoTipo);
		
		String sqlSuscripcion = this.um.getSql("suscripcion/buscarSuscripcion.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		suscripcionFinder = new FinderModel("Suscripcion", sqlSuscripcion);
		
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
		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.monedaTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.productoFinder.getNameFinder()) == 0) {

			this.productoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.productoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.formaPagoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.formaPagoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.suscripcionFinder.getNameFinder()) == 0) {

			this.suscripcionFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.suscripcionFinder, "listFinder");

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
		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.monedaTipoFinder.setListFinder(this.filtrarListaObject(filter, this.monedaTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.monedaTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}

		if (finder.compareTo(this.productoFinder.getNameFinder()) == 0) {

			this.productoFinder.setListFinder(this.filtrarListaObject(filter, this.productoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.productoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.formaPagoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.formaPagoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.formaPagoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.suscripcionFinder.getNameFinder()) == 0) {

			this.suscripcionFinder.setListFinder(this.filtrarListaObject(filter, this.suscripcionFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.suscripcionFinder, "listFinder");

			return;
		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			Comprobante comprobante = fc.getComprobante(this.getCurrentSucursalUsuario(), new Date(), this.ventaSelected.getComprobanteTipo().getTipoid());
			
			if (comprobante == null) {
				
				this.mensajeError("No existe numeracion activa para este tipo de comprante.");
				return;
			}
			
			this.ventaSelected.setTimbrado(comprobante.getTimbrado());
			this.ventaSelected.setTimbradoEmision(comprobante.getEmision());
			this.ventaSelected.setTimbradoVencimiento(comprobante.getVencimiento());
			
			return;
		}
		
		if (finder.compareTo(this.condicionVentaTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setCondicionVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.ventaSelected.setMonedaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {
			
			Persona p = this.reg.getObjectById(Persona.class.getName(), id);

			this.ventaSelected.setPersona(p);
			
			this.ventaSelected.setRazonSocial(p.getRazonSocial());
			this.ventaSelected.setPersonaDocumentoTipo(p.getDocumentoTipo());
			this.ventaSelected.setPersonaDocumento(p.getDocumentoNum());
			
			if (p.getDireccion() != null) {
				
				this.ventaSelected.setDireccion(p.getDireccion());
				
			}
			
			return;
		}

		if (finder.compareTo(this.productoFinder.getNameFinder()) == 0) {
			
			Producto p = this.reg.getObjectById(Producto.class.getName(), id);
			
			this.ventaDetalleSelected = new VentaDetalle();
			this.ventaDetalleSelected.setEmpresa(getCurrentEmpresa());
			this.ventaDetalleSelected.setVenta(this.ventaSelected);
			this.ventaDetalleSelected.setProducto(p);
			this.ventaDetalleSelected.setProductoDescripcion(p.getNombre());
			this.ventaDetalleSelected.setPrecio(p.getPrecioVenta());
			this.ventaDetalleSelected.setIvaTipo(p.getIvaTipo());
			this.ventaDetalleSelected.setUnidadMedidaTipo(p.getUnidadMedidaTipo());
			
			if (p.getProductoTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_PRODUCTO_PRODUCTO) == 0) {
				
				this.ventaDetalleSelected.setDeposito(depositoSelected);
				
			}
			
			
			return;
		}
		
		if (finder.compareTo(this.formaPagoTipoFinder.getNameFinder()) == 0) {

			this.ventaPagoSelected.setFormaPagoTipo(this.reg.getObjectById(Tipo.class.getName(), id));

			return;
		}
		
		
	if (finder.compareTo(this.suscripcionFinder.getNameFinder()) == 0) {
		
			Suscripcion s = this.reg.getObjectById(Suscripcion.class.getName(), id);
			
			Producto p = this.reg.getObjectById(Producto.class.getName(), s.getServicio().getProductoid());
			
			this.ventaDetalleSelected = new VentaDetalle();
			this.ventaDetalleSelected.setEmpresa(getCurrentEmpresa());
			this.ventaDetalleSelected.setVenta(this.ventaSelected);
			this.ventaDetalleSelected.setProducto(p);
			this.ventaDetalleSelected.setProductoDescripcion("Pago "+p.getNombre()+" "+s.getPersona().getRazonSocial());
			this.ventaDetalleSelected.setPrecio(p.getPrecioVenta());
			this.ventaDetalleSelected.setIvaTipo(p.getIvaTipo());
			this.ventaDetalleSelected.setUnidadMedidaTipo(p.getUnidadMedidaTipo());
			this.ventaDetalleSelected.setSuscripcion(s);
			
			return;
		}

	}
	
	@Command
	@NotifyChange("*")
	public void agregarDetalle() {
		
		this.ventaSelected.getDetalles().add(this.ventaDetalleSelected);
		
		this.ventaDetalleSelected = null;
		
		this.actualizarTotales();
		
	}
	
	@Command
	@NotifyChange("*")
	public void borrarDetalle(@BindingParam("ventaDetalle") VentaDetalle vd ) {
		
		this.ventaSelected.getDetalles().remove(vd);
		this.actualizarTotales();
	}
	
	@Command
	@NotifyChange("*")
	public void actualizarTotales() {
		
		double total10 = 0;
		double total5 = 0;
		double total0 = 0;
		
		for (VentaDetalle vdx : this.ventaSelected.getDetalles()) {
			
			if (vdx.getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_10) == 0) {
				
				total10 += (vdx.getCantidad()*vdx.getPrecio())-vdx.getDescuento();
				
				continue;
			}
			
			if (vdx.getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_5) == 0) {
				
				total5 += (vdx.getCantidad()*vdx.getPrecio())-vdx.getDescuento();
				continue;
			}
			
			if (vdx.getIvaTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_IVA_EXENTO) == 0) {
				
				total0 += (vdx.getCantidad()*vdx.getPrecio())-vdx.getDescuento();
				continue;
				
			}
			
		}
		
		
		
		this.ventaSelected.setTotal10(total10);
		this.ventaSelected.setTotal5(total5);
		this.ventaSelected.setTotalExento(total0);
		
		this.ventaPagoSelected.setMonto(total0+total5+total10);
		
		this.actualizarVuelto();
		
	}	
	
	@Command
	@NotifyChange("*")
	public void actualizarVuelto() {
		
		this.vuelto = this.getVentaPagoSelected().getMonto()-(this.ventaSelected.getTotal10()+this.ventaSelected.getTotal5()+this.ventaSelected.getTotalExento());
		
	}	
	
	@Command
	@NotifyChange("*")
	public void guardarVenta() {
		
		if (this.ventaSelected.getPersona() == null) {
			
			this.mensajeError("Debes cargar a una persona.");
			
			return;
			
		}
		
		if (this.ventaSelected.getDetalles().size()<=0) {
			
			this.mensajeError("No hay items Cargados");
			
			return;
			
		}
		
		if (this.vuelto < -1) {
			
			this.mensajeError("Verifique el pago, el vuelto no puede ser negativo");
			return;
		}
		
		if (this.ventaSelected.getFecha() == null) {
			
			this.ventaSelected.setFecha(new Date());
			
		}
		
		this.ventaSelected.setComprobanteNum(fc.getComprobanteNro(getCurrentSucursalUsuario(), new Date(), this.ventaSelected.getComprobanteTipo().getTipoid()));
		
		this.ventaPagoSelected.setVenta(ventaSelected);
		this.ventaSelected.getPagos().add(this.ventaPagoSelected);
		this.ventaSelected = this.save(this.ventaSelected);
		
		
		
		this.descontarStock(this.ventaSelected);
		
		this.actualizarSuscripcion(ventaSelected);
		
		Notification.show("Venta "+this.ventaSelected.getComprobanteNum()+" cargada Correctamente");
		
		this.limpiarVenta();
		
		
	}
	
	private void actualizarSuscripcion(Venta venta) {
		
		for (VentaDetalle vd : venta.getDetalles()) {

			
			if (vd.getSuscripcion() != null) {
				
				Suscripcion s = vd.getSuscripcion();
				
				if (vd.getSuscripcion().getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_ANUAL) == 0) {
					
					s.setVencimiento(this.um.calcularFecha(s.getVencimiento(), Calendar.YEAR, (int) vd.getCantidad()));
					
				}else if (vd.getSuscripcion().getSuscripcionTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_SUSCRIPCION_MENSUAL) == 0){
					
					s.setVencimiento(this.um.calcularFecha(s.getVencimiento(), Calendar.MONTH, (int) vd.getCantidad()));
				}
				
				this.save(s);
				
			}
			
		}
		
	}
	
	private void descontarStock(Venta venta) {
		
		String idsFiltrados = venta.getDetalles().stream()
                .filter(obj -> ParamsLocal.SIGLA_TIPO_PRODUCTO_PRODUCTO.equals(obj.getProducto().getProductoTipo().getSigla()))  // Filtrar productos
                .map(obj -> String.valueOf(obj.getProducto().getProductoid()))  // Convertir el id (Long) a String
                .collect(Collectors.joining(", "));  // Concatenar los ids
		
		if (idsFiltrados.length() <= 0) {
			
			return;
		}
		
		List<ProductoDeposito> lpd = this.reg.getAllObjectsByCondicion(ProductoDeposito.class, 
				"empresa.empresaid = "+this.getCurrentEmpresa().getEmpresaid()+"\n"
				+ "and deposito.depositoid = "+this.depositoSelected.getDepositoid()+"\n"
				+ "and producto.productoid in ("+idsFiltrados+")");
		
		System.out.println("El tamaño de lpd es: "+lpd.size() );
		
		if (lpd.size() <= 0) {
			
			return;
			
		}
		
		List<VentaDetalle> lvd = venta.getDetalles();
		
		Map<Long, VentaDetalle> mapaVentaDetalle = lvd.stream()
		        .collect(Collectors.toMap(vd -> vd.getProducto().getProductoid(), vd -> vd));

		for (ProductoDeposito pd : lpd) {
		    VentaDetalle ventaDetalle = mapaVentaDetalle.get(pd.getProducto().getProductoid());
		    
		    // Verificar si hay un detalle de venta para este producto
		    if (ventaDetalle != null) {
		        // Restar la cantidad del depósito
		    	
		    	System.out.println("El producto es: "+pd.getProducto().getNombre());
		    	System.out.println("cantidad es: "+pd.getCantidad());
		    	pd.setCantidad(pd.getCantidad() - ventaDetalle.getCantidad());
		    	System.out.println("descontado es: "+pd.getCantidad());
		    	
		    	this.save(pd);
		      
		    }else {
		    	
		    	System.out.println("fue null encontrar la venta detalle ");
		    	
		    }
		}
		
	//	this.reg.saveObject(lpd, this.getCurrentUser().getAccount());
		
		
	}
	
	public double getVuelto() {
		return vuelto;
	}

	public void setVuelto(double vuelto) {
		this.vuelto = vuelto;
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

	public FinderModel getMonedaTipoFinder() {
		return monedaTipoFinder;
	}

	public void setMonedaTipoFinder(FinderModel monedaTipoFinder) {
		this.monedaTipoFinder = monedaTipoFinder;
	}

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	public FinderModel getProductoFinder() {
		return productoFinder;
	}

	public void setProductoFinder(FinderModel productoFinder) {
		this.productoFinder = productoFinder;
	}

	public VentaDetalle getVentaDetalleSelected() {
		return ventaDetalleSelected;
	}

	public void setVentaDetalleSelected(VentaDetalle ventaDetalleSelected) {
		this.ventaDetalleSelected = ventaDetalleSelected;
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

	public boolean isDisabledBotonCobrar() {
		return disabledBotonCobrar;
	}

	public void setDisabledBotonCobrar(boolean disabledBotonCobrar) {
		this.disabledBotonCobrar = disabledBotonCobrar;
	}

	public FinderModel getSuscripcionFinder() {
		return suscripcionFinder;
	}

	public void setSuscripcionFinder(FinderModel suscripcionFinder) {
		this.suscripcionFinder = suscripcionFinder;
	}

	

}
