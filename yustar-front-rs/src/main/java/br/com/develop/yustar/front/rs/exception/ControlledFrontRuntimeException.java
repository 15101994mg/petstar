package br.com.develop.yustar.front.rs.exception;

public class ControlledFrontRuntimeException extends RuntimeException
{

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public ControlledFrontRuntimeException( Exception e )
	{
		super( e );
	}

	public ControlledFrontRuntimeException( String message, Throwable e )
	{
		super( message, e );
	}

	public ControlledFrontRuntimeException( String message )
	{
		super( message );
	}
}
