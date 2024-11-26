package com.doxaerp.sistema.administracion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.doxacore.report.ReportBigExcel;
import com.doxaerp.modelo.Venta;
import com.doxaerp.util.TemplateViewModelLocal;



public class VentaVM extends TemplateViewModelLocal{
	
	private List<Object[]> lVentas;
	private List<Object[]> lVentasOri;
	private Venta ventaSelected;
	
	private Date fechaDesde;
	private Date fechaHasta;

	private boolean editar = false;

	@Init(superclass = true)
	public void initVentaVM() {

		fechaDesde = this.um.modificarHorasMinutosSegundos(new Date(), 0,0,0,0);

		fechaHasta = this.um.modificarHorasMinutosSegundos(this.fechaDesde, 23, 59, 59, 999);
		
		this.cargarVentas();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeVentaVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[8];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lVentas")
	public void filtrarVenta() {

		this.lVentas = this.filtrarListaObject(this.filtroColumns, this.lVentasOri);

	}

	private void cargarVentas() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sql = this.um.getSql("venta/listaVenta.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", sdf.format(fechaDesde))
				.replace("?3", sdf.format(fechaHasta));
		
		this.lVentas = this.reg.sqlNativo(sql);
		this.lVentasOri = this.lVentas;

	}
	
	@Command
	@NotifyChange("*")
	public void onChangeFiltroFechas() {
		
		cargarVentas();
		
	}
	
	@Command
	public void exportarExcel() {
				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		List<String[]> titulos = new ArrayList<String[]>();
		
		
		String[] t1 = {"Reporte de Ventas"};
		String[] t2 = {this.getCurrentEmpresa().getRazonSocial()};
		String[] t3 = {"Fecha Desde:", sdf.format(this.fechaDesde)};
		String[] t4 = {"Fecha Hasta:", sdf.format(this.fechaHasta)};
		String[] espacioBlanco = {""};

		titulos.add(t1);
		titulos.add(t2);
		titulos.add(espacioBlanco);
		titulos.add(t3);
		titulos.add(t4);
		titulos.add(espacioBlanco);
		
		List<String[]> headersDatos = new ArrayList<String[]>();
		String [] hd1 =  {"FECHA","RAZONSOCIAL","TIMBRA", "COMPROBANTE", "NUMERO", "CONDICION", "MONEDA", "TOTAL"};
		headersDatos.add(hd1);
		
		/*String sql = this.um.getSql("comprobanteElectronico/listaComprobantesElectronicos.sql")
				.replace("?1", sdf.format(desde)).replace("?2", sdf.format(hasta))
				.replace("?3", this.contribuyenteSelected.getContribuyenteid() + "");
		
		List<Object[]> datos = this.reg.sqlNativo(sql);*/
		
		List<Object[]> detalles = new ArrayList<>();
		
		for (Object[] ox : this.lVentas) {
			
			Object[] o = new Object[8];
			
				
			o[0] = ox[0].toString();
			o[1] = ox[1].toString();
			o[2] = ox[2].toString();
			o[3] = ox[3].toString();
			o[4] = ox[4].toString();
			o[5] = ox[5].toString();
			o[6] = ox[6].toString();	
			o[7] = ox[7].toString();	
			
			detalles.add(o);
		}
		
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		ReportBigExcel rv = new ReportBigExcel("RVENTAS_"+sdf2.format(new Date()));
		rv.descargar(titulos, headersDatos, detalles);
		
	}

	public List<Object[]> getlVentas() {
		return lVentas;
	}

	public void setlVentas(List<Object[]> lVentas) {
		this.lVentas = lVentas;
	}

	public Venta getVentaSelected() {
		return ventaSelected;
	}

	public void setVentaSelected(Venta ventaSelected) {
		this.ventaSelected = ventaSelected;
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

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	
	

}
