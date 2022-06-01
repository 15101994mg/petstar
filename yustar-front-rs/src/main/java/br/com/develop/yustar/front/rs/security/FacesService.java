package br.com.develop.yustar.front.rs.security;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.develop.yustar.front.rs.exception.ErrorHandlingInterceptor;

public class FacesService {

	private static final Logger LOG = Logger.getLogger(ErrorHandlingInterceptor.class.getSimpleName());;

	private static String montarMensagem(FacesMessage.Severity severity, String titulo, String mensagem) {
		FacesMessage message = new FacesMessage(severity, titulo, mensagem);
		FacesContext.getCurrentInstance().addMessage(null, message);
		LOG.warning(mensagem);
		return message.getDetail();
	}

	public static void info(String mensagem) {
		info("Info", mensagem);

	}

	public static void info(String titulo, String mensagem) {
		montarMensagem(FacesMessage.SEVERITY_INFO, titulo, mensagem);
	}

	public static void warn(String mensagem) {
		warn("Warn", mensagem);

	}

	public static void warn(String titulo, String mensagem) {
		montarMensagem(FacesMessage.SEVERITY_WARN, titulo, mensagem);
	}

	public static void error(String mensagem) {
		error("Error", mensagem);

	}

	public static void error(String titulo, String mensagem) {
		montarMensagem(FacesMessage.SEVERITY_ERROR, titulo, mensagem);
	}

	public static void fatal(String mensagem) {
		fatal("Fatal", mensagem);

	}

	public static void fatal(String titulo, String mensagem) {
		montarMensagem(FacesMessage.SEVERITY_FATAL, titulo, mensagem);
	}

}
