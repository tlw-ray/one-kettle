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
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionSequenceResource;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class KettleJobAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "KettleComponent"; //$NON-NLS-1$

  public static final String JOB_FILE_ELEMENT = "job-file"; //$NON-NLS-1$
  public static final String REPOSITORY_DIRECTORY = "directory"; //$NON-NLS-1$
  public static final String REPOSITORY_JOB = "job"; //$NON-NLS-1$
  public static final String LOGGING_LEVEL = "logging-level"; //$NON-NLS-1$
  public static final String KETTLE_LOGGING_LEVEL = "kettle-logging-level"; //$NON-NLS-1$
  public static final String EXECUTION_STATUS_OUTPUT_ELEMENT = "kettle-execution-status"; //$NON-NLS-1$
  public static final String EXECUTION_LOG_OUTPUT_ELEMENT = "kettle-execution-log"; //$NON-NLS-1$
  public static final String OUTPUT_EXECUTION_LOG = "output-execution-log"; //$NON-NLS-1$
  public static final String OUTPUT_EXECUTION_STATUS = "output-execution-status"; //$NON-NLS-1$

  public static final String[] LOGGING_LEVEL_VALUES = new String[] {
    "minimal", "basic", "detail", "error", "rowlevel", "debug", "none"//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
  };

  public static final String[] EXPECTED_RESOURCES = new String[] { JOB_FILE_ELEMENT };

  protected static final String[] EXPECTED_INPUTS = new String[] { REPOSITORY_DIRECTORY, REPOSITORY_JOB, LOGGING_LEVEL,
    KETTLE_LOGGING_LEVEL };

  protected static final String[] EXPECTED_OUTPUTS = new String[] { EXECUTION_STATUS_OUTPUT_ELEMENT,
    EXECUTION_LOG_OUTPUT_ELEMENT };

  public KettleJobAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public KettleJobAction() {
    super( COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    addResource( JOB_FILE_ELEMENT );
  }

  public static boolean accepts( Element elem ) {
    boolean result = false;
    if ( ActionDefinition.accepts( elem ) && hasComponentName( elem, COMPONENT_NAME ) ) {

      result =
          ( elem.selectSingleNode( ActionSequenceDocument.ACTION_RESOURCES_NAME + "/" + JOB_FILE_ELEMENT ) != null )
              || ( elem.selectSingleNode( ActionSequenceDocument.ACTION_INPUTS_NAME + "/" + REPOSITORY_JOB ) != null )
              || ( elem.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + REPOSITORY_JOB ) != null );
    }
    return result;
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public void setJob( IActionInputSource value ) {
    setActionInputValue( REPOSITORY_JOB, value );
  }

  public IActionInput getJob() {
    return getInput( REPOSITORY_JOB );
  }

  public void setDirectory( IActionInputSource value ) {
    setActionInputValue( REPOSITORY_DIRECTORY, value );
  }

  public IActionInput getDirectory() {
    return getInput( REPOSITORY_DIRECTORY );
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

  public ActionSequenceValidationError[] validate() {

    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( REPOSITORY_DIRECTORY );
    if ( validationError == null ) {
      validationError = validateResource( JOB_FILE_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing job file location input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Job file location input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Job file location input parameter is uninitialized.";
            break;
        }
        errors.add( validationError );
      }
    } else if ( validationError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
      validationError = validateInput( JOB_FILE_ELEMENT );
      if ( validationError != null ) {
        switch ( validationError.errorCode ) {
          case ActionSequenceValidationError.INPUT_MISSING:
            validationError.errorMsg = "Missing job file location input parameter.";
            break;
          case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
            validationError.errorMsg = "Job file location input parameter references unknown variable.";
            break;
          case ActionSequenceValidationError.INPUT_UNINITIALIZED:
            validationError.errorMsg = "Job file location input parameter is uninitialized.";
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

  public IActionResource setJobFile( URI uri, String mimeType ) {
    // We never want to get rid of the kettle job resource element.
    // That's what's used to differentiate a kettle transformation action from a kettle job action.
    // If the uri is null we'll either delete the action sequence resource that is referenced
    // of map the resource to an invalid name.
    IActionResource actionResource = null;
    if ( uri == null ) {
      actionResource = getResource( JOB_FILE_ELEMENT );
      if ( actionResource != null ) {
        IActionSequenceDocument actionSequenceDocument = getDocument();
        IActionSequenceResource actionSequenceResource =
            actionSequenceDocument.getResource( actionResource.getPublicName() );
        IActionResource[] actionResources = actionSequenceDocument.getReferencesTo( actionSequenceResource );
        if ( ( actionResources.length == 1 ) && actionResources[0].equals( actionResource ) ) {
          actionSequenceResource.delete();
        } else {
          actionResource.setMapping( "NULL_MAPPING" );
        }
      }
      actionResource = null;
    } else {
      actionResource = setResourceUri( JOB_FILE_ELEMENT, uri, mimeType );
    }
    return actionResource;
  }

  public IActionResource getJobFile() {
    return getResource( JOB_FILE_ELEMENT );
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

}
