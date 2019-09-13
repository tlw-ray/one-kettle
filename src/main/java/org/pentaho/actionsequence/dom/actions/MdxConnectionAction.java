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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class MdxConnectionAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.MDXLookupRule"; //$NON-NLS-1$
  public static final String CONNECTION_ELEMENT = "connection"; //$NON-NLS-1$
  public static final String CONNECTION_PROPS = "connection-properties"; //$NON-NLS-1$
  public static final String USER_ID_ELEMENT = "user-id"; //$NON-NLS-1$
  public static final String PASSWORD_ELEMENT = "password"; //$NON-NLS-1$
  public static final String DRIVER_ELEMENT = "driver"; //$NON-NLS-1$
  public static final String JNDI_ELEMENT = "jndi"; //$NON-NLS-1$
  public static final String LOCATION_ELEMENT = "location"; //$NON-NLS-1$
  public static final String MDX_CONNECTION_ELEMENT = "mdx-connection-string"; //$NON-NLS-1$
  public static final String CATALOG_ELEMENT = "catalog"; //$NON-NLS-1$
  public static final String CATALOG_RESOURCE = "catalog-resource"; //$NON-NLS-1$
  public static final String ROLE_ELEMENT = "role"; //$NON-NLS-1$
  public static final String PREPARED_COMPONENT_ELEMENT = "prepared_component"; //$NON-NLS-1$
  public static final String DEFAULT_CONNECTION_NAME = "shared_olap_connection"; //$NON-NLS-1$
  public static final String DEFAULT_LOCATION = "mondrian"; //$NON-NLS-1$
  public static final String OUTPUT_CONNECTION = "output-connection"; //$NON-NLS-1$
  public static final String PROPERTY = "property"; //$NON-NLS-1$
  public static final String KEY_NODE = "key"; //$NON-NLS-1$
  public static final String VALUE_NODE = "value"; //$NON-NLS-1$
  public static final String EXTENDED_COLUMN_NAMES_ELEMENT = "extended_column_names"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { LOCATION_ELEMENT, CONNECTION_ELEMENT,
    USER_ID_ELEMENT, PASSWORD_ELEMENT, DRIVER_ELEMENT, MDX_CONNECTION_ELEMENT, ROLE_ELEMENT, JNDI_ELEMENT };

  protected static final String[] EXPECTED_RESOURCES = new String[] { CATALOG_ELEMENT };

  public MdxConnectionAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public MdxConnectionAction() {
    this( COMPONENT_NAME );
  }

  protected MdxConnectionAction( String componentName ) {
    super( componentName );
  }

  public void setExtendedColumnNames( IActionInputSource value ) {
    setActionInputValue( EXTENDED_COLUMN_NAMES_ELEMENT, value );
  }

  public IActionInput getExtendedColumnNames() {
    return getInput( EXTENDED_COLUMN_NAMES_ELEMENT );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    setJndi( new ActionInputConstant( "", this.actionParameterMgr ) ); //$NON-NLS-1$
    setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    setOutputConnection( DEFAULT_CONNECTION_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedOutputNames() {
    return new String[] { PREPARED_COMPONENT_ELEMENT };
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setLocation( IActionInputSource value ) {
    setActionInputValue( LOCATION_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnectionString( null );
    }
  }

  public IActionInput getLocation() {
    return getInput( LOCATION_ELEMENT );
  }

  public void setUserId( IActionInputSource value ) {
    setActionInputValue( USER_ID_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnectionString( null );
      setJndi( null );
      setConnectionProps( null );
      setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    }
  }

  public IActionInput getUserId() {
    return getInput( USER_ID_ELEMENT );
  }

  public void setPassword( IActionInputSource value ) {
    setActionInputValue( PASSWORD_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnectionString( null );
      setJndi( null );
      setConnectionProps( null );
      setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    }
  }

  public IActionInput getPassword() {
    return getInput( PASSWORD_ELEMENT );
  }

  public void setDriver( IActionInputSource value ) {
    setActionInputValue( DRIVER_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnectionString( null );
      setJndi( null );
      setConnectionProps( null );
      setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    }
  }

  public IActionInput getDriver() {
    return getInput( DRIVER_ELEMENT );
  }

  public void setMdxConnectionString( IActionInputSource value ) {
    setActionInputValue( MDX_CONNECTION_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setConnection( null );
      setConnectionProps( null );
      setLocation( null );
      setUserId( null );
      setPassword( null );
      setDriver( null );
      setRole( null );
    }
  }

  public IActionInput getMdxConnectionString() {
    return getInput( MDX_CONNECTION_ELEMENT );
  }

  public void setRole( IActionInputSource value ) {
    setActionInputValue( ROLE_ELEMENT, value );
  }

  public IActionInput getRole() {
    return getInput( ROLE_ELEMENT );
  }

  public void setConnection( IActionInputSource value ) {
    setActionInputValue( CONNECTION_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setJndi( null );
      setMdxConnectionString( null );
      setConnectionProps( null );
    }
  }

  public IActionInput getConnection() {
    return getInput( CONNECTION_ELEMENT );
  }

  public void setConnectionProps( IActionInputSource value ) {
    if ( value instanceof IActionInputVariable ) {
      throw new IllegalArgumentException();
    } else if ( value == null || ( (ActionInputConstant) value ).getValue() == null ) {
      clearComponentDef( CONNECTION_PROPS );
    } else {
      clearComponentDef( CONNECTION_PROPS );
      /*
       * Get the hash map of the key, value pairs from the properties and create an entry for each key inside element
       * CONNECTION_PROPS
       */
      Properties properties = (Properties) ( (ActionInputConstant) value ).getValue();
      Element compDefElement = (Element) actionDefElement.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME );
      Element connectionPropsElement = compDefElement.addElement( CONNECTION_PROPS );
      for ( Iterator iter = properties.entrySet().iterator(); iter.hasNext(); ) {
        Map.Entry mapEntry = (Map.Entry) iter.next();
        Element propElement = connectionPropsElement.addElement( PROPERTY );
        propElement.addElement( KEY_NODE ).setText( mapEntry.getKey().toString() );
        propElement.addElement( VALUE_NODE ).setText( mapEntry.getValue().toString() );
      }
      setDriver( null );
      setUserId( null );
      setPassword( null );
      setJndi( null );
      setMdxConnectionString( null );
      setConnection( null );
    }
  }

  public IActionInput getConnectionProps() {
    IActionInput actionInput = IActionInput.NULL_INPUT;
    Element connectionPropsElement = getComponentDefElement( CONNECTION_PROPS );
    if ( connectionPropsElement != null ) {
      Properties properties = new Properties();
      List propertyElements = connectionPropsElement.selectNodes( PROPERTY );
      for ( Iterator iter = propertyElements.iterator(); iter.hasNext(); ) {
        Element propElement = (Element) iter.next();
        Element keyElement = propElement.element( KEY_NODE );
        Element valueElement = propElement.element( VALUE_NODE );
        if ( keyElement != null ) {
          properties.put( keyElement.getText(), valueElement != null ? valueElement.getText() : null );
        }
      }
      actionInput = new ActionInputConstant( properties, this.actionParameterMgr );
    }
    return actionInput;
  }

  private void clearComponentDef( String key ) {
    Element element = getComponentDefElement( key );
    if ( element != null ) {
      element.detach();
    }
  }

  public void setJndi( IActionInputSource value ) {
    setActionInputValue( JNDI_ELEMENT, value );
    if ( ( value instanceof IActionInputVariable )
        || ( ( value != null ) && ( ( (ActionInputConstant) value ).getValue() != null ) ) ) {
      setMdxConnectionString( null );
      setConnection( null );
      setUserId( null );
      setPassword( null );
      setDriver( null );
      setConnectionProps( null );
      setLocation( new ActionInputConstant( DEFAULT_LOCATION, this.actionParameterMgr ) );
    }
  }

  public IActionInput getJndi() {
    return getInput( JNDI_ELEMENT );
  }

  public void setOutputConnection( String publicOutputName ) {
    setOutput( PREPARED_COMPONENT_ELEMENT, publicOutputName, ActionSequenceDocument.MDX_CONNECTION_TYPE );
  }

  public IActionOutput getOutputConnection() {
    return getOutput( PREPARED_COMPONENT_ELEMENT );
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
          if ( validationError != null ) {
            switch ( validationError.errorCode ) {
              case ActionSequenceValidationError.INPUT_MISSING:
                validationError.errorMsg = "Missing database connection input parameter.";
                break;
              case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
                validationError.errorMsg = "Database connection input parameter references unknown variable.";
                break;
              case ActionSequenceValidationError.INPUT_UNINITIALIZED:
                validationError.errorMsg = "Database connection input parameter in unitialized.";
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
      }
    }

    validationError = validateOutput( PREPARED_COMPONENT_ELEMENT );
    if ( validationError != null ) {
      if ( validationError.errorCode == ActionSequenceValidationError.OUTPUT_MISSING ) {
        validationError.errorMsg = "Missing output connection name.";
      }
      errors.add( validationError );
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public IActionResource setCatalogResource( URI uri, String mimeType ) {
    return setResourceUri( CATALOG_ELEMENT, uri, mimeType );
  }

  public IActionResource getCatalogResource() {
    return getResource( CATALOG_ELEMENT );
  }

  public void setCatalog( IActionInputSource value ) {
    setActionInputValue( CATALOG_ELEMENT, value );
  }

  public IActionInput getCatalog() {
    return getInput( CATALOG_ELEMENT );
  }

}
