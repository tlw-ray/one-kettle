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
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class MdxQueryAction extends MdxConnectionAction {

  public static final String COMPONENT_NAME = "org.pentaho.component.MDXLookupRule"; //$NON-NLS-1$
  public static final String CONNECTION_ELEMENT = "connection"; //$NON-NLS-1$
  public static final String USER_ID_ELEMENT = "user-id"; //$NON-NLS-1$
  public static final String PASSWORD_ELEMENT = "password"; //$NON-NLS-1$
  public static final String JNDI_ELEMENT = "jndi"; //$NON-NLS-1$
  public static final String LOCATION_ELEMENT = "location"; //$NON-NLS-1$
  public static final String MDX_CONNECTION_ELEMENT = "mdx-connection-string"; //$NON-NLS-1$
  public static final String QUERY_ELEMENT = "query"; //$NON-NLS-1$
  public static final String QUERY_RESULTS_ELEMENT = "query-results"; //$NON-NLS-1$
  public static final String CATALOG_ELEMENT = "catalog"; //$NON-NLS-1$
  public static final String ROLE_ELEMENT = "role"; //$NON-NLS-1$
  public static final String PREPARED_COMPONENT_ELEMENT = "prepared_component"; //$NON-NLS-1$
  public static final String DEFAULT_QUERY_RESULTS_NAME = "query_result"; //$NON-NLS-1$
  public static final String MDX_CONNECTION = "mdx-connection"; //$NON-NLS-1$
  public static final String OUTPUT_RESULT_SET = "output-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_PREPARED_STATEMENT = "output-prepared_statement"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { LOCATION_ELEMENT, CONNECTION_ELEMENT,
    USER_ID_ELEMENT, PASSWORD_ELEMENT, MDX_CONNECTION_ELEMENT, ROLE_ELEMENT, QUERY_ELEMENT, JNDI_ELEMENT };

  protected static final String[] EXPECTED_RESOURCES = new String[] { CATALOG_ELEMENT };

  public MdxQueryAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public MdxQueryAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    setJndi( new ActionInputConstant( "", this.actionParameterMgr ) ); //$NON-NLS-1$
    setQuery( new ActionInputConstant( "", this.actionParameterMgr ) ); //$NON-NLS-1$
    setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    setOutputResultSet( DEFAULT_QUERY_RESULTS_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME )
        && ( ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + QUERY_ELEMENT ) != null ) //$NON-NLS-1$
        || ( element.selectSingleNode( ActionSequenceDocument.ACTION_INPUTS_NAME + "/" + QUERY_ELEMENT ) != null ) ); //$NON-NLS-1$
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    String expectedOutput = QUERY_RESULTS_ELEMENT;
    if ( getOutput( expectedOutput ) == null ) {
      IActionOutput[] actionOutputs = getOutputs( ActionSequenceDocument.RESULTSET_TYPE );
      if ( actionOutputs.length > 0 ) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[] { expectedOutput };
  }

  public void setUserId( IActionInputSource value ) {
    super.setUserId( value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnection( null );
    }
  }

  public void setPassword( IActionInputSource value ) {
    super.setPassword( value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnection( null );
    }
  }

  public void setMdxConnectionString( IActionInputSource value ) {
    super.setMdxConnectionString( value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnection( null );
    }
  }

  public void setConnection( IActionInputSource value ) {
    super.setConnection( value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnection( null );
    }
  }

  public void setJndi( IActionInputSource value ) {
    super.setJndi( value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value instanceof ActionInputConstant ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnection( null );
    }
  }

  public void setQuery( IActionInputSource value ) {
    setActionInputValue( QUERY_ELEMENT, value );
  }

  public IActionInput getQuery() {
    return getInput( QUERY_ELEMENT );
  }

  public void setOutputResultSet( String publicOutputName ) {
    setOutput( QUERY_RESULTS_ELEMENT, publicOutputName, ActionSequenceDocument.RESULTSET_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputPreparedStatement( null );
    }
  }

  public IActionOutput getOutputResultSet() {
    IActionOutput actionOutput = getOutput( QUERY_RESULTS_ELEMENT );
    if ( actionOutput == null ) {
      IActionOutput[] allOutputs = getOutputs();
      if ( allOutputs.length > 0 ) {
        actionOutput = allOutputs[0];
      }
    }
    return actionOutput;
  }

  public void setOutputPreparedStatement( String publicOutputName ) {
    setOutput( PREPARED_COMPONENT_ELEMENT, publicOutputName, ActionSequenceDocument.MDX_QUERY_TYPE );
    if ( ( publicOutputName != null ) && ( publicOutputName.trim().length() > 0 ) ) {
      setOutputResultSet( null );
    }
  }

  public IActionOutput getOutputPreparedStatement() {
    return getOutput( PREPARED_COMPONENT_ELEMENT );
  }

  public void setMdxConnection( IActionInputVariable variable ) {
    setActionInputValue( PREPARED_COMPONENT_ELEMENT, variable );
    if ( variable != null ) {
      setMdxConnectionString( null );
      setConnection( null );
      setUserId( null );
      setPassword( null );
      setJndi( null );
      setLocation( null );
    }
  }

  public IActionInput getMdxConnection() {
    return getInput( PREPARED_COMPONENT_ELEMENT );
  }

  public ActionSequenceValidationError[] validate() {

    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( MDX_CONNECTION_ELEMENT );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        validationError.errorMsg = "Database connection input parameter references unknown variable.";
        errors.add( validationError );
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        validationError.errorMsg = "Database connection input parameter is uninitialized.";
        errors.add( validationError );
      } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
        validationError = validateResource( CATALOG_ELEMENT );
        if ( validationError != null ) {
          switch ( validationError.errorCode ) {
            case ActionSequenceValidationError.INPUT_MISSING:
              validationError.errorMsg = "Missing mondrian schema input parameter.";
              break;
            case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
              validationError.errorMsg = "Mondrian schema input parameter references unknown variable.";
              break;
            case ActionSequenceValidationError.INPUT_UNINITIALIZED:
              validationError.errorMsg = "Mondrian schema input parameter is uninitialized.";
              break;
          }
          errors.add( validationError );
        }

        validationError = validateInput( CONNECTION_ELEMENT );
        if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
          validationError = validateInput( JNDI_ELEMENT );
          if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
            validationError = validateInput( PREPARED_COMPONENT_ELEMENT );
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
            validationError.errorMsg = "Database connection input parameter references unknown variable.";
            errors.add( validationError );
          } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
            validationError.errorMsg = "Database connection input parameter is uninitialized.";
            errors.add( validationError );
          }
        } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
          validationError.errorMsg = "Database connection input parameter references unknown variable.";
          errors.add( validationError );
        } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
          validationError.errorMsg = "Database connection input parameter is uninitialized.";
          errors.add( validationError );
        }
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
      validationError = validateOutput( QUERY_RESULTS_ELEMENT );
      if ( validationError != null ) {
        validationError.errorMsg = "Missing query results output parameter.";
        errors.add( validationError );
      }
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }
}
