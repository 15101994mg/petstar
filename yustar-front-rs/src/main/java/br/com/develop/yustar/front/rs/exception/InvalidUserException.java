package br.com.develop.yustar.front.rs.exception;

public class InvalidUserException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String USUARIO_INVALIDO = "Usuario %s invalido.";
	private String usuario;

	public InvalidUserException(String usuario, Exception ex) {
		super(String.format(USUARIO_INVALIDO, usuario), ex);
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

}
