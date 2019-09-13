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
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class JasperReportAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.jasper.JasperReportsComponent"; //$NON-NLS-1$
  public static final String OUTPUT_TYPE_ELEMENT = "output-type"; //$NON-NLS-1$
  public static final String REPORT_OUTPUT_ELEMENT = "report-output"; //$NON-NLS-1$
  public static final String DRIVER_ELEMENT = "driver"; //$NON-NLS-1$
  public static final String CONNECTION_ELEMENT = "connection"; //$NON-NLS-1$
  public static final String USER_ID_ELEMENT = "user-id"; //$NON-NLS-1$
  public static final String PASSWORD_ELEMENT = "password"; //$NON-NLS-1$
  public static final String JNDI_ELEMENT = "jndi"; //$NON-NLS-1$
  public static final String REPORT_DEFINITION_ELEMENT = "report-definition"; //$NON-NLS-1$
  public static final String OUTPUT_REPORT = "output-report"; //$NON-NLS-1$

  public static final String[] EXPECTED_RESOURCES = new String[] { REPORT_DEFINITION_ELEMENT };

  protected static final String[] EXPECTED_INPUTS = new String[] { OUTPUT_TYPE_ELEMENT, DRIVER_ELEMENT,
    CONNECTION_ELEMENT, USER_ID_ELEMENT, PASSWORD_ELEMENT, JNDI_ELEMENT };

  public JasperReportAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public JasperReportAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setComponentDefinition( OUTPUT_TYPE_ELEMENT, "html" ); //$NON-NLS-1$
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    String expectedOutput = REPORT_OUTPUT_ELEMENT;
    if ( getOutput( expectedOutput ) == null ) {
      IActionOutput[] actionOutputs = getOutputs( ActionSequenceDocument.CONTENT_TYPE );
      if ( actionOutputs.length > 0 ) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[] { expectedOutput };
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setConnection( IActionInputSource value ) {
    setActionInputValue( CONNECTION_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
    }
  }

  public IActionInput getConnection() {
    return getInput( CONNECTION_ELEMENT );
  }

  public void setUserId( IActionInputSource value ) {
    setActionInputValue( USER_ID_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
    }
  }

  public IActionInput getUserId() {
    return getInput( USER_ID_ELEMENT );
  }

  public void setDriver( IActionInputSource value ) {
    setActionInputValue( DRIVER_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
    }
  }

  public IActionInput getDriver() {
    return getInput( DRIVER_ELEMENT );
  }

  public void setPassword( IActionInputSource value ) {
    setActionInputValue( PASSWORD_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {

      setJndi( null );
    }
  }

  public IActionInput getPassword() {
    return getInput( PASSWORD_ELEMENT );
  }

  public void setJndi( IActionInputSource value ) {
    setActionInputValue( JNDI_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setDriver( null );
      setConnection( null );
      setUserId( null );
      setPassword( null );
    }
  }

  public IActionInput getJndi() {
    return getInput( JNDI_ELEMENT );
  }

  public void setOutputType( IActionInputSource value ) {
    setActionInputValue( OUTPUT_TYPE_ELEMENT, value );
  }

  public IActionInput getOutputType() {
    return getInput( OUTPUT_TYPE_ELEMENT );
  }

  public void setOutputReport( String publicOutputName ) {
    setOutput( REPORT_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.CONTENT_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      IActionOutput[] actionOutputs = getOutputs();
      for ( int i = 0; i < actionOutputs.length; i++ ) {
        if ( actionOutputs[i].getType().equals( ActionSequenceDocument.CONTENT_TYPE )
            && !actionOutputs[i].getName().equals( REPORT_OUTPUT_ELEMENT ) ) {
          actionOutputs[i].delete();
        }
      }
    }
  }

  public IActionOutput getOutputReport() {
    String privateOutputName = REPORT_OUTPUT_ELEMENT;
    if ( getOutput( privateOutputName ) == null ) {
      IActionOutput[] actionOutputs = getOutputs( ActionSequenceDocument.CONTENT_TYPE );
      if ( actionOutputs.length > 0 ) {
        privateOutputName = actionOutputs[0].getName();
      }
    }
    return getOutput( REPORT_OUTPUT_ELEMENT );
  }

  public IActionSequenceValidationError[] validate() {

    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( DRIVER_ELEMENT );
    if ( validationError == null ) {
      validationError = validateInput( CONNECTION_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing database connection input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Database connection input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Database connection input parameter is uninitialized.";
            break;
        }
        errors.add( validationError );
      }

      validationError = validateInput( USER_ID_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing database login input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Database login input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Database login input parameter is uninitialized.";
            break;
        }
        errors.add( validationError );
      }
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
      validationError = validateInput( JNDI_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing database connection input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Database connection input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Database connection input parameter is uninitialized.";
            break;
        }
        errors.add( validationError );
      }
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
      validationError.errorMsg = "Database driver input parameter references unknown variable.";
      errors.add( validationError );
    }

    validationError = validateInput( OUTPUT_TYPE_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing report format input parameter.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Report format input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Report format input parameter is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    validationError = validateResource( REPORT_DEFINITION_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing report definition input parameter.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Report definition input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Report definition input parameter is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public IActionResource setReportDefinition( URI uri, String mimeType ) {
    return setResourceUri( REPORT_DEFINITION_ELEMENT, uri, mimeType );
  }

  public IActionResource getReportDefinition() {
    return getResource( REPORT_DEFINITION_ELEMENT );
  }
}
