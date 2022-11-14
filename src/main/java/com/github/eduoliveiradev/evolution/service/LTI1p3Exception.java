/* Copyright (C) 2012, Carnegie Learning Inc. All Rights Reserved.
  This program is the subject of trade secrets and intellectual property 
  rights owned by Carnegie Learning. This legend must continue to appear 
  in the source code despite modifications or enhancements by any party.*/
package com.github.eduoliveiradev.evolution.service;

import java.util.Map;
import java.util.Properties;

@SuppressWarnings("serial")
public class LTI1p3Exception extends ClServiceException {

    @Override public LTI1p3Exception log( Integer n ){ return log(n, getClass()); }
    @Override public LTI1p3Exception log(){ return super.log(getClass()); }
    @Override public LTI1p3Exception withData( Map<String, ?> map ) { return withData( map, getClass() ); }
    @Override public LTI1p3Exception withData(Properties map) { return withData( map, getClass()); }
    @Override public LTI1p3Exception withDatum( String key, Object val ) { return withDatum(key, val, getClass()); }

    public LTI1p3Exception() { super(); }

	public LTI1p3Exception( String message, Throwable cause ) { super(message, cause); }

	public LTI1p3Exception( String message ) { super(message); }

	public LTI1p3Exception( Throwable cause ) { super(cause); }

}
