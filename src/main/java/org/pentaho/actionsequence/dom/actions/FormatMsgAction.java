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
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;

/**
 * @deprecated As of 2.0
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class FormatMsgAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.UtilityComponent"; //$NON-NLS-1$
  public static final String STRING_FORMAT_ELEMENT = "format-string"; //$NON-NLS-1$
  public static final String OUTPUT_STRING_ELEMENT = "formatted-msg"; //$NON-NLS-1$
  public static final String RETURN_NAME_XPATH = "format/return"; //$NON-NLS-1$
  public static final String ARGUMENT_XPATH = "format/arg"; //$NON-NLS-1$
  public static final String FORMAT_MSG_COMMAND = "format"; //$NON-NLS-1$
  public static final String OUTPUT_STRING = "output-string"; //$NON-NLS-1$
  public static final String MSG_INPUT_PREFIX = "msgInput"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { STRING_FORMAT_ELEMENT };

  public FormatMsgAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public FormatMsgAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setComponentDefinition( FORMAT_MSG_COMMAND, "" ); //$NON-NLS-1$
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    String outputName = getComponentDefinitionValue( RETURN_NAME_XPATH );
    if ( ( outputName == null ) || ( outputName.trim().length() == 0 ) ) {
      outputName = OUTPUT_STRING_ELEMENT;
    }
    return new String[] { outputName };
  }

  @SuppressWarnings( "deprecation" )
  public static boolean accepts( Element element ) {
    boolean accepts = false;
    if ( ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME ) ) {
      accepts =
          ( element.selectNodes( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + FORMAT_MSG_COMMAND ).size() == 1 ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + CopyParamAction.COPY_PARAM_COMMAND ) == null ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + PrintParamAction.PRINT_PARAMS_COMMAND ) == null ) //$NON-NLS-1$
              && ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + PrintMapValsAction.PRINT_MAP_VALS_COMMAND ) == null ); //$NON-NLS-1$
    }
    return accepts;
  }

  public void setFormatString( ActionInputConstant value ) {
    String stringValue = ( value != null ? value.getStringValue() : null );
    if ( stringValue != null ) {
      stringValue = "\"" + stringValue + "\"";
    }

    setComponentDefinition(
        FORMAT_MSG_COMMAND + "/" + STRING_FORMAT_ELEMENT, value == null ? null : value.getStringValue() ); //$NON-NLS-1$
  }

  public ActionInputConstant getFormatString() {
    String formatString = getComponentDefinitionValue( FORMAT_MSG_COMMAND + "/" + STRING_FORMAT_ELEMENT );
    if ( formatString != null ) {
      formatString = formatString.trim();
      if ( formatString.startsWith( "\"" ) && formatString.endsWith( "\"" ) ) {
        if ( formatString.length() < 3 ) {
          formatString = ""; //$NON-NLS-1$
        }
        formatString = formatString.substring( 1, formatString.length() - 1 );
      }
    }
    return formatString == null ? IActionInput.NULL_INPUT : new ActionInputConstant( formatString,
        this.actionParameterMgr );
  }

  public void setOutputString( String publicOutputName ) {
    String privateName = getComponentDefinitionValue( RETURN_NAME_XPATH );
    if ( ( privateName == null ) || ( privateName.trim().length() == 0 ) ) {
      privateName = OUTPUT_STRING_ELEMENT;
    }
    IActionOutput actionOutput = setOutput( privateName, publicOutputName, ActionSequenceDocument.STRING_TYPE );
    if ( actionOutput == null ) {
      setComponentDefinition( RETURN_NAME_XPATH, (String) null );
    } else {
      setComponentDefinition( RETURN_NAME_XPATH, privateName );
    }
  }

  public IActionOutput getOutputString() {
    String privateName = getComponentDefinitionValue( RETURN_NAME_XPATH );
    if ( ( privateName == null ) || ( privateName.trim().length() == 0 ) ) {
      privateName = OUTPUT_STRING_ELEMENT;
    }
    return getOutput( privateName );
  }

  public ActionSequenceValidationError[] validate() {
    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = null;
    if ( getFormatString() == IActionInput.NULL_INPUT ) { //$NON-NLS-1$
      validationError = new ActionSequenceValidationError();
      validationError.actionDefinition = this;
      validationError.errorCode = ActionSequenceValidationError.INPUT_MISSING;
      validationError.errorMsg = "Missing format string.";
      validationError.parameterName = STRING_FORMAT_ELEMENT;
      errors.add( validationError );
    }

    String privateName = getComponentDefinitionValue( RETURN_NAME_XPATH );
    if ( ( privateName == null ) || ( privateName.trim().length() == 0 ) ) {
      privateName = OUTPUT_STRING_ELEMENT;
    }

    validationError = validateOutput( privateName );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.OUTPUT_MISSING ) {
        validationError.errorMsg = "Missing formatted message output parameter.";
      }
      errors.add( validationError );
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public IActionInput[] getMsgInputs() {
    ArrayList msgInputs = new ArrayList();
    Element[] elements = getComponentDefElements( ARGUMENT_XPATH );
    for ( int i = 0; i < elements.length; i++ ) {
      String msgInputParamName = elements[i].getText();
      IActionInput msgInput = getInput( msgInputParamName );
      if ( msgInput != IActionInput.NULL_INPUT ) {
        msgInputs.add( msgInput );
      }
    }
    return (IActionInput[]) msgInputs.toArray( new IActionInput[0] );
  }

  public void addMsgInput( IActionInputSource inputSource ) {
    IActionInput[] oldInputs = getMsgInputs();
    for ( int i = 0; i < oldInputs.length; i++ ) {
      if ( oldInputs[i] instanceof ActionInput ) {
        ( (ActionInput) oldInputs[i] ).delete();
      }
    }
    setComponentDefinition( ARGUMENT_XPATH, new String[0] );

    ArrayList msgInputParamNames = new ArrayList();
    for ( int i = 0; i < oldInputs.length; i++ ) {
      if ( ( oldInputs[i] instanceof ActionInputConstant ) && ( oldInputs[i].getValue() != null ) ) {
        String msgInputParamName = getUniqueNameParam();
        msgInputParamNames.add( msgInputParamName );
        setActionInputValue( msgInputParamName, (ActionInputConstant) oldInputs[i] );
      } else if ( oldInputs[i] instanceof ActionInput ) {
        ActionInput actionInput = (ActionInput) oldInputs[i];
        msgInputParamNames.add( actionInput.getName() );
        setActionInputValue( actionInput.getName(), actionInput );
      }
    }

    if ( inputSource instanceof IActionInputVariable ) {
      IActionInputVariable actionVariable = (IActionInputVariable) inputSource;
      msgInputParamNames.add( actionVariable.getVariableName() );
      setActionInputValue( actionVariable.getVariableName(), actionVariable );
    } else if ( ( inputSource instanceof ActionInputConstant )
        && ( ( (ActionInputConstant) inputSource ).getValue() != null ) ) {
      String msgInputParamName = getUniqueNameParam();
      msgInputParamNames.add( msgInputParamName );
      setActionInputValue( msgInputParamName, (ActionInputConstant) inputSource );
    }

    if ( msgInputParamNames.size() > 0 ) {
      setComponentDefinition( ARGUMENT_XPATH, (String[]) msgInputParamNames.toArray( new String[0] ) );
    }
  }

  public void setMsgInputs( IActionInput[] values ) {
    Object[] oldInputs = getMsgInputs();
    for ( int i = 0; i < oldInputs.length; i++ ) {
      if ( oldInputs[i] instanceof ActionInput ) {
        ( (ActionInput) oldInputs[i] ).delete();
      }
    }
    setComponentDefinition( ARGUMENT_XPATH, new String[0] );

    ArrayList msgInputParamNames = new ArrayList();
    for ( int i = 0; i < values.length; i++ ) {
      if ( values[i] instanceof IActionInputVariable ) {
        IActionInputVariable actionVariable = (IActionInputVariable) values[i];
        msgInputParamNames.add( actionVariable.getVariableName() );
        setActionInputValue( actionVariable.getVariableName(), actionVariable );
      } else if ( ( values[i] instanceof ActionInputConstant ) && ( values[i].getValue() != null ) ) {
        String msgInputParamName = getUniqueNameParam();
        msgInputParamNames.add( msgInputParamName );
        setActionInputValue( msgInputParamName, (ActionInputConstant) values[i] );
      } else if ( values[i] instanceof ActionInput ) {
        ActionInput actionInput = (ActionInput) values[i];
        msgInputParamNames.add( actionInput.getName() );
        setActionInputValue( actionInput.getName(), actionInput );
      }
    }
    if ( msgInputParamNames.size() > 0 ) {
      setComponentDefinition( ARGUMENT_XPATH, (String[]) msgInputParamNames.toArray( new String[0] ) );
    }
  }

  private String getUniqueNameParam() {
    String name = null;
    boolean isUnique = false;
    for ( int i = 1; !isUnique; i++ ) {
      name = MSG_INPUT_PREFIX + i;
      isUnique = ( getInputParam( name ) == null ) && ( getComponentDefElement( name ) == null );
    }
    return name;
  }
}
