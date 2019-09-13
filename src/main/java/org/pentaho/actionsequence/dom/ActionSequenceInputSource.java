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

import org.dom4j.Document;
import org.dom4j.Element;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

public class ActionSequenceInputSource implements IActionSequenceInputSource {

  Element sourceElement;
  IActionParameterMgr actionInputProvider;

  protected ActionSequenceInputSource( Element element, IActionParameterMgr actionInputProvider ) {
    sourceElement = element;
    this.actionInputProvider = actionInputProvider;
  }

  public void setOrigin( String origin ) {
    sourceElement.setName( origin );
    ActionSequenceDocument.fireIoChanged( getActionSequenceInput() );
  }

  public String getOrigin() {
    return sourceElement.getName();
  }

  public void setName( String name ) {
    sourceElement.setText( name );
    ActionSequenceDocument.fireIoChanged( getActionSequenceInput() );
  }

  public String getName() {
    return sourceElement.getText();
  }

  public IActionSequenceInput getActionSequenceInput() {
    ActionSequenceInput actionSequenceInput = null;
    if ( sourceElement != null ) {
      Element ancestorElement = sourceElement.getParent();
      if ( ancestorElement != null ) {
        ancestorElement = ancestorElement.getParent();
        if ( ancestorElement != null ) {
          actionSequenceInput = new ActionSequenceInput( ancestorElement, actionInputProvider );
        }
      }
    }
    return actionSequenceInput;
  }

  public void delete() {
    Document doc = sourceElement.getDocument();
    if ( doc != null ) {
      IActionSequenceInput actionSequenceInput = getActionSequenceInput();
      sourceElement.detach();
      ActionSequenceDocument.fireIoChanged( actionSequenceInput );
    }
  }
}
