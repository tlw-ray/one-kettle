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

import java.util.ArrayList;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;

/**
 * @deprecated As of 2.0
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class PrintMapValsAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.UtilityComponent"; //$NON-NLS-1$
  public static final String PRINT_MAP_VALS_COMMAND = "getmapvalues"; //$NON-NLS-1$
  public static final String TARGET_MAP_XPATH = "getmapvalues/property-map"; //$NON-NLS-1$
  public static final String PROPERTY_MAP_ELEMENT = "property-map"; //$NON-NLS-1$
  public static final String MAP_KEY_XPATH = "getmapvalues/arg"; //$NON-NLS-1$
  public static final String KEY_PARAM_PREFIX = "key"; //$NON-NLS-1$

  public PrintMapValsAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public PrintMapValsAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setComponentDefinition( PRINT_MAP_VALS_COMMAND, "" ); //$NON-NLS-1$
  }

  @SuppressWarnings( "deprecation" )
  public static boolean accepts( Element element ) {
    boolean accepts = false;
    if ( ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME ) ) {
      accepts =
          ( element.selectNodes( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + PRINT_MAP_VALS_COMMAND ).size() == 1 ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + FormatMsgAction.FORMAT_MSG_COMMAND ) == null ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + PrintParamAction.PRINT_PARAMS_COMMAND ) == null ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + CopyParamAction.COPY_PARAM_COMMAND ) == null ); //$NON-NLS-1$
    }
    return accepts;
  }

  public void setPropertyMap( IActionInputVariable value ) {
    String mapParamName = getComponentDefinitionValue( TARGET_MAP_XPATH );
    if ( value == null ) {
      setActionInputValue( mapParamName, (IActionInputSource) null );
      setComponentDefinition( TARGET_MAP_XPATH, "", false );
    } else {
      if ( !PROPERTY_MAP_ELEMENT.equals( mapParamName ) ) {
        setComponentDefinition( TARGET_MAP_XPATH, PROPERTY_MAP_ELEMENT, false );
      }
      setActionInputValue( PROPERTY_MAP_ELEMENT, value );
    }
  }

  public IActionInput getPropertyMap() {
    String mapParamName = getComponentDefinitionValue( TARGET_MAP_XPATH );
    IActionInput actionInput = IActionInput.NULL_INPUT;
    if ( ( mapParamName != null ) && ( mapParamName.trim().length() > 0 ) ) {
      actionInput = getInput( mapParamName );
    }
    return actionInput;
  }

  public IActionInput[] getKeys() {
    ArrayList keys = new ArrayList();
    Element[] elements = getComponentDefElements( MAP_KEY_XPATH );
    for ( int i = 0; i < elements.length; i++ ) {
      keys.add( new ActionInputConstant( elements[i].getText(), this.actionParameterMgr ) );
    }
    return (IActionInput[]) keys.toArray( new IActionInput[0] );
  }

  public void setKeys( ActionInputConstant[] keys ) {
    Object[] oldKeys = getKeys();
    for ( int i = 0; i < oldKeys.length; i++ ) {
      if ( oldKeys[i] instanceof ActionInput ) {
        ( (ActionInput) oldKeys[i] ).delete();
      }
    }
    IActionOutput[] oldOutputs = getOutputs();
    for ( int i = 0; i < oldOutputs.length; i++ ) {
      oldOutputs[i].delete();
    }
    setComponentDefinition( MAP_KEY_XPATH, new String[0] );

    ArrayList keyParamNames = new ArrayList();
    for ( int i = 0; i < keys.length; i++ ) {
      String keyParamName = keys[i].getStringValue();
      if ( keyParamName != null ) {
        keyParamNames.add( keyParamName );
        setOutput( keyParamName, keyParamName, ActionSequenceDocument.STRING_TYPE );
      }
    }

    if ( keyParamNames.size() > 0 ) {
      setComponentDefinition( MAP_KEY_XPATH, (String[]) keyParamNames.toArray( new String[0] ) );
    }
  }
}
