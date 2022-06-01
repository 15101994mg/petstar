package br.com.develop.yustar.front.rs.exception;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolationException;


@Interceptor
@Priority( Interceptor.Priority.APPLICATION )
public class ErrorHandlingInterceptor implements Serializable
{

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(ErrorHandlingInterceptor.class.getSimpleName());;

	private static final Object STRING_VAZIA = "";

	@AroundInvoke
	public Object logarExcecoes( InvocationContext context ) throws Exception
	{
		try
		{
			return context.proceed();
		}
		catch ( Throwable t )
		{
			while ( ( t instanceof FacesException
			        || t instanceof ELException
			        || t instanceof ConstraintViolationException )
			        && t.getCause() != null )
			{
				t = t.getCause();
			}

			if ( t instanceof ControlledFrontRuntimeException )
			{
				return montarMensagemTratada( t, t.getMessage() );
				
			}
			else if ( t instanceof SQLException )
			{
				return montarMensagemNaoTratada( t );
			}
			else 
			{
				return montarMensagemNaoTratada( t );
			}
		}
	}

	private Object montarMensagemTratada( Throwable t, String mensagem )
    {
		FacesMessage message = new FacesMessage(
		        FacesMessage.SEVERITY_ERROR, "Erro", mensagem );
		FacesContext.getCurrentInstance().addMessage( null, message );
		LOG.warning( mensagem );
		return message.getDetail();
    }

	private Object montarMensagemNaoTratada( Throwable t )
    {
	    LOG.log(Level.SEVERE, "Erro ao executar aplicação", t );
	    FacesMessage message = new FacesMessage(
	            FacesMessage.SEVERITY_ERROR,
	            "Erro",
	            "Erro na aplicação.  Verifique o log do sistema e entre em contato com o desenvolvedor do site." );
	    FacesContext.getCurrentInstance().addMessage( null, message );

	    return message.getDetail();
    }
}
