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

public class ActionSequenceOutputDestination implements IActionSequenceOutputDestination {

  Element destinationElement;
  IActionParameterMgr actionInputProvider;

  protected ActionSequenceOutputDestination( Element element, IActionParameterMgr actionInputProvider ) {
    destinationElement = element;
    this.actionInputProvider = actionInputProvider;
  }

  public void setDestination( String destination ) {
    destinationElement.setName( destination );
    ActionSequenceDocument.fireIoChanged( getActionSequenceOutput() );
  }

  public String getDestination() {
    return destinationElement.getName();
  }

  public void setName( String name ) {
    destinationElement.setText( name );
    ActionSequenceDocument.fireIoChanged( getActionSequenceOutput() );
  }

  public String getName() {
    return destinationElement.getText();
  }

  public IActionSequenceOutput getActionSequenceOutput() {
    ActionSequenceOutput actionSequenceOutput = null;
    if ( destinationElement != null ) {
      Element ancestorElement = destinationElement.getParent();
      if ( ancestorElement != null ) {
        ancestorElement = ancestorElement.getParent();
        if ( ancestorElement != null ) {
          actionSequenceOutput = new ActionSequenceOutput( ancestorElement, actionInputProvider );
        }
      }
    }
    return actionSequenceOutput;
  }

  public void delete() {
    Document doc = destinationElement.getDocument();
    if ( doc != null ) {
      IActionSequenceOutput actionSequenceOutput = getActionSequenceOutput();
      destinationElement.detach();
      ActionSequenceDocument.fireIoChanged( actionSequenceOutput );
    }
  }
}
