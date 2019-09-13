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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class EmailAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.EmailComponent"; //$NON-NLS-1$
  public static final String TO_ELEMENT = "to"; //$NON-NLS-1$
  public static final String FROM_ELEMENT = "from"; //$NON-NLS-1$
  public static final String CC_ELEMENT = "cc"; //$NON-NLS-1$
  public static final String BCC_ELEMENT = "bcc"; //$NON-NLS-1$
  public static final String SUBJECT_ELEMENT = "subject"; //$NON-NLS-1$
  public static final String PLAIN_MSG_ELEMENT = "message-plain"; //$NON-NLS-1$
  public static final String HTML_MSG_ELEMENT = "message-html"; //$NON-NLS-1$

  public class HTMLMsgInput extends ActionInput {
    HTMLMsgInput( Element element, IActionParameterMgr actionParameterMgr ) {
      super( element, actionParameterMgr );
    }

    @SuppressWarnings( "resource" )
    public Object getValue() {
      Object msg = super.getValue();
      if ( msg instanceof InputStream ) {
        InputStream is = (InputStream) msg;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int numRead = 0;
        try {
          while ( ( numRead = is.read( bytes ) ) != -1 ) {
            if ( numRead > 0 ) {
              baos.write( bytes, 0, numRead );
            }
          }
          msg = baos.toString();
        } catch ( Exception e ) {
          msg = "";
        }
      }
      return msg;
    }
  }

  public class HashMapInput extends ActionInput {
    String key;

    HashMapInput( Element element, IActionParameterMgr actionParameterMgr, String key ) {
      super( element, actionParameterMgr );
      this.key = key;
    }

    public Object getValue() {
      return ( (HashMap) super.getValue() ).get( key );
    }
  }

  protected static final String[] EXPECTED_INPUTS = new String[] { TO_ELEMENT, FROM_ELEMENT, CC_ELEMENT, BCC_ELEMENT,
    SUBJECT_ELEMENT, PLAIN_MSG_ELEMENT, HTML_MSG_ELEMENT };

  public EmailAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public EmailAction() {
    super( COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public void setTo( IActionInputSource value ) {
    setActionInputValue( TO_ELEMENT, value );
  }

  public IActionInput getTo() {
    IActionInput actionInput = getInput( TO_ELEMENT );
    // The to address could come from a hash map that is named after the "to" input. I believe
    // this is deprecated functionality.
    if ( actionInput.getValue() instanceof HashMap ) {
      HashMapInput hashMapInput =
          new HashMapInput( ( (ActionInput) actionInput ).getElement(),
              ( (ActionInput) actionInput ).getParameterMgr(), TO_ELEMENT );
      if ( hashMapInput.getValue() != null ) {
        actionInput = hashMapInput;
      }
    }
    // End deprecated functionality
    return actionInput;
  }

  public void setFrom( IActionInputSource value ) {
    setActionInputValue( FROM_ELEMENT, value );
  }

  public IActionInput getFrom() {
    return getInput( FROM_ELEMENT );
  }

  public void setCc( IActionInputSource value ) {
    setActionInputValue( CC_ELEMENT, value );
  }

  public IActionInput getCc() {
    return getInput( CC_ELEMENT );
  }

  public void setBcc( IActionInputSource value ) {
    setActionInputValue( BCC_ELEMENT, value );
  }

  public IActionInput getBcc() {
    return getInput( BCC_ELEMENT );
  }

  public void setMessageHtml( IActionInputSource value ) {
    setActionInputValue( HTML_MSG_ELEMENT, value );
  }

  public IActionInput getMessageHtml() {
    IActionInput actInput = getInput( HTML_MSG_ELEMENT );
    if ( actInput instanceof ActionInput ) {
      actInput =
          new HTMLMsgInput( ( (ActionInput) actInput ).getElement(), ( (ActionInput) actInput ).getParameterMgr() );
    }
    return actInput;
  }

  public void setMessagePlain( IActionInputSource value ) {
    setActionInputValue( PLAIN_MSG_ELEMENT, value );
  }

  public IActionInput getMessagePlain() {

    IActionInput actionInput;
    // The message could come from a hash map that is named after the "to" input. I believe
    // this is deprecated functionality.
    actionInput = getInput( TO_ELEMENT );
    if ( actionInput.getValue() instanceof HashMap ) {
      actionInput =
          new HashMapInput( ( (ActionInput) actionInput ).getElement(),
              ( (ActionInput) actionInput ).getParameterMgr(), EmailAction.PLAIN_MSG_ELEMENT );
      if ( actionInput.getValue() == null ) {
        actionInput = null;
      }
    } else {
      actionInput = null;
    }
    // End deprecated functionality

    if ( actionInput == null ) {
      actionInput = getInput( PLAIN_MSG_ELEMENT );
    }

    return actionInput;
  }

  public void setSubject( IActionInputSource subject ) {
    setActionInputValue( SUBJECT_ELEMENT, subject );
  }

  public IActionInput getSubject() {
    IActionInput actionInput;
    // The subject could come from a hash map that is named after the "to" input. I believe
    // this is deprecated functionality.
    actionInput = getInput( TO_ELEMENT );
    if ( actionInput.getValue() instanceof HashMap ) {
      actionInput =
          new HashMapInput( ( (ActionInput) actionInput ).getElement(),
              ( (ActionInput) actionInput ).getParameterMgr(), EmailAction.SUBJECT_ELEMENT );
      if ( actionInput.getValue() == null ) {
        actionInput = null;
      }
    } else {
      actionInput = null;
    }
    // End deprecated functionality

    if ( actionInput == null ) {
      actionInput = getInput( SUBJECT_ELEMENT );
    }

    return actionInput;
  }

  public IActionSequenceValidationError[] validate() {
    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( TO_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing input parameter for destination address.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Destination address is unavailable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Destination address is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    validationError = validateInput( SUBJECT_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing input parameter for subject.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Subject input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Subject input parameter is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    ActionSequenceValidationError htmlError = validateInput( HTML_MSG_ELEMENT );
    ActionSequenceValidationError plainError = validateInput( PLAIN_MSG_ELEMENT );
    if ( ( htmlError != null ) && ( plainError != null ) ) {
      if ( plainError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        plainError.errorMsg = "Email message input parameter is uninitialized.";
        errors.add( plainError );
      } else if ( htmlError.errorCode == ActionSequenceValidationError.INPUT_UNINITIALIZED ) {
        htmlError.errorMsg = "Email message input parameter is uninitialized.";
        errors.add( htmlError );
      } else if ( plainError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        plainError.errorMsg = "Email message input parameter references unknown variable.";
        errors.add( plainError );
      } else if ( htmlError.errorCode == ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR ) {
        htmlError.errorMsg = "Email message input parameter references unknown variable.";
        errors.add( htmlError );
      } else if ( plainError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
        plainError.errorMsg = "Missing input parameter for email message.";
        errors.add( plainError );
      } else if ( htmlError.errorCode == ActionSequenceValidationError.INPUT_MISSING ) {
        htmlError.errorMsg = "Missing input parameter for email message.";
        errors.add( htmlError );
      } else {
        errors.add( plainError );
      }
    }

    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }

  public EmailAttachment addAttachment( IActionInputVariable variable ) {
    EmailAttachment[] emailAttachments = getAttachments();
    for ( int i = 0; i < emailAttachments.length; i++ ) {
      if ( emailAttachments[i].isDeprecatedAttachmentStyle() ) {
        emailAttachments[i].convertToNewAttachmentStyle();
      }
    }
    return new EmailAttachment( this, variable );
  }

  public EmailAttachment addAttachment( String name, URI uri, String mimeType ) {
    EmailAttachment[] emailAttachments = getAttachments();
    for ( int i = 0; i < emailAttachments.length; i++ ) {
      if ( emailAttachments[i].isDeprecatedAttachmentStyle() ) {
        emailAttachments[i].convertToNewAttachmentStyle();
      }
    }
    return new EmailAttachment( this, name, uri, mimeType );
  }

  public EmailAttachment[] getAttachments() {
    Element[] elements = getComponentDefElements( EmailAttachment.ELEMENT_NAME );
    EmailAttachment[] emailAttachments = new EmailAttachment[elements.length];
    if ( emailAttachments.length != 0 ) {
      for ( int i = 0; i < elements.length; i++ ) {
        emailAttachments[i] = new EmailAttachment( elements[i], actionParameterMgr );
      }
    } else {
      // This else statement handles deprecated functionality. It is here to ensure that old
      // style email actions still work.
      if ( ( getInput( EmailAttachment.OLD_ATTACHMENT_ELEMENT ).getValue() != null )
          || ( getComponentDefElement( EmailAttachment.OLD_ATTACHMENT_ELEMENT ) != null ) ) {
        emailAttachments = new EmailAttachment[1];
        emailAttachments[0] = new EmailAttachment( this );
      }
    }
    return emailAttachments;
  }
}
