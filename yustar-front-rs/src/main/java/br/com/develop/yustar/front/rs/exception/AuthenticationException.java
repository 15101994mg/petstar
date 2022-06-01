package br.com.develop.yustar.front.rs.exception;

public abstract class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthenticationException( String mensagem, Exception ex) {
		super(mensagem, ex);
	}

}
