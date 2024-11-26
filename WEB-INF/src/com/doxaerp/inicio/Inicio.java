package com.doxaerp.inicio;

import org.zkoss.zk.ui.Sessions;

import com.doxacore.login.UsuarioCredencial;
import com.doxacore.modelo.Usuario;
import com.doxacore.util.Register;
import com.doxaerp.modelo.EmpresaUsuario;

public class Inicio {


	public void beforeLogin() {
		
	}

	public void afterLogin() {
		
		System.out.println("=========== Iiciando Sistema DoxaERP ================");
		
		
		Register reg = new Register();
		UsuarioCredencial usuarioCredencial = (UsuarioCredencial) Sessions.getCurrent().getAttribute("userCredential");
		
		Usuario currentUser = reg.getObjectByColumn(Usuario.class, "account",
				usuarioCredencial.getAccount());
		
		String campo[] = {"usuario", "actual"};
		Object[] value = {currentUser, true};
		
		EmpresaUsuario eu =  reg.getObjectByColumns(EmpresaUsuario.class, campo, value);
		
		usuarioCredencial.setExtra(eu.getEmpresa().getRazonSocial());
		Sessions.getCurrent().setAttribute("userCredential", usuarioCredencial);
	}
	
}
