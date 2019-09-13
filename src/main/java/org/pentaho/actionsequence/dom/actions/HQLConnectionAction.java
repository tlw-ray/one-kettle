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

/*
 * Copyright 2006 - 2017 Hitachi Vantara.  All rights reserved. 
 * This software was developed by Hitachi Vantara and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */

import java.net.URI;
import java.util.ArrayList;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class HQLConnectionAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.HQLLookupRule"; //$NON-NLS-1$
  public static final String PREPARED_COMPONENT_ELEMENT = "prepared_component"; //$NON-NLS-1$
  public static final String DEFAULT_CONNECTION_NAME = "shared_connection"; //$NON-NLS-1$
  public static final String PROPERTY = "property"; //$NON-NLS-1$
  public static final String CLASSNAMES = "classNames"; //$NON-NLS-1$
  public static final String HIBERNATE_CONFIG = "hibernateConfig"; //$NON-NLS-1$
  public static final String HQL_CONNECTION_TYPE = "hql-connection"; //$NON-NLS-1$
  // Added by Marc and Ramaiz
  public static final String TIMEOUT = "timeout"; //$NON-NLS-1$
  public static final String MAXROWS = "max_rows"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { CLASSNAMES };

  protected static final String[] EXPECTED_RESOURCES = new String[] { HIBERNATE_CONFIG };

  public HQLConnectionAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public HQLConnectionAction() {
    this( COMPONENT_NAME );
  }

  protected HQLConnectionAction( String componentName ) {
    super( componentName );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setOutputConnectionName( DEFAULT_CONNECTION_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    return new String[] { PREPARED_COMPONENT_ELEMENT };
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setOutputConnectionName( String name ) {
    setOutput( PREPARED_COMPONENT_ELEMENT, name, HQL_CONNECTION_TYPE );
  }

  public String getOutputConnectionName() {
    return getPublicOutputName( PREPARED_COMPONENT_ELEMENT );
  }

  public IActionOutput getOutputConnectionParam() {
    return getOutput( PREPARED_COMPONENT_ELEMENT );
  }

  public IActionSequenceValidationError[] validate() {

    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( CLASSNAMES );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        validationError.errorMsg = "Database connection input parameter references unknown variable.";
        errors.add( validationError );
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        validationError.errorMsg = "Database connection input parameter is uninitialized.";
        errors.add( validationError );
      }
    }

    validationError = validateInput( HIBERNATE_CONFIG );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        validationError.errorMsg = "Database connection input parameter references unknown variable.";
        errors.add( validationError );
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        validationError.errorMsg = "Database connection input parameter is uninitialized.";
        errors.add( validationError );
      }
    }

    validationError = validateOutput( PREPARED_COMPONENT_ELEMENT );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.OUTPUT_MISSING ) {
        validationError.errorMsg = "Missing output connection name.";
      }
      errors.add( validationError );
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public void setClassNames( IActionInputSource value ) {
    setActionInputValue( CLASSNAMES, value );
  }

  public IActionInput getClassNames() {
    return getInput( CLASSNAMES );
  }

  public IActionResource setHibernateConfig( URI uri, String mimeType ) {
    return setResourceUri( HIBERNATE_CONFIG, uri, mimeType );
  }

  public IActionResource getHibernateConfigResource() {
    return getResource( HIBERNATE_CONFIG );
  }

  public void setQueryTimeout( IActionInputSource value ) {
    setActionInputValue( TIMEOUT, value );
  }

  public IActionInput getQueryTimeout() {
    return getInput( TIMEOUT );
  }

  public void setMaxRows( IActionInputSource value ) {
    setActionInputValue( MAXROWS, value );
  }

  public IActionInput getMaxRows() {
    return getInput( MAXROWS );
  }

}
