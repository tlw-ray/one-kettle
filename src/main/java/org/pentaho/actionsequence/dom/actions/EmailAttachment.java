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

import java.io.IOException;
import java.net.URI;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.actionsequence.dom.IActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionSequenceElement;
import org.pentaho.commons.connection.IPentahoStreamSource;

public class EmailAttachment implements IActionSequenceElement {

  Element attachmentElement;
  IActionParameterMgr actionInputProvider;
  EmailAction emailAction; // NOTE: This is only valid with deprecated attachment styles. Otherwise it's null.

  public static final String ATTACHMENT_NAME_ATTRIBUTE = "name-param"; //$NON-NLS-1$
  public static final String ATTACHMENT_CONTENT_ATTRIBUTE = "input-param"; //$NON-NLS-1$
  public static final String ATTACHMENT_RESOURCE_ATTRIBUTE = "resource-param"; //$NON-NLS-1$
  public static final String ATTACHMENT_NAME_PREFIX = "attach_name_";
  public static final String ATTACHMENT_RESOURCE_PREFIX = "attach_resource_";
  public static final String ELEMENT_NAME = "attachment-ref";
  public static final String OLD_ATTACHMENT_ELEMENT = "attach";
  public static final String OLD_ATTACHMENT_NAME_ELEMENT = "attach-name";

  // This constructor supports deprecated functionality.
  EmailAttachment( EmailAction emailAction ) {
    this.emailAction = emailAction;
    actionInputProvider = emailAction.actionParameterMgr;
  }

  EmailAttachment( EmailAction emailAction, IActionInputVariable attachment ) {
    attachmentElement =
        DocumentHelper.makeElement( emailAction.getElement(), ActionSequenceDocument.COMPONENT_DEF_NAME ).addElement(
            ELEMENT_NAME ); //$NON-NLS-1$
    actionInputProvider = emailAction.actionParameterMgr;
    setName( attachment.getVariableName() );
    setContentParam( attachment );
  }

  EmailAttachment( EmailAction emailAction, String name, URI uri, String mimeType ) {
    attachmentElement =
        DocumentHelper.makeElement( emailAction.getElement(), ActionSequenceDocument.COMPONENT_DEF_NAME ).addElement(
            ELEMENT_NAME ); //$NON-NLS-1$
    actionInputProvider = emailAction.actionParameterMgr;
    setName( name );
    setContentResource( uri, mimeType );
  }

  EmailAttachment( Element attachmentElement, IActionParameterMgr actionInputProvider ) {
    this.attachmentElement = attachmentElement;
    this.actionInputProvider = actionInputProvider;
  }

  public void delete() {
    EmailAction emailAction = getEmailAction();
    Attribute attribute = attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE );
    if ( attribute != null ) {
      emailAction.setActionInputValue( attribute.getValue().trim(), (IActionInputSource) null );
    }
    attribute = attachmentElement.attribute( ATTACHMENT_CONTENT_ATTRIBUTE );
    if ( attribute != null ) {
      emailAction.setActionInputValue( attribute.getValue().trim(), (IActionInputSource) null );
    }
    attribute = attachmentElement.attribute( ATTACHMENT_RESOURCE_ATTRIBUTE );
    if ( attribute != null ) {
      IActionResource actionResource = emailAction.getResource( attribute.getValue().trim() );
      actionResource.setURI( null );
    }
    attachmentElement.detach();
  }

  public IActionSequenceDocument getDocument() {
    ActionSequenceDocument doc = null;
    if ( ( attachmentElement != null ) && ( attachmentElement.getDocument() != null ) ) {
      doc = new ActionSequenceDocument( attachmentElement.getDocument(), actionInputProvider );
    }
    return doc;
  }

  public Element getElement() {
    return attachmentElement;
  }

  public EmailAction getEmailAction() {
    return emailAction != null ? emailAction : new EmailAction( attachmentElement.getParent().getParent(),
        actionInputProvider );
  }

  void convertToNewAttachmentStyle() {
    Element actionElement = emailAction.getElement();

    attachmentElement =
        DocumentHelper.makeElement( actionElement, ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + ELEMENT_NAME ); //$NON-NLS-1$

    // Convert the attachment content.
    Element oldAttachmentElement =
        (Element) actionElement.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/"
            + OLD_ATTACHMENT_ELEMENT );
    String attachmentParam = oldAttachmentElement.getText();
    oldAttachmentElement.detach();
    attachmentElement.addAttribute( ATTACHMENT_CONTENT_ATTRIBUTE, attachmentParam );

    // Convert the attachment name.
    String attachmentNameParam = null;
    String attachmentName = null;
    Element oldAttachmentNameElement =
        (Element) actionElement.selectSingleNode( ActionSequenceDocument.COMPONENT_DEF_NAME + "/"
            + OLD_ATTACHMENT_NAME_ELEMENT );
    if ( oldAttachmentNameElement != null ) {
      attachmentName = oldAttachmentNameElement.getText();
      oldAttachmentNameElement.detach();
    } else {
      attachmentNameParam = OLD_ATTACHMENT_NAME_ELEMENT;
    }
    if ( attachmentName != null ) {
      attachmentElement.addAttribute( ATTACHMENT_NAME_ATTRIBUTE, getUniqueNameParam() );
      emailAction.setActionInputValue( attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE ).getValue().trim(),
          new ActionInputConstant( attachmentName, this.actionInputProvider ) );
    } else {
      attachmentElement.addAttribute( ATTACHMENT_NAME_ATTRIBUTE, attachmentNameParam );
    }

    emailAction = null;
  }

  public void setName( String name ) {
    if ( isDeprecatedAttachmentStyle() ) {
      convertToNewAttachmentStyle();
    }
    if ( attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE ) == null ) {
      attachmentElement.addAttribute( ATTACHMENT_NAME_ATTRIBUTE, getUniqueNameParam() );
    }
    getEmailAction().setActionInputValue( attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE ).getValue().trim(),
        new ActionInputConstant( name.trim(), this.actionInputProvider ) );
  }

  public String getName() {
    String paramName =
        isDeprecatedAttachmentStyle() ? OLD_ATTACHMENT_NAME_ELEMENT : attachmentElement.attribute(
            ATTACHMENT_NAME_ATTRIBUTE ).getValue().trim();
    Object name = null;
    if ( paramName != null ) {
      name = getEmailAction().getInput( paramName ).getValue();
    }
    if ( ( name != null ) && ( actionInputProvider != null ) ) {
      name = actionInputProvider.replaceParameterReferences( name.toString() );
    }
    return name != null ? name.toString() : null;
  }

  public void setNameParam( IActionInputVariable variable ) {
    if ( isDeprecatedAttachmentStyle() ) {
      convertToNewAttachmentStyle();
    }
    if ( variable != null ) {
      if ( attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE ) == null ) {
        attachmentElement.addAttribute( ATTACHMENT_NAME_ATTRIBUTE, getUniqueNameParam() );
      }
      String paramName = attachmentElement.attribute( ATTACHMENT_NAME_ATTRIBUTE ).getValue().trim();
      getEmailAction().setActionInputValue( paramName, variable );
    }
  }

  public ActionInput getNameParam() {
    ActionInput actionInput = null;
    String paramName =
        isDeprecatedAttachmentStyle() ? OLD_ATTACHMENT_NAME_ELEMENT : attachmentElement.attribute(
            ATTACHMENT_NAME_ATTRIBUTE ).getValue().trim();
    if ( paramName != null ) {
      actionInput = getEmailAction().getInputParam( paramName );
    }
    return actionInput;
  }

  public void setContentParam( IActionInputVariable variable ) {
    if ( isDeprecatedAttachmentStyle() ) {
      convertToNewAttachmentStyle();
    }
    if ( variable != null ) {
      Attribute resourceAttribute = attachmentElement.attribute( ATTACHMENT_RESOURCE_ATTRIBUTE );
      if ( resourceAttribute != null ) {
        getEmailAction().setResourceUri( resourceAttribute.getValue(), null, null );
        attachmentElement.addAttribute( ATTACHMENT_CONTENT_ATTRIBUTE, null );
      }

      attachmentElement.addAttribute( ATTACHMENT_CONTENT_ATTRIBUTE, variable.getVariableName() );
      getEmailAction().setActionInputValue( variable.getVariableName(), variable );
    }
  }

  public ActionInput getContentParam() {
    ActionInput actionInput = null;
    if ( isDeprecatedAttachmentStyle() ) {
      Element oldAttachmentElement =
          (Element) getEmailAction().getElement().selectSingleNode(
              ActionSequenceDocument.COMPONENT_DEF_NAME + "/" + OLD_ATTACHMENT_ELEMENT );
      String attachmentParam = oldAttachmentElement.getText();
      actionInput = getEmailAction().getInputParam( attachmentParam );
    } else {
      Attribute attribute = attachmentElement.attribute( ATTACHMENT_CONTENT_ATTRIBUTE );
      if ( attribute != null ) {
        actionInput = getEmailAction().getInputParam( attribute.getValue().trim() );
      }
    }
    return actionInput;
  }

  public IActionResource getContentResource() {
    IActionResource actionResource = null;
    if ( !isDeprecatedAttachmentStyle() ) {
      Attribute attribute = attachmentElement.attribute( ATTACHMENT_RESOURCE_ATTRIBUTE );
      if ( attribute != null ) {
        actionResource = getEmailAction().getResource( attribute.getValue().trim() );
      }
    }
    return actionResource;
  }

  public IActionResource setContentResource( URI uri, String mimeType ) {
    if ( uri == null ) {
      throw new IllegalArgumentException();
    }
    IActionResource actionResource = null;
    if ( isDeprecatedAttachmentStyle() ) {
      convertToNewAttachmentStyle();
    }
    if ( uri != null ) {
      ActionInput actionInput = getContentParam();
      if ( actionInput != null ) {
        actionInput.delete();
      }
      attachmentElement.addAttribute( ATTACHMENT_CONTENT_ATTRIBUTE, null );

      Attribute resourceAttribute = attachmentElement.attribute( ATTACHMENT_RESOURCE_ATTRIBUTE );
      if ( resourceAttribute == null ) {
        attachmentElement.addAttribute( ATTACHMENT_RESOURCE_ATTRIBUTE, getUniqueResourceName() );
        resourceAttribute = attachmentElement.attribute( ATTACHMENT_RESOURCE_ATTRIBUTE );
      }
      actionResource = getEmailAction().setResourceUri( resourceAttribute.getValue(), uri, mimeType );
    }
    return actionResource;
  }

  private String getUniqueResourceName() {
    String name = null;
    boolean isUnique = false;
    for ( int i = 1; !isUnique; i++ ) {
      name = ATTACHMENT_RESOURCE_PREFIX + i;
      isUnique = ( getEmailAction().getResource( name ) == null );
    }
    return name;
  }

  private String getUniqueNameParam() {
    String name = null;
    boolean isUnique = false;
    EmailAction emailAction = getEmailAction();
    for ( int i = 1; !isUnique; i++ ) {
      name = ATTACHMENT_NAME_PREFIX + i;

      if ( ( emailAction.getInputParam( name ) == null ) && ( emailAction.getComponentDefElement( name ) == null ) ) {
        isUnique = true;
      }
    }
    return name;
  }

  boolean isDeprecatedAttachmentStyle() {
    return emailAction != null;
  }

  public IPentahoStreamSource getContent() throws IOException {
    IPentahoStreamSource dataSrc = null;
    IActionResource actionResource = null;
    Attribute attribute = getElement().attribute( EmailAttachment.ATTACHMENT_RESOURCE_ATTRIBUTE );
    if ( attribute != null ) {
      actionResource = getEmailAction().getResource( attribute.getValue().trim() );
    }
    if ( actionResource != null ) {
      dataSrc = actionResource.getDataSource();
    } else if ( actionInputProvider != null ) {
      IActionInput contentParam = getContentParam();
      if ( contentParam != null ) {
        dataSrc = actionInputProvider.getDataSource( contentParam );
      }
    }
    return dataSrc;
  }
}
