/*
 * Copyright 2002 - 2017 Hitachi Vantara.  All rights reserved.
 * 
 * This software was developed by Hitachi Vantara and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. TThe Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.actionsequence.dom.actions;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;

public abstract class AbstractRelationalDbAction extends ActionDefinition {

  public class FormatInput extends ActionInput {

    FormatInput( Element element, IActionParameterMgr actionParameterMgr ) {
      super( element, actionParameterMgr );
    }

    public Object getValue() {
      Format format = null;
      String formatPattern = getStringValue();
      if ( formatPattern != null ) {
        format = getFormat( formatPattern );
      }
      return format;
    }
  }

  public static final String QUERY_ELEMENT = "query"; //$NON-NLS-1$
  public static final String QUERY_NAME_ELEMENT = "query-name"; //$NON-NLS-1$
  public static final String PREPARED_COMPONENT_ELEMENT = "prepared_component"; //$NON-NLS-1$
  public static final String JNDI_ELEMENT = "jndi"; //$NON-NLS-1$
  public static final String DRIVER_ELEMENT = "driver"; //$NON-NLS-1$
  public static final String CONNECTION_ELEMENT = "connection"; //$NON-NLS-1$
  public static final String USER_ID_ELEMENT = "user-id"; //$NON-NLS-1$
  public static final String PASSWORD_ELEMENT = "password"; //$NON-NLS-1$
  public static final String TRANSFORM_ELEMENT = "transform"; //$NON-NLS-1$
  public static final String TRANSFORM_SORT_COLUMN_ELEMENT = "sort-by-col"; //$NON-NLS-1$
  public static final String TRANSFORM_PIVOT_COLUMN_ELEMENT = "pivot-column"; //$NON-NLS-1$
  public static final String TRANSFORM_MEASURES_COLUMN_ELEMENT = "measures-column"; //$NON-NLS-1$
  public static final String TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT = "format-type"; //$NON-NLS-1$
  public static final String TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT = "format-string"; //$NON-NLS-1$
  public static final String TRANSFORM_SORT_FORMAT_TYPE_ELEMENT = "sort-format-type"; //$NON-NLS-1$
  public static final String TRANSFORM_SORT_FORMAT_STRING_ELEMENT = "sort-format-string"; //$NON-NLS-1$
  public static final String TRANSFORM_ORDERED_MAPS = "ordered-maps"; //$NON-NLS-1$
  public static final String DECIMAL_FORMAT_TYPE = "decimal"; //$NON-NLS-1$
  public static final String DATE_FORMAT_TYPE = "decimal"; //$NON-NLS-1$
  public static final String QUERY_RESULT_ELEMENT = "query-result"; //$NON-NLS-1$
  public static final String OUTPUT_NAME_ELEMENT = "output-name"; //$NON-NLS-1$
  public static final String MAX_ROWS_ELEMENT = "max_rows"; //$NON-NLS-1$
  public static final String LIVE_CONNECTION_ELEMENT = "live"; //$NON-NLS-1$
  public static final String DB_URL_NAME = "db-url"; //$NON-NLS-1$
  public static final String OUTPUT_RESULT_SET = "output-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_PREPARED_STATEMENT = "output-prepared_statement"; //$NON-NLS-1$
  public static final String SHARED_CONNECTION = "shared-connection"; //$NON-NLS-1$
  public static final String RESULTSET_FORWARD_ONLY = "forward-only"; //$NON-NLS-1$
  // Added by Arijit Chatterjee "timeout in xaction file"
  public static final String TIMEOUT = "timeout"; //$NON-NLS-1$
  public static final String READ_ONLY_ELEMENT = "read_only"; //$NON-NLS-1$

  public AbstractRelationalDbAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public AbstractRelationalDbAction( String componentName ) {
    super( componentName );
  }

  public IActionInput getQuery() {
    IActionInput actionInput = getInput( QUERY_ELEMENT );

    // This is deprecated functionality for determining the name of the query input parameter.
    if ( actionInput.getValue() == null ) {
      Object queryName = getInput( QUERY_NAME_ELEMENT ).getValue();
      if ( queryName != null ) {
        actionInput = getInput( queryName.toString() );
      }
    }
    // End deprecated functionality

    return actionInput;
  }

  public void setQuery( IActionInputSource value ) {
    setActionInputValue( QUERY_ELEMENT, value );

    // Remove deprecated method of determining query that may exist.
    setActionInputValue( QUERY_NAME_ELEMENT, (IActionInputSource) null );
  }

  // Added by Arijit Chatterjee "Sets the value of timeout provided in xaction file"
  public void setQueryTimeout( IActionInputSource value ) {
    setActionInputValue( TIMEOUT, value );
  }

  // Added by Arijit Chatterjee. Gets the value of TIMEOUT
  public IActionInput getQueryTimeout() {
    return getInput( TIMEOUT );
  }

  public void setSharedConnection( IActionInputSource value ) {
    if ( value instanceof ActionInputConstant ) {
      throw new IllegalArgumentException();
    }
    setActionInputValue( PREPARED_COMPONENT_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setDriver( null );
      setDbUrl( null );
      setUserId( null );
      setPassword( null );
      setJndi( null );
    }
  }

  public IActionInput getSharedConnection() {
    return getInput( PREPARED_COMPONENT_ELEMENT );
  }

  public void setJndi( IActionInputSource value ) {
    setActionInputValue( JNDI_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setDriver( null );
      setDbUrl( null );
      setUserId( null );
      setPassword( null );
      setSharedConnection( null );
    }
  }

  public IActionInput getJndi() {
    return getInput( JNDI_ELEMENT );
  }

  public void setDriver( IActionInputSource value ) {
    setActionInputValue( DRIVER_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setSharedConnection( null );
    }
  }

  public IActionInput getDriver() {
    return getInput( DRIVER_ELEMENT );
  }

  public void setDbUrl( IActionInputSource value ) {
    setActionInputValue( CONNECTION_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setSharedConnection( null );
    }
  }

  public IActionInput getDbUrl() {
    return getInput( CONNECTION_ELEMENT );
  }

  public void setUserId( IActionInputSource value ) {
    setActionInputValue( USER_ID_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setSharedConnection( null );
    }
  }

  public IActionInput getUserId() {
    return getInput( USER_ID_ELEMENT );
  }

  public void setPassword( IActionInputSource value ) {
    setActionInputValue( PASSWORD_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setSharedConnection( null );
    }
  }

  public IActionInput getPassword() {
    return getInput( PASSWORD_ELEMENT );
  }

  public void setPerformTransform( IActionInputSource value ) {
    setActionInputValue( TRANSFORM_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setTransformMeasuresColumn( null );
      setTransformPivotColumn( null );
      setTransformSortColumn( null );
      setTransformPivotDataFormat( null );
      setTransformSortDataFormat( null );
    }
  }

  public IActionInput getPerformTransform() {
    return getInput( TRANSFORM_ELEMENT );
  }

  public void setTransformPivotColumn( IActionInputSource value ) {
    setActionInputValue( TRANSFORM_PIVOT_COLUMN_ELEMENT, value );
  }

  public IActionInput getTransformPivotColumn() {
    return getInput( TRANSFORM_PIVOT_COLUMN_ELEMENT );
  }

  public void setTransformMeasuresColumn( IActionInputSource value ) {
    setActionInputValue( TRANSFORM_MEASURES_COLUMN_ELEMENT, value );
  }

  public IActionInput getTransformMeasuresColumn() {
    return getInput( TRANSFORM_MEASURES_COLUMN_ELEMENT );
  }

  public void setTransformPivotDataFormat( IActionInputSource value ) {
    if ( ( value == null )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() == null ) ) ) {
      setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT, (IActionInputSource) null );
      setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
    } else if ( value instanceof ActionInputConstant ) {
      Object object = ( (ActionInputConstant) value ).getValue();
      if ( object instanceof DecimalFormat ) {
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT, new ActionInputConstant(
            ( (DecimalFormat) object ).toPattern(), this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT, new ActionInputConstant( DECIMAL_FORMAT_TYPE,
            this.actionParameterMgr ) );
      } else if ( object instanceof SimpleDateFormat ) {
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT, new ActionInputConstant(
            ( (SimpleDateFormat) object ).toPattern(), this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT, new ActionInputConstant( DATE_FORMAT_TYPE,
            this.actionParameterMgr ) );
      } else {
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT, new ActionInputConstant( object,
            this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
      }
    } else {
      setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT, value );
      setActionInputValue( TRANSFORM_PIVOT_DATA_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
    }
  }

  public IActionInput getTransformPivotDataFormat() {
    IActionInput actInput = getInput( TRANSFORM_PIVOT_DATA_FORMAT_STRING_ELEMENT );
    if ( actInput instanceof ActionInput ) {
      actInput =
          new FormatInput( ( (ActionInput) actInput ).getElement(), ( (ActionInput) actInput ).getParameterMgr() );
    } else if ( actInput instanceof ActionInputConstant ) {
      Format format = null;
      String formatPattern = actInput.getStringValue();
      if ( formatPattern != null ) {
        format = getFormat( formatPattern );
      }
      if ( format != null ) {
        actInput = new ActionInputConstant( format, actionParameterMgr );
      }
    }
    return actInput;
  }

  public void setTransformSortDataFormat( IActionInputSource value ) {
    if ( ( value == null )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() == null ) ) ) {
      setActionInputValue( TRANSFORM_SORT_FORMAT_STRING_ELEMENT, (IActionInputSource) null );
      setActionInputValue( TRANSFORM_SORT_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
    } else if ( value instanceof ActionInputConstant ) {
      Object object = ( (ActionInputConstant) value ).getValue();
      if ( object instanceof DecimalFormat ) {
        setActionInputValue( TRANSFORM_SORT_FORMAT_STRING_ELEMENT, new ActionInputConstant( ( (DecimalFormat) object )
            .toPattern(), this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_SORT_FORMAT_TYPE_ELEMENT, new ActionInputConstant( DECIMAL_FORMAT_TYPE,
            this.actionParameterMgr ) );
      } else if ( object instanceof SimpleDateFormat ) {
        setActionInputValue( TRANSFORM_SORT_FORMAT_STRING_ELEMENT, new ActionInputConstant(
            ( (SimpleDateFormat) object ).toPattern(), this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_SORT_FORMAT_TYPE_ELEMENT, new ActionInputConstant( DATE_FORMAT_TYPE,
            this.actionParameterMgr ) );
      } else {
        setActionInputValue( TRANSFORM_SORT_FORMAT_STRING_ELEMENT, new ActionInputConstant( object,
            this.actionParameterMgr ) );
        setActionInputValue( TRANSFORM_SORT_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
      }
    } else {
      setActionInputValue( TRANSFORM_SORT_FORMAT_STRING_ELEMENT, value );
      setActionInputValue( TRANSFORM_SORT_FORMAT_TYPE_ELEMENT, (IActionInputSource) null );
    }
  }

  public IActionInput getTransformSortDataFormat() {
    IActionInput actInput = getInput( TRANSFORM_SORT_FORMAT_STRING_ELEMENT );
    if ( actInput instanceof ActionInput ) {
      actInput =
          new FormatInput( ( (ActionInput) actInput ).getElement(), ( (ActionInput) actInput ).getParameterMgr() );
    } else if ( actInput instanceof ActionInputConstant ) {
      Format format = null;
      String formatPattern = actInput.getStringValue();
      if ( formatPattern != null ) {
        format = getFormat( formatPattern );
      }
      if ( format != null ) {
        actInput = new ActionInputConstant( format, actionParameterMgr );
      }
    }
    return actInput;
  }

  public void setTransformSortColumn( IActionInputSource value ) {
    setActionInputValue( TRANSFORM_SORT_COLUMN_ELEMENT, value );
  }

  public IActionInput getTransformSortColumn() {
    return getInput( TRANSFORM_SORT_COLUMN_ELEMENT );
  }

  public void setTransformOrderOutputColumns( IActionInputSource value ) {
    setActionInputValue( TRANSFORM_ORDERED_MAPS, value );
  }

  public IActionInput getTransformOrderOutputColumns() {
    return getInput( TRANSFORM_ORDERED_MAPS );
  }

  public void setOutputResultSet( String publicOutputName ) {
    // This removes deprecated functionality.
    setActionInputValue( OUTPUT_NAME_ELEMENT, (IActionInputSource) null );
    // End deprecated functionality.

    setOutput( QUERY_RESULT_ELEMENT, publicOutputName, ActionSequenceDocument.RESULTSET_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputPreparedStatement( null );
    }
  }

  public IActionOutput getOutputResultSet() {
    // This is deprecated functionality.
    Object outputName = getInput( OUTPUT_NAME_ELEMENT ).getValue();
    if ( outputName == null ) {
      outputName = QUERY_RESULT_ELEMENT;
    }
    // End deprecated functionality.

    IActionOutput actionOutput = getOutput( outputName.toString() );

    // More deprecated functionality.
    if ( actionOutput == null ) {
      IActionOutput[] actionOutputs = getOutputs();
      if ( actionOutputs.length > 0 ) {
        actionOutput = actionOutputs[0];
      }
    }
    // End deprecated functionality.

    return actionOutput;
  }

  public void setMaxRows( IActionInputSource value ) {
    setActionInputValue( MAX_ROWS_ELEMENT, value );
  }

  public IActionInput getMaxRows() {
    return getInput( MAX_ROWS_ELEMENT );
  }

  public void setReadOnly( IActionInputSource value ) {
    setActionInputValue( READ_ONLY_ELEMENT, value );
  }

  public IActionInput getReadOnly() {
    return getInput( READ_ONLY_ELEMENT );
  }

  public void setOutputPreparedStatement( String publicOutputName ) {
    setOutput( PREPARED_COMPONENT_ELEMENT, publicOutputName, ActionSequenceDocument.SQL_QUERY_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputResultSet( null );
      IActionOutput[] actionOutputs = getOutputs();
      for ( int i = 0; i < actionOutputs.length; i++ ) {
        if ( !actionOutputs[i].getType().equals( ActionSequenceDocument.SQL_QUERY_TYPE ) ) {
          actionOutputs[i].delete();
        }
      }
    }
  }

  public IActionOutput getOutputPreparedStatement() {
    return getOutput( PREPARED_COMPONENT_ELEMENT );
  }

  public void setLive( IActionInputSource value ) {
    setActionInputValue( LIVE_CONNECTION_ELEMENT, value );
  }

  public IActionInput getLive() {
    return getInput( LIVE_CONNECTION_ELEMENT );
  }

  public void setUseForwardOnlyResultSet( IActionInputSource value ) {
    setActionInputValue( RESULTSET_FORWARD_ONLY, value );
  }

  public IActionInput getUseForwardOnlyResultSet() {
    return getInput( RESULTSET_FORWARD_ONLY );
  }

  private Format getFormat( String formatPattern ) {
    Format format = null;
    try {
      format = new DecimalFormat( formatPattern.toString() );
    } catch ( Exception ex ) {
      try {
        format = new SimpleDateFormat( formatPattern.toString() );
      } catch ( Exception ex2 ) {
        format = null;
      }
    }
    return format;
  }
}
