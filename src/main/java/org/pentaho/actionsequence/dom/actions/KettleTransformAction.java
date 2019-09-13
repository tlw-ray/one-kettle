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
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionSequenceResource;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

public class KettleTransformAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "KettleComponent"; //$NON-NLS-1$
  @Deprecated
  protected static final String TRANSFORMATION_STEP_ELEMENT_OLD = "importstep"; //$NON-NLS-1$
  public static final String TRANSFORMATION_STEP_ELEMENT = "monitor-step"; //$NON-NLS-1$
  public static final String TRANSFORMATION_FILE_ELEMENT = "transformation-file"; //$NON-NLS-1$
  @Deprecated
  public static final String TRANSFORMATION_OUTPUT_ELEMENT = "transformation-output"; //$NON-NLS-1$
  public static final String EXECUTION_STATUS_OUTPUT_ELEMENT = "kettle-execution-status"; //$NON-NLS-1$
  public static final String EXECUTION_LOG_OUTPUT_ELEMENT = "kettle-execution-log"; //$NON-NLS-1$
  public static final String TRANSFORM_SUCCESS_OUTPUT_ELEMENT = "transformation-output-rows"; //$NON-NLS-1$
  public static final String TRANSFORM_ERROR_OUTPUT_ELEMENT = "transformation-output-error-rows"; //$NON-NLS-1$
  public static final String TRANSFORM_SUCCESS_COUNT_OUTPUT_ELEMENT = "transformation-output-rows-count"; //$NON-NLS-1$
  public static final String TRANSFORM_ERROR_COUNT_OUTPUT_ELEMENT = "transformation-output-error-rows-count"; //$NON-NLS-1$
  public static final String REPOSITORY_DIRECTORY = "directory"; //$NON-NLS-1$
  public static final String REPOSITORY_TRANSFORMATION = "transformation"; //$NON-NLS-1$
  public static final String[] EXPECTED_RESOURCES = new String[] { TRANSFORMATION_FILE_ELEMENT };
  @Deprecated
  public static final String OUTPUT_RESULT_SET = "output-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_SUCCESS_RESULT_SET = "output-success-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_ERROR_RESULT_SET = "output-error-result-set"; //$NON-NLS-1$
  public static final String OUTPUT_SUCCESS_COUNT = "output-success-count"; //$NON-NLS-1$
  public static final String OUTPUT_ERROR_COUNT = "output-error-count"; //$NON-NLS-1$
  public static final String OUTPUT_EXECUTION_LOG = "output-execution-log"; //$NON-NLS-1$
  public static final String OUTPUT_EXECUTION_STATUS = "output-execution-status"; //$NON-NLS-1$
  public static final String NULL_MAPPING = "NULL_MAPPING"; //$NON-NLS-1$
  public static final String LOGGING_LEVEL = "logging-level"; //$NON-NLS-1$
  public static final String KETTLE_LOGGING_LEVEL = "kettle-logging-level"; //$NON-NLS-1$

  public static final String[] LOGGING_LEVEL_VALUES = new String[] {
    "minimal", "basic", "detail", "error", "rowlevel", "debug", "none" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
  };

  protected static final String[] EXPECTED_INPUTS = new String[] { TRANSFORMATION_STEP_ELEMENT_OLD,
    TRANSFORMATION_STEP_ELEMENT, REPOSITORY_DIRECTORY, REPOSITORY_TRANSFORMATION, LOGGING_LEVEL, KETTLE_LOGGING_LEVEL };

  protected static final String[] EXPECTED_OUTPUTS = new String[] { TRANSFORMATION_OUTPUT_ELEMENT,
    EXECUTION_STATUS_OUTPUT_ELEMENT, EXECUTION_LOG_OUTPUT_ELEMENT, TRANSFORM_SUCCESS_OUTPUT_ELEMENT,
    TRANSFORM_ERROR_OUTPUT_ELEMENT, TRANSFORM_SUCCESS_COUNT_OUTPUT_ELEMENT, TRANSFORM_ERROR_COUNT_OUTPUT_ELEMENT };

  public KettleTransformAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public KettleTransformAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    addResource( TRANSFORMATION_FILE_ELEMENT );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public static boolean accepts( Element element ) {
    boolean result = false;
    if ( ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME ) ) {
      result =
          ( element.selectSingleNode( ActionSequenceDocument.ACTION_RESOURCES_NAME + "/" + TRANSFORMATION_FILE_ELEMENT ) != null ) //$NON-NLS-1$
              || ( element.selectSingleNode( ActionSequenceDocument.ACTION_INPUTS_NAME
                  + "/" + REPOSITORY_TRANSFORMATION ) != null ) //$NON-NLS-1$
              || ( element.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME
                  + "/" + REPOSITORY_TRANSFORMATION ) != null ); //$NON-NLS-1$
    }
    return result;
  }

  public String[] getReservedOutputNames() {
    return EXPECTED_OUTPUTS;
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setTransformation( IActionInputSource value ) {
    setActionInputValue( REPOSITORY_TRANSFORMATION, value );
  }

  public IActionInput getTransformation() {
    return getInput( REPOSITORY_TRANSFORMATION );
  }

  public void setLoggingLevel( IActionInputSource value ) {
    setActionInputValue( LOGGING_LEVEL, value );
  }

  public IActionInput getLoggingLevel() {
    return getInput( LOGGING_LEVEL );
  }

  public void setKettleLoggingLevel( IActionInputSource value ) {
    setActionInputValue( KETTLE_LOGGING_LEVEL, value );
  }

  public IActionInput getKettleLoggingLevel() {
    return getInput( KETTLE_LOGGING_LEVEL );
  }

  public void setDirectory( IActionInputSource value ) {
    setActionInputValue( REPOSITORY_DIRECTORY, value );
  }

  public IActionInput getDirectory() {
    return getInput( REPOSITORY_DIRECTORY );
  }

  @Deprecated
  public void setImportstep( IActionInputSource value ) {
    setMonitorStep( value );
  }

  @Deprecated
  public IActionInput getImportstep() {
    return getMonitorStep();
  }

  public void setMonitorStep( IActionInputSource value ) {
    setActionInputValue( TRANSFORMATION_STEP_ELEMENT_OLD, ActionInputConstant.NULL_INPUT );
    setActionInputValue( TRANSFORMATION_STEP_ELEMENT, value );
  }

  public IActionInput getMonitorStep() {
    IActionInput input = getInput( TRANSFORMATION_STEP_ELEMENT_OLD );
    if ( input == ActionInput.NULL_INPUT ) {
      input = getInput( TRANSFORMATION_STEP_ELEMENT );
    }
    return input;
  }

  @Deprecated
  public void setOutputResultSet( String publicOutputName ) {
    setOutputSuccessResultSet( publicOutputName );
  }

  @Deprecated
  public IActionOutput getOutputResultSet() {
    return getOutputSuccessResultSet();
  }

  public void setOutputSuccessResultSet( String publicOutputName ) {
    setOutput( TRANSFORMATION_OUTPUT_ELEMENT, null, ActionSequenceDocument.RESULTSET_TYPE );
    setOutput( TRANSFORM_SUCCESS_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.RESULTSET_TYPE );
  }

  public IActionOutput getOutputSuccessResultSet() {
    IActionOutput output = getOutput( TRANSFORMATION_OUTPUT_ELEMENT );
    if ( output == null ) {
      output = getOutput( TRANSFORM_SUCCESS_OUTPUT_ELEMENT );
    }
    return output;
  }

  public void setOutputErrorResultSet( String publicOutputName ) {
    setOutput( TRANSFORM_ERROR_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.RESULTSET_TYPE );
  }

  public IActionOutput getOutputErrorResultSet() {
    return getOutput( TRANSFORM_ERROR_OUTPUT_ELEMENT );
  }

  public void setOutputSuccessCount( String publicOutputName ) {
    setOutput( TRANSFORM_SUCCESS_COUNT_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.INTEGER_TYPE );
  }

  public IActionOutput getOutputSuccessCount() {
    return getOutput( TRANSFORM_SUCCESS_COUNT_OUTPUT_ELEMENT );
  }

  public void setOutputErrorCount( String publicOutputName ) {
    setOutput( TRANSFORM_ERROR_COUNT_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.INTEGER_TYPE );
  }

  public IActionOutput getOutputErrorCount() {
    return getOutput( TRANSFORM_ERROR_COUNT_OUTPUT_ELEMENT );
  }

  public void setOutputExecutionLog( String publicOutputName ) {
    setOutput( EXECUTION_LOG_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.STRING_TYPE );
  }

  public IActionOutput getOutputExecutionLog() {
    return getOutput( EXECUTION_LOG_OUTPUT_ELEMENT );
  }

  public void setOutputExecutionStatus( String publicOutputName ) {
    setOutput( EXECUTION_STATUS_OUTPUT_ELEMENT, publicOutputName, ActionSequenceDocument.STRING_TYPE );
  }

  public IActionOutput getOutputExecutionStatus() {
    return getOutput( EXECUTION_STATUS_OUTPUT_ELEMENT );
  }

  public IActionSequenceValidationError[] validate() {
    ArrayList<IActionSequenceValidationError> errors = new ArrayList<IActionSequenceValidationError>();
    ActionSequenceValidationError validationError = validateInput( REPOSITORY_DIRECTORY );
    if ( validationError == null ) {
      validationError = validateResource( TRANSFORMATION_FILE_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing transformation file location input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Transformation file location input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Transformation file location input parameter is unitialized.";
            break;
        }
        errors.add( validationError );
      }
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
      validationError = validateInput( TRANSFORMATION_FILE_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing transformation file location input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Transformation file location input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Transformation file location input parameter is uninitialized.";
            break;
        }
        errors.add( validationError );
      }
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
      validationError.errorMsg = "Repository directory input parameter references unknown variable.";
      errors.add( validationError );
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
      validationError.errorMsg = "Repository directory input parameter is uninitialized.";
      errors.add( validationError );
    } else {
      errors.add( validationError );
    }
    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public IActionResource setTransformationFile( URI uri, String mimeType ) {
    IActionResource actionResource = getResource( TRANSFORMATION_FILE_ELEMENT );
    // We never want to get rid of the kettle transformation resource element.
    // That's what's used to differentiate a kettle transformation action from a kettle job action.
    // If the uri is null we'll either delete the action sequence resource that is referenced
    // or map the resource to an invalid name.
    if ( uri == null ) {
      actionResource = getResource( TRANSFORMATION_FILE_ELEMENT );
      if ( actionResource != null ) {
        IActionSequenceDocument actionSequenceDocument = getDocument();
        IActionSequenceResource actionSequenceResource =
            actionSequenceDocument.getResource( actionResource.getPublicName() );
        IActionResource[] actionResources = actionSequenceDocument.getReferencesTo( actionSequenceResource );
        if ( ( actionResources.length == 1 ) && actionResources[0].equals( actionResource ) ) {
          actionSequenceResource.delete();
        } else {
          actionResource.setMapping( NULL_MAPPING );
        }
      }
      actionResource = null;
    } else {
      actionResource = setResourceUri( TRANSFORMATION_FILE_ELEMENT, uri, mimeType );
    }
    return actionResource;
  }

  public IActionResource getTransformationFile() {
    IActionResource actionResource = getResource( TRANSFORMATION_FILE_ELEMENT );
    if ( ( actionResource != null ) && NULL_MAPPING.equals( actionResource.getMapping() ) ) {
      actionResource = null;
    }
    return actionResource;
  }

}
