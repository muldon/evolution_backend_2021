/* Copyright (C) 2012, Carnegie Learning Inc. All Rights Reserved.
  This program is the subject of trade secrets and intellectual property 
  rights owned by Carnegie Learning. This legend must continue to appear 
  in the source code despite modifications or enhancements by any party.*/
package cl.services.lti1p3.ag.receiver.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for exceptions used and thrown by members of the cl.services.lti1p3.ag.receiver
 * packages. needs:<ul>
 * <li> best practice is not to show stack traces to users,
 * however, we also need a way to identify errors when they occur.
 * Therefore, every exception has a mostly-unique "event" identifier
 * that can be displayed to the user and also logged. To make these
 * possible for users to read and relate them, we'll display them as base-36
 * in 4-digit chunks. To keep them relatively short, we only display the 
 * least-significant 5 digits to the user -- that should be enough to locate
 * the event in our logs. If our tutor client gets one of these, we should 
 * log the full event id, even if we don't show the user.
 * </ul>
 * Every time one of these exceptions is generated, you should follow one of these patterns.
 * <b>At a minimum, you must use the first.</b><p>
 * <code>
 * new ClServiceException(message).withDatum("key",value).log();  // log under runtime control<br>
 * new ClServiceException(message).withDatum("key",value).log(2);  // log two lines of stack trace<br>
 * new ClServiceException(message).withData(props).log(0);  // log message and data only<br>
 * </code>
 */
public abstract class ClServiceException extends Exception
{
	private static final long serialVersionUID = 5687262506386889014L;
	private final static Logger _logger = LoggerFactory.getLogger( ClServiceException.class );
	static final long epoch =  1430800000L*1000;  // 1000*seconds since 1970/1/1
//	static final long dsecsPerWk = 86400L*10*7;	// decasecs
	final long eventid = (System.currentTimeMillis() - epoch)/2;
	final String s = Long.toString(eventid, Character.MAX_RADIX);
	final String userfriendly = pack(s);
	private final Map<String,Object> _exception_data = new LinkedHashMap<String, Object>( );

    private Integer _stack_logged = null;
    /* 
     * return the last six characters of the argument as xxx-yyy
     */
	private static final String pack(String s) {
		int l = s.length();
		int a = (l < 6 ? 0 : l - 6);
		int b = (l < 3 ? 0 : l - 3);
		return s.substring(a, b) + '-' + s.substring(b);
	}
    public ClServiceException() {
		super();
	}

	public ClServiceException( String message, Throwable cause ) {
		super(message, cause);
	}

	public ClServiceException( String message ) {
		super(message);
	}

	public ClServiceException( Throwable cause ) {
		super(cause);
	}

    public final Map<String,Object> getExceptionData(){ return Collections.unmodifiableMap(_exception_data); }

	/** Adds the given set of exception data attributes to this exception.
	 * Returns {@code this} for fluent usage.
	 * <p>
	 * Because exceptions may not have type parameters, subclasses must
	 * implement this method to narrow its return type, like so:
	 * 
	 * <pre>
	 * return withData(data, getClass());
	 * </pre> */
	public abstract ClServiceException withData( Map<String, ?> data);
	public ClServiceException withData( Properties args)  { return withData(args, getClass()); }
	public ClServiceException withData(Object[] args) { return withData(args, getClass()); }
	
	protected final <T extends ClServiceException> T withData( Map<String, ?> map,
		Class<T> clazz) {
    	_exception_data.putAll( map );
    	return clazz.cast(this);
    }
	protected final <T extends ClServiceException> T withData(Properties map, Class<T> clazz) {
    	for ( Object key : map.keySet()) {
    		_exception_data.put(key.toString(), (String)map.get(key));
    	}
    	return clazz.cast(this);
	}



	/** Adds the given exception data attribute and value to this exception.
	 * Returns {@code this} for fluent usage.
	 * <p>
	 * See {@link #withData(Map)} for implementation notes. */
    public abstract ClServiceException withDatum(String key, Object val);

    protected final <T extends ClServiceException> T withDatum( String key, Object val, Class<T> clazz ) {
    	_exception_data.put( key, val );
    	return clazz.cast(this);
    }
    protected final <T extends ClServiceException> T withData( Object[] val, Class<T> clazz ) {
    	for ( Object o : val ) 
    		_exception_data.put( "arg", o);
    	return clazz.cast(this);
    }

	@Override
	public final String getMessage() {
		return super.getMessage() + " (event id " + userfriendly + ")";
	}
	
	public final String getEventId() { return userfriendly ; }

	private final Integer getDebugLevel() {
		String level = getClass().toString() + "-log";
		if (level.startsWith("class ")) level = level.substring("class ".length());
		return Integer.getInteger(level, getDefaultDebugLevel());
	}

    /** Subclasses can override this to control logging level.
     *  Non-null ==> we'll print log message and exception data.
     *  N > 0 ==> print N lines of stack trace
     **/
    protected Integer getDefaultDebugLevel(){ return null; }

	/** Clients should use this method to provide a hook for logging that can be
	 * optionally activated at runtime. In the absence of any runtime
	 * configuration, this method will <em>not</em> produce any logging output;
	 * if you want output by default, use {@link #log(Integer)} instead.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * throw new ClServiceException().log();
	 * </pre>
	 * 
	 * See {@link #withData(Map)} for implementation notes. */
    public abstract ClServiceException log();

    protected <T extends ClServiceException> T log(Class<T> clazz){ return log( getDebugLevel(), clazz ); }

	/** Clients should use this method to log thrown exceptions. Unlike
	 * {@link #log()}, this method <em>does</em> produce output even without any
	 * runtime configuration.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * throw new ClServiceException().log(2);
	 * </pre>
	 * 
	 * See {@link #withData(Map)} for implementation notes.
	 * 
	 * @param n If {@code 0}, logs just the exception name, message, and
	 *            attributes. If {@code >0}, also logs that many stack frames. */
    public abstract ClServiceException log( Integer n );

    protected <T extends ClServiceException> T log( Integer n, Class<T> clazz )
    {
		if ( n != null ) {
			_logger.error( getClass().toString() + ": " + getMessage() +_exception_data.toString() );

            // TODO do we want to check for some regex in the stack trace, and log that ?
            // TODO or the first line of the stack trace?
			if ( n > 0 && ( _stack_logged == null || n > _stack_logged ))
            {
				StackTraceElement[] stack = this.getStackTrace();
				_logger.error( StringUtils.join( Arrays.asList( Arrays.copyOfRange( stack, 0, n ) ), "\n" ) );
			}
            _stack_logged = (_stack_logged == null ? n : Math.max( _stack_logged, n ));
		}
        return clazz.cast(this);
	}
    private int defaultStatus = 500;
    public void setHTTPstatus( int s ) { defaultStatus = s; }
    /**
	 * Subclasses should override for use by general-purpose ExceptionMappers if
	 * no specific ExceptionMapper is defined or configured.
	 */
    public int defaultHTTPstatus() { return defaultStatus; }
}
