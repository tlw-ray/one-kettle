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
import org.pentaho.actionsequence.dom.IActionOutput;

public class SqlExecuteAction extends AbstractRelationalDbAction {

  public static final String COMPONENT_NAME = "SQLExecute"; //$NON-NLS-1$
  public static final String CONTINUE_ON_EXCEPTION = "continue_on_exception"; //$NON-NLS-1$
  public static final String FORCE_SINGLE_STATEMENT = "force_single_statement"; //$NON-NLS-1$
  public static final String MULTI_STATEMENT_SEPARATOR = "multi_statement_separator"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { DRIVER_ELEMENT, CONNECTION_ELEMENT, USER_ID_ELEMENT,
    PASSWORD_ELEMENT, JNDI_ELEMENT, QUERY_ELEMENT, CONTINUE_ON_EXCEPTION, FORCE_SINGLE_STATEMENT,
    MULTI_STATEMENT_SEPARATOR };

  public SqlExecuteAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public SqlExecuteAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setActionInputValue( JNDI_ELEMENT, new ActionInputConstant( "", this.actionParameterMgr ) );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    String expectedOutput = QUERY_RESULT_ELEMENT;
    String compDefVal = getComponentDefinitionValue( OUTPUT_NAME_ELEMENT );
    if ( compDefVal != null ) {
      expectedOutput = compDefVal;
    } else if ( getOutput( expectedOutput ) == null ) {
      IActionOutput[] actionOutputs = getOutputs( ActionSequenceDocument.RESULTSET_TYPE );
      if ( actionOutputs.length > 0 ) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[] { expectedOutput };
  }

  public void setContinueOnException( IActionInputSource value ) {
    setActionInputValue( CONTINUE_ON_EXCEPTION, value );
  }

  public IActionInput getContinueOnException() {
    return getInput( CONTINUE_ON_EXCEPTION );
  }

  public void setForceSingleStatement( IActionInputSource value ) {
    setActionInputValue( FORCE_SINGLE_STATEMENT, value );
  }

  public IActionInput getForceSingleStatement() {
    return getInput( FORCE_SINGLE_STATEMENT );
  }

  public void setMultiStatementSeparator( IActionInputSource value ) {
    setActionInputValue( MULTI_STATEMENT_SEPARATOR, value );
  }

  public IActionInput getMultiStatementSeparator() {
    return getInput( MULTI_STATEMENT_SEPARATOR );
  }
}
