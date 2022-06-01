package br.com.develop.yustar.front.rs.exception;

public class BusinessFrontException extends ControlledFrontRuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessFrontException( final Exception e )
	{
		super( e );
	}

	public BusinessFrontException( final String message, final Throwable e )
	{
		super( message, e );
	}

	public BusinessFrontException( final String message )
	{
		super( message );
	}
	
	public BusinessFrontException( String message, Object[] args )
	{
		super( String.format( message, args ) );
	}

}
