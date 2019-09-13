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

public class JMSAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "com.pentaho.component.JMSComponent"; //$NON-NLS-1$
  public static final String SOLUTION_NAME_ELEMENT = "solution-name"; //$NON-NLS-1$
  public static final String ACTION_PATH_ELEMENT = "action-path"; //$NON-NLS-1$
  public static final String ACTION_NAME_ELEMENT = "action-name"; //$NON-NLS-1$
  public static final String QUEUE_NAME_ELEMENT = "jms-queue-name"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { SOLUTION_NAME_ELEMENT, ACTION_PATH_ELEMENT,
    ACTION_NAME_ELEMENT, QUEUE_NAME_ELEMENT };

  public JMSAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public JMSAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public void setSolutionName( IActionInputSource value ) {
    setActionInputValue( SOLUTION_NAME_ELEMENT, value );
  }

  public IActionInput getSolutionName() {
    return getInput( ACTION_NAME_ELEMENT );
  }

  public void setActionPath( IActionInputSource value ) {
    setActionInputValue( ACTION_PATH_ELEMENT, value );
  }

  public IActionInput getActionPath() {
    return getInput( ACTION_NAME_ELEMENT );
  }

  public void setActionName( IActionInputSource value ) {
    setActionInputValue( ACTION_NAME_ELEMENT, value );
  }

  public IActionInput getActionName() {
    return getInput( ACTION_NAME_ELEMENT );
  }

  public void setJmsQueueName( IActionInputSource value ) {
    setActionInputValue( QUEUE_NAME_ELEMENT, value );
  }

  public IActionInput getJmsQueueName() {
    return getInput( QUEUE_NAME_ELEMENT );
  }

}
