package br.com.develop.yustar.front.security.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.develop.yustar.front.rs.exception.AuthenticationException;
import br.com.develop.yustar.front.rs.security.SecurityFrontClient;
import br.com.develop.yustar.rs.model.UsuarioDTO;


@Named( value = LoginAuthenticator.BEAN_NAME )
@SessionScoped
public class LoginAuthenticator implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String BEAN_NAME = "loginAuthenticatorCDI";

	private String login;

	private String senha;

	private boolean loggedIn;

	public LoginAuthenticator()
	{
		super();
	}
	
	@Inject
	private SecurityFrontClient securityFrontClient;

	private UsuarioDTO usuarioLogado;

	public String getLogin()
	{
		return this.login;
	}
	

	public void setLogin( final String login )
	{
		this.login = login;
	}

	public String getSenha()
	{
		return this.senha;
	}

	public void setSenha( final String senha )
	{
		this.senha = senha;
	}

	public boolean isLoggedIn()
	{
		return this.loggedIn;
	}

	public void setLoggedIn( final boolean loggedIn )
	{
		this.loggedIn = loggedIn;
	}

	public String doLogout()
	{
		try {
			HttpSession session = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.removeAttribute("usuario");
			setLoggedIn(false);
			return "/pages/public/login/loginpage.xhtml";
		}
		catch (Exception e) {
			
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Falha no logout da aplicação. Verificar log."));
			 e.printStackTrace();
			 return "/pages/public/login/loginpage.xhtml";
		}
	}

	public String doLogin()
	{
		try
		{
			if ( this.login != null ) {
				UsuarioDTO usuario = securityFrontClient.authenticate(this.login, this.senha);
				if ( usuario != null ) {
					this.usuarioLogado = usuario;
					return "HOME";
				}
			}
		}
		catch (AuthenticationException e) {
			adicionarMensagem("Falha de autenticação", e.getMessage());
		}
		catch ( final Exception e )
		{
			adicionarMensagem( "Falha de Autenticação",
					"Verificar log da aplicação" );
			e.printStackTrace();
		}
		return "LOGIN_ERRO";

	}

	public void adicionarMensagem( final String cabecalho, final String detalhe )
	{
		FacesContext.getCurrentInstance().addMessage( null,
				new FacesMessage( cabecalho, detalhe ) );

	}

	@Override
	public String toString()
	{
		return String.format(
				"LoginAuthenticator [login: %s, loggedIn: %s, usuario: %s]",
				this.login, this.loggedIn, this.login );
	}


	public UsuarioDTO getUsuarioLogado() {
		return usuarioLogado;
	}


	public void setUsuarioLogado(UsuarioDTO usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
}
