package br.com.develop.yustar.front.security.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.develop.yustar.rs.model.UsuarioDTO;

@Named( value = Autorizador.BEAN_NAME )
@SessionScoped
public class Autorizador implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_NAME = "autorizador";
	
	public static final int CLIENTE = 1;

	public static final int FORNECEDOR = 2;

	public static final int ADMINISTRADOR = 3;
	
	@Inject
	private LoginAuthenticator loginAuth;

	
	public boolean autorizar(int valorCliente, int valorFornecedor, int valorAdministrador) {

		UsuarioDTO usu = loginAuth.getUsuarioLogado();
		if ( usu != null ) {
			
			switch (usu.getTipoUsuario()) {
			case CLIENTE:
				return valorCliente == CLIENTE;
			case FORNECEDOR:
				return valorFornecedor == FORNECEDOR;
			case ADMINISTRADOR:
				return valorAdministrador == ADMINISTRADOR;
			}
		}
		return false; 
	}

}
