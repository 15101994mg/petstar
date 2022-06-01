package br.com.develop.yustar.front.rs.exception;

public class InvalidPasswordException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SENHA_INVALIDA = "Senha invalida.";

	public InvalidPasswordException() {
		super(SENHA_INVALIDA, null);
	}

}
