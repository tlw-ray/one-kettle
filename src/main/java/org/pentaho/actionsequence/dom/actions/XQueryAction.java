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

import java.net.URI;
import java.util.ArrayList;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class XQueryAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.XQueryLookupRule"; //$NON-NLS-1$
  public static final String DOCUMENT_ELEMENT = "document"; //$NON-NLS-1$
  public static final String QUERY_ELEMENT = "query"; //$NON-NLS-1$
  public static final String QUERY_RESULT_ELEMENT = "query-result"; //$NON-NLS-1$
  public static final String SOURCE_XML = "source-xml"; //$NON-NLS-1$
  public static final String PREPARED_COMPONENT_ELEMENT = "prepared_component"; //$NON-NLS-1$
  public static final String OUTPUT_RESULT_SET = "output-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_PREPARED_STATEMENT = "output-prepared_statement"; //$NON-NLS-1$
  public static final String DEFAULT_QUERY_RESULTS_NAME = "query_result"; //$NON-NLS-1$
  public static final String XML_DOCUMENT = "xml-document"; //$NON-NLS-1$
  public static final String LIVE_CONNECTION_ELEMENT = "live"; //$NON-NLS-1$
  public static final String TIMEOUT = "timeout"; //$NON-NLS-1$
  public static final String MAXROWS = "max_rows"; //$NON-NLS-1$

  protected static final String[] EXPECTED_RESOURCES = new String[] { DOCUMENT_ELEMENT };

  protected static final String[] EXPECTED_INPUTS = new String[] { DOCUMENT_ELEMENT, QUERY_ELEMENT };

  public XQueryAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public XQueryAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME )
        && ( ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + QUERY_ELEMENT ) != null ) //$NON-NLS-1$
        || ( element.selectSingleNode( ActionSequenceDocument.ACTION_INPUTS_NAME + "/" + QUERY_ELEMENT ) != null ) ); //$NON-NLS-1$;
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    String expectedOutput = QUERY_RESULT_ELEMENT;
    if ( getOutput( expectedOutput ) == null ) {
      IActionOutput[] actionOutputs = getOutputs( ActionSequenceDocument.RESULTSET_TYPE );
      if ( actionOutputs.length > 0 ) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[] { expectedOutput };
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setSharedConnection( IActionInputSource value ) {
    if ( value instanceof ActionInputConstant ) {
      throw new IllegalArgumentException();
    }
    setActionInputValue( PREPARED_COMPONENT_ELEMENT, value );
  }

  public IActionInput getSharedConnection() {
    return getInput( PREPARED_COMPONENT_ELEMENT );
  }

  public void setSourceXml( IActionInputSource value ) {
    setActionInputValue( DOCUMENT_ELEMENT, value );
  }

  public IActionInput getSourceXml() {
    return getInput( DOCUMENT_ELEMENT );
  }

  public void setQuery( IActionInputSource value ) {
    if ( ( value == null )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() == null ) ) ) {
      value = new ActionInputConstant( "", null );
    }
    setActionInputValue( QUERY_ELEMENT, value );
  }

  public IActionInput getQuery() {
    return getInput( QUERY_ELEMENT );
  }

  public void setLive( IActionInputSource value ) {
    setActionInputValue( LIVE_CONNECTION_ELEMENT, value );
  }

  public IActionInput getLive() {
    return getInput( LIVE_CONNECTION_ELEMENT );
  }

  public void setOutputResultSet( String publicOutputName ) {
    setOutput( QUERY_RESULT_ELEMENT, publicOutputName, ActionSequenceDocument.RESULTSET_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputPreparedStatement( null );
    }
  }

  public IActionOutput getOutputResultSet() {
    IActionOutput actionOutput = getOutput( QUERY_RESULT_ELEMENT );
    // This if statement provides backward compatibility for deprecated functionality.
    if ( actionOutput == null ) {
      IActionOutput[] outputs = getOutputs();
      for ( int i = 0; i < outputs.length; i++ ) {
        if ( !outputs[i].getName().equals( PREPARED_COMPONENT_ELEMENT ) ) {
          actionOutput = outputs[i];
          break;
        }
      }
    }
    return actionOutput;
  }

  public void setOutputPreparedStatement( String publicOutputName ) {
    setOutput( PREPARED_COMPONENT_ELEMENT, publicOutputName, ActionSequenceDocument.XQUERY_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputResultSet( null );
      IActionOutput[] actionOutputs = getOutputs();
      for ( int i = 0; i < actionOutputs.length; i++ ) {
        if ( !actionOutputs[i].getType().equals( ActionSequenceDocument.XQUERY_TYPE ) ) {
          actionOutputs[i].delete();
        }
      }
    }
  }

  public IActionOutput getOutputPreparedStatement() {
    return getOutput( PREPARED_COMPONENT_ELEMENT );
  }

  public IActionSequenceValidationError[] validate() {

    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( DOCUMENT_ELEMENT );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
        validationError = validateResource( DOCUMENT_ELEMENT );
        if ( validationError != null ) {
          switch ( validationError.errorCode ) {
            case ActionSequenceValidationError.INPUT_MISSING:
              validationError.errorMsg = "Missing source XML input parameter.";
              break;
            case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
              validationError.errorMsg = "Source XML input parameter references unknown variable.";
              break;
            case ActionSequenceValidationError.INPUT_UNINITIALIZED:
              validationError.errorMsg = "Source XML input parameter is uninitialized.";
              break;
          }
          errors.add( validationError );
        }
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        validationError.errorMsg = "Source XML input parameter references unknown variable.";
        errors.add( validationError );
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        validationError.errorMsg = "Source XML input parameter is uninitialized.";
        errors.add( validationError );
      }
    }

    validationError = validateInput( QUERY_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing query input parameter.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Query input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Query input parameter is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    validationError = validateOutput( PREPARED_COMPONENT_ELEMENT );
    if ( validationError != null ) {
      validationError = validateOutput( QUERY_RESULT_ELEMENT );
      if ( validationError != null ) {
        validationError.errorMsg = "Missing query results output parameter.";
        errors.add( validationError );
      }
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setQuery( new ActionInputConstant( "", null ) );
    setOutputResultSet( DEFAULT_QUERY_RESULTS_NAME );
  }

  public IActionResource setXmlDocument( URI uri, String mimeType ) {
    return setResourceUri( DOCUMENT_ELEMENT, uri, mimeType );
  }

  public IActionResource getXmlDocument() {
    return getResource( DOCUMENT_ELEMENT );
  }

  public void setQueryTimeout( IActionInputSource value ) {
    // Not implemented in the component but
    // plumbed through here for when it is.
    setActionInputValue( TIMEOUT, value );
  }

  public IActionInput getQueryTimeout() {
    // Not implemented in the component but
    // plumbed through here for when it is.
    return getInput( TIMEOUT );
  }

  public void setMaxRows( IActionInputSource value ) {
    setActionInputValue( MAXROWS, value );
  }

  public IActionInput getMaxRows() {
    return getInput( MAXROWS );
  }
}
