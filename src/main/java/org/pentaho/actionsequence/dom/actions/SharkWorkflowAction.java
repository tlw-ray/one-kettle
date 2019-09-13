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
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;

public class SharkWorkflowAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.SharkWorkflowComponent"; //$NON-NLS-1$
  public static final String PACKAGE_NAME_ELEMENT = "package-name"; //$NON-NLS-1$
  public static final String NEW_INSTANCE_ELEMENT = "new-instance"; //$NON-NLS-1$
  public static final String PROCESS_NAME_ELEMENT = "process-name"; //$NON-NLS-1$
  protected static final String[] EXPECTED_INPUTS = new String[] { PACKAGE_NAME_ELEMENT, NEW_INSTANCE_ELEMENT,
    PROCESS_NAME_ELEMENT };

  public SharkWorkflowAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public SharkWorkflowAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public void setPackageName( IActionInputSource value ) {
    setActionInputValue( PACKAGE_NAME_ELEMENT, value );
  }

  public IActionInput getPackageName() {
    return getInput( PACKAGE_NAME_ELEMENT );
  }

  public void setProcessName( IActionInputSource value ) {
    setActionInputValue( PROCESS_NAME_ELEMENT, value );
  }

  public IActionInput getProcessName() {
    return getInput( PROCESS_NAME_ELEMENT );
  }

  public void setNewInstance( IActionInputSource value ) {
    setActionInputValue( NEW_INSTANCE_ELEMENT, value );
  }

  public IActionInput getNewInstance() {
    return getInput( NEW_INSTANCE_ELEMENT );
  }
}
