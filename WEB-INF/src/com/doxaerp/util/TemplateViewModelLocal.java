package com.doxaerp.util;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.doxacore.TemplateViewModel;
import com.doxaerp.modelo.Empresa;
import com.doxaerp.modelo.EmpresaUsuario;
import com.doxaerp.modelo.EmpresaUsuarioSucursal;
import com.doxaerp.modelo.Sucursal;

public abstract class TemplateViewModelLocal extends TemplateViewModel{
	
	@Init(superclass = true)
	public void initTemplateViewModelLocal() {
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeTemplateViewModelLocal() {
		
	}
	
	private EmpresaUsuario getCurrentEmpresaUsuario() {
		
		String campo[] = {"usuario", "actual"};
		Object[] value = {this.getCurrentUser(), true};
		
		return this.reg.getObjectByColumns(EmpresaUsuario.class, campo, value);
		
	}
	
	protected Empresa getCurrentEmpresa() {
		
		return this.getCurrentEmpresaUsuario().getEmpresa();
	}
	
	protected Sucursal getCurrentSucursal() {
		
		String campo[] = {"empresaUsuario", "actual"};
		Object[] value = {this.getCurrentEmpresaUsuario(), true};
		
		EmpresaUsuarioSucursal eus =  this.reg.getObjectByColumns(EmpresaUsuarioSucursal.class, campo, value);
		
		return eus.getSucursal();
	}

}
