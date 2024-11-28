package com.doxaerp.sistema.main;


import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.calendar.impl.DefaultCalendarItem;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;

import com.doxaerp.sistema.main.calendar.CalendarItem;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
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
				ci.setBeginDate((Date) x[2]);
				
				ci.setEndDate((Date) x[3]);
				
				ci.setTitle(x[1].toString());
				ci.setContent(x[4].toString());
				ci.setStyle("background-color: #008f39; color: #FFFFFF;");
				
				this.calendarModel.add(ci);
			}
			
		}
		
	}

	public SimpleCalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(SimpleCalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}
	
	

}
