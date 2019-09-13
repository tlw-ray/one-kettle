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
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;

public class StartScheduledJobAction extends AbstractJobSchedulerAction {

  public static final String START_SCHED_JOB_CMND = "startJob"; //$NON-NLS-1$

  public static final String SOLUTION_ELEMENT = "solution"; //$NON-NLS-1$

  public static final String PATH_ELEMENT = "path"; //$NON-NLS-1$

  public static final String ACTION_ELEMENT = "action"; //$NON-NLS-1$

  public static final String TRIGGER_TYPE_ELEMENT = "triggerType"; //$NON-NLS-1$

  public static final String TRIGGER_NAME_ELEMENT = "triggerName"; //$NON-NLS-1$

  public static final String REPEAT_INTERVAL_ELEMENT = "repeatInterval"; //$NON-NLS-1$

  public static final String REPEAT_COUNT_ELEMENT = "repeatCount"; //$NON-NLS-1$

  public static final String CRON_STRING_ELEMENT = "cronString"; //$NON-NLS-1$

  public static final String CRON_TRIGGER = "cron"; //$NON-NLS-1$

  public static final String SIMPLE_TRIGGER = "simple"; //$NON-NLS-1$

  public static final String REPEAT_INTERVAL = "repeat-interval"; //$NON-NLS-1$

  public static final String REPEAT_COUNT = "repeat-count"; //$NON-NLS-1$

  public static final String CRON_STRING = "cron-string"; //$NON-NLS-1$

  public static final String TRIGGER_NAME = "trigger-name"; //$NON-NLS-1$

  public static final String DEFAULT_STR = "default"; //$NON-NLS-1$

  public static final String DEFAULT_CRON_EXP_STR = "0 0 12 * * ?"; // Everyday at Noon //$NON-NLS-1$

  static final String[] EXPECTED_INPUTS = new String[] { JOB_ACTION_ELEMENT, SOLUTION_ELEMENT, PATH_ELEMENT,
    ACTION_ELEMENT, TRIGGER_TYPE_ELEMENT, TRIGGER_NAME_ELEMENT, REPEAT_INTERVAL_ELEMENT, REPEAT_COUNT_ELEMENT,
    JOB_NAME_ELEMENT, CRON_STRING_ELEMENT };

  public StartScheduledJobAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public StartScheduledJobAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setComponentDefinition( JOB_ACTION_ELEMENT, START_SCHED_JOB_CMND );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public static boolean accepts( Element element ) {
    boolean result = false;
    if ( AbstractJobSchedulerAction.accepts( element ) ) {
      element =
          (Element) element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + JOB_ACTION_ELEMENT ); //$NON-NLS-1$
      result = ( element != null ) && element.getText().equals( START_SCHED_JOB_CMND );
    }
    return result;
  }

  public void setCronString( IActionInputSource value ) {
    setActionInputValue( CRON_STRING_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setTriggerType( new ActionInputConstant( CRON_TRIGGER, this.actionParameterMgr ) );
      setRepeatCount( null );
      setRepeatInterval( null );
    } else if ( CRON_TRIGGER.equals( getTriggerType().getStringValue() ) ) {
      setTriggerType( null );
    }
  }

  public IActionInput getCronString() {
    return getInput( CRON_STRING_ELEMENT );
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

  public void setTriggerName( IActionInputSource value ) {
    setActionInputValue( TRIGGER_NAME_ELEMENT, value );
  }

  public IActionInput getTriggerName() {
    return getInput( TRIGGER_NAME_ELEMENT );
  }

  public void setRepeatCount( IActionInputSource value ) {
    setActionInputValue( REPEAT_COUNT_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setTriggerType( new ActionInputConstant( SIMPLE_TRIGGER, this.actionParameterMgr ) );
      setCronString( null );
    } else if ( SIMPLE_TRIGGER.equals( getTriggerType().getStringValue() ) ) {
      setTriggerType( null );
    }
  }

  public IActionInput getRepeatCount() {
    return getInput( REPEAT_COUNT_ELEMENT );
  }

  public void setRepeatInterval( IActionInputSource value ) {
    setActionInputValue( REPEAT_INTERVAL_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setTriggerType( new ActionInputConstant( SIMPLE_TRIGGER, this.actionParameterMgr ) );
      setCronString( null );
    } else if ( SIMPLE_TRIGGER.equals( getTriggerType().getStringValue() ) ) {
      setTriggerType( null );
    }
  }

  public IActionInput getRepeatInterval() {
    return getInput( REPEAT_INTERVAL_ELEMENT );
  }

  public void setTriggerType( IActionInputSource triggerType ) {
    setActionInputValue( TRIGGER_TYPE_ELEMENT, triggerType );
  }

  public IActionInput getTriggerType() {
    return getInput( TRIGGER_TYPE_ELEMENT );
  }
}
