package com.doxaerp.sistema.main;


import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

import com.doxaerp.modelo.Agendamiento;
import com.doxaerp.sistema.main.calendar.CalendarItem;
import com.doxaerp.util.ParamsLocal;
import com.doxaerp.util.TemplateViewModelLocal;
import com.ibm.icu.text.SimpleDateFormat;



public class CalendarVM extends TemplateViewModelLocal{

	private SimpleCalendarModel calendarModel;
	
	@Init(superclass = true)
	public void initCalendarVM() throws ParseException {
		
		this.calendarModel = new SimpleCalendarModel();
		this.dataCharger();
		
	}
	
	@AfterCompose(superclass = true)
	public void afterComposeCalendarVM(@ContextParam(ContextType.VIEW) Component view) {
		
		
	}
	
	
	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
	}
	
	public void dataCharger() throws ParseException {
		
		Calendar calendar = Calendar.getInstance();
		
		String sqlSuscripcion = this.um.getSql("calendar/listDatosSuscripcion.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		
		List<Object[]> ldatos = this.reg.sqlNativo(sqlSuscripcion);
		
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
		if (ldatos != null && ldatos.size() > 0) {
			
			for(Object[] x : ldatos) {
				
				CalendarItem ci = new CalendarItem();
				ci.setLocked(true);
				
				Date date = (Date) x[1];
				date = um.modificarHorasMinutosSegundos(date, 8, 0, 0, 0);
				calendar.setTime(date);
				ci.setBeginDate(calendar.getTime());
				
				date = (Date) x[2];
				date = um.modificarHorasMinutosSegundos(date, 9, 0, 0, 0);
				
				calendar.setTime(date);
				ci.setEndDate(calendar.getTime());
				
				ci.setTitle(x[3].toString());
				ci.setContent(x[4].toString());
				ci.setStyle("background-color: #FF0000; color: #FFFFFF;");
				this.calendarModel.add(ci);
			}
			
		}
		
		String sqlAgendamiento = this.um.getSql("calendar/listDatosAgendamiento.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"").replace("?2", this.getCurrentSucursal().getSucursalid()+"");
		List<Object[]> lAgendamietos = this.reg.sqlNativo(sqlAgendamiento);
		
		if (lAgendamietos != null && lAgendamietos.size() > 0) {
			
			for(Object[] x : lAgendamietos) {
				
				
				CalendarItem ci = new CalendarItem();
				ci.setLocked(true);
				
				ci.setBeginDate((Date) x[1]);
				
				ci.setEndDate((Date) x[2]);
				
				if (x[7].toString().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_SERVICIO) == 0) {
					
					ci.setTitle(x[5].toString());
					ci.setContent(x[6].toString());
					
				}else if (x[7].toString().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_RECORDATORIO) == 0) {
					
					ci.setTitle(x[3].toString());
					ci.setContent(x[4].toString());
					
				}
				
				
				
				
				ci.setStyle("background-color: #008f39; color: #FFFFFF;");
				
				this.calendarModel.add(ci);
			}
			
		}
		
	}
	
	private Window modal;
	private Agendamiento agendamientoSelected;
	
	/*@Listen(CalendarsEvent.ON_ITEM_CREATE + " = #calendars; " + CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
	public void agendamientoModal(CalendarsEvent event) {

		event.stopClearGhost();
		this.agendamientoSelected = (agendamientoSelected) event.getCalendarItem();
		
		if (agendamientoSelected == null) {
			
			this.agendamientoSelected = new agendamientoSelected();
			this.agendamientoSelected.setRecordatorioManual(true);
			this.agendamientoSelected.setBegin(event.getBeginDate());
			this.agendamientoSelected.setEnd(event.getEndDate());
			
		}
	
		modal = (Window) Executions.createComponents("/instituto/zul/inicio/agendamientoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}*/

	public SimpleCalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(SimpleCalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}
	
	

}
