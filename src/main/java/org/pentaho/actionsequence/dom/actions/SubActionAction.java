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

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;

public class SubActionAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.SubActionComponent"; //$NON-NLS-1$
  public static final String SOLUTION_ELEMENT = "solution"; //$NON-NLS-1$
  public static final String PATH_ELEMENT = "path"; //$NON-NLS-1$
  public static final String ACTION_ELEMENT = "action"; //$NON-NLS-1$
  public static final String PROXY_ELEMENT = "session-proxy"; //$NON-NLS-1$
  public static final String PROXY_REF_ELEMENT = "session-proxy-ref"; //$NON-NLS-1$
  protected static final String[] EXPECTED_INPUTS = new String[] { SOLUTION_ELEMENT, PATH_ELEMENT, ACTION_ELEMENT,
    PROXY_ELEMENT, PROXY_REF_ELEMENT };

  public SubActionAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public SubActionAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public void setSolution( IActionInputSource value ) {
    setActionInputValue( SOLUTION_ELEMENT, value );
  }

  public IActionInput getSolution() {
    return getInput( SOLUTION_ELEMENT );
  }

  public void setPath( IActionInputSource value ) {
    setActionInputValue( PATH_ELEMENT, value );
  }

  public IActionInput getPath() {
    return getInput( PATH_ELEMENT );
  }

  public void setAction( IActionInputSource value ) {
    setActionInputValue( ACTION_ELEMENT, value );
  }

  public IActionInput getAction() {
    return getInput( ACTION_ELEMENT );
  }

  public void setSessionProxy( IActionInputSource value ) {
    if ( ( value == null )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() == null ) ) ) {
      setActionInputValue( PROXY_REF_ELEMENT, (IActionInputSource) null );
      setActionInputValue( PROXY_ELEMENT, (IActionInputSource) null );
    } else {
      setActionInputValue( PROXY_REF_ELEMENT, value );
      setActionInputValue( PROXY_ELEMENT, new ActionInputConstant( PROXY_REF_ELEMENT, this.actionParameterMgr ) );
    }
  }

  public IActionInput getSessionProxy() {
    IActionInput actionInput = getInput( PROXY_ELEMENT );
    String stringValue = actionInput.getStringValue();
    if ( stringValue != null ) {
      actionInput = getInput( stringValue );
    }
    return actionInput;
  }
}
