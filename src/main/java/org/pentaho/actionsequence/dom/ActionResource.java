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

package org.pentaho.actionsequence.dom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.pentaho.actionsequence.dom.actions.ActionDefinition;
import org.pentaho.actionsequence.dom.actions.ActionFactory;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;
import org.pentaho.commons.connection.IPentahoStreamSource;

/**
 * A wrapper class for an action definition resource element.
 * 
 * @author Angelo Rodriguez
 * 
 */
public class ActionResource extends AbstractActionIOElement implements IActionResource {

  public ActionResource( Element resourceElement, IActionParameterMgr actionInputProvider ) {
    super( resourceElement, actionInputProvider );
    this.ioElement = resourceElement;
  }

  /**
   * @return the name of the resource
   */
  public String getName() {
    return ioElement.getName();
  }

  /**
   * Sets the name of the resource
   * 
   * @param resourceName
   *          the resource name
   */
  public void setName( String resourceName ) {
    if ( !ioElement.getName().equals( resourceName ) ) {
      ioElement.setName( resourceName );
      ActionSequenceDocument.fireResourceRenamed( this );
    }
  }

  /**
   * @return the name to which the resource is mapped. Returns an empty string if the input/output is not mapped.
   */
  public String getMapping() {
    String name = ""; //$NON-NLS-1$
    Attribute attr = ioElement.attribute( ActionSequenceDocument.MAPPING_NAME );
    if ( attr != null ) {
      name = attr.getValue().trim();
      if ( name.equals( getName() ) ) {
        name = ""; //$NON-NLS-1$
      }
    }
    return name;
  }

  /**
   * Sets the name to which the resource is mapped.
   * 
   * @param mapping
   *          the mapped name. If null any existing mapping is removed.
   */
  public void setMapping( String mapping ) {
    if ( ( mapping == null ) || ( mapping.trim().length() == 0 ) || mapping.trim().equals( getName() ) ) {
      if ( ioElement.attribute( ActionSequenceDocument.MAPPING_NAME ) != null ) {
        ioElement.addAttribute( ActionSequenceDocument.MAPPING_NAME, null );
        ActionSequenceDocument.fireResourceChanged( this );
      }
    } else {
      mapping = mapping.trim();
      if ( !mapping.equals( ioElement.attributeValue( ActionSequenceDocument.MAPPING_NAME ) ) ) {
        ioElement.addAttribute( ActionSequenceDocument.MAPPING_NAME, mapping );
        ActionSequenceDocument.fireResourceChanged( this );
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.designstudio.dom.IActionSequenceElement#delete()
   */
  public void delete() {
    Document doc = ioElement.getDocument();
    if ( doc != null ) {
      ioElement.detach();
      ActionSequenceDocument.fireResourceRemoved( new ActionSequenceDocument( doc, actionInputProvider ), this );
    }
  }

  public boolean equals( Object arg0 ) {
    boolean result = false;
    if ( arg0 != null ) {
      if ( arg0.getClass() == this.getClass() ) {
        ActionResource resource = (ActionResource) arg0;
        result = ( resource.ioElement != null ? resource.ioElement.equals( this.ioElement ) : ( resource == this ) );
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.designstudio.dom.IActionSequenceElement#getElement()
   */
  public Element getElement() {
    return ioElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.designstudio.dom.IActionSequenceElement#getDocument()
   */
  public IActionSequenceDocument getDocument() {
    ActionSequenceDocument doc = null;
    if ( ( ioElement != null ) && ( ioElement.getDocument() != null ) ) {
      doc = new ActionSequenceDocument( ioElement.getDocument(), actionInputProvider );
    }
    return doc;
  }

  /**
   * @return the mapped name if it exists, otherwise the resource name is returned.
   */
  public String getPublicName() {
    String mapping = ioElement.attributeValue( ActionSequenceDocument.MAPPING_NAME );
    return ( ( mapping != null ) && ( mapping.trim().length() > 0 ) ) ? mapping.trim() : ioElement.getName();
  }

  public IActionDefinition getActionDefinition() {
    ActionDefinition actionDefinition = null;
    if ( ioElement != null ) {
      Element ancestorElement = ioElement.getParent();
      if ( ancestorElement != null ) {
        ancestorElement = ancestorElement.getParent();
        if ( ( ancestorElement != null )
            && ancestorElement.getName().equals( ActionSequenceDocument.ACTION_DEFINITION_NAME ) ) {
          actionDefinition = ActionFactory.getActionDefinition( ancestorElement, actionInputProvider );
        }
      }
    }
    return actionDefinition;
  }

  public String getType() {
    return null;
  }

  public void setType( String ioType ) {
    throw new UnsupportedOperationException();
  }

  public URI getUri() {
    URI uri = null;
    IActionSequenceResource actionSequenceResource = getDocument().getResource( getPublicName() );
    if ( actionSequenceResource != null ) {
      uri = actionSequenceResource.getUri();
    }
    return uri;
  }

  public String getMimeType() {
    String mimeType = null;
    IActionSequenceResource actionSequenceResource = getDocument().getResource( getPublicName() );
    if ( actionSequenceResource != null ) {
      mimeType = actionSequenceResource.getMimeType();
    }
    return mimeType;
  }

  public void setURI( URI uri ) {
    String logicalName = getPublicName();
    IActionSequenceResource actionSequenceResource = getDocument().getResource( logicalName );
    if ( uri == null ) {
      IActionSequenceDocument document = getDocument();
      delete();
      if ( ( actionSequenceResource != null ) && ( document.getReferencesTo( actionSequenceResource ).length == 0 ) ) {
        document.setResourceUri( logicalName, null, null );
      }
    } else {
      if ( actionSequenceResource == null ) {
        getDocument().setResourceUri( logicalName, uri, "text/plain" );
      } else {
        String mimeType = actionSequenceResource.getMimeType();
        IActionResource[] actionResources = getDocument().getReferencesTo( actionSequenceResource );
        if ( ( actionResources.length == 1 ) && actionResources[0].equals( this ) ) {
          getDocument().setResourceUri( logicalName, uri, mimeType );
        } else {
          logicalName = createLogicalName( getName() );
          setMapping( logicalName );
          getDocument().setResourceUri( logicalName, uri, mimeType );
        }
      }
    }
  }

  public void setMimeType( String mimeType ) {
    String logicalName = getPublicName();
    IActionSequenceResource actionSequenceResource = getDocument().getResource( logicalName );
    if ( actionSequenceResource != null ) {
      IActionResource[] actionResources = getDocument().getReferencesTo( actionSequenceResource );
      if ( ( actionResources.length == 1 ) && actionResources[0].equals( this ) ) {
        getDocument().setResourceUri( logicalName, actionSequenceResource.getUri(), mimeType );
      } else {
        logicalName = createLogicalName( getName() );
        setMapping( logicalName );
        getDocument().setResourceUri( logicalName, actionSequenceResource.getUri(), mimeType );
      }
    }
  }

  private String createLogicalName( String resourceName ) {
    String logicalName;
    logicalName = resourceName;
    int index = 1;
    while ( getDocument().getResource( logicalName ) != null ) {
      logicalName = resourceName + index;
      index++;
    }
    return logicalName;
  }

  public IPentahoStreamSource getDataSource() throws FileNotFoundException {
    IPentahoStreamSource dataSource = null;
    if ( actionInputProvider != null ) {
      dataSource = actionInputProvider.getDataSource( this );
    }
    return dataSource;
  }

  public InputStream getInputStream() throws FileNotFoundException {
    InputStream inputStream = null;
    if ( actionInputProvider != null ) {
      inputStream = actionInputProvider.getInputStream( this );
    }
    return inputStream;
  }

  public String getStringValue() throws IOException {
    String stringValue = null;
    if ( actionInputProvider != null ) {
      stringValue = actionInputProvider.getString( this );
    }
    return stringValue;
  }

}
