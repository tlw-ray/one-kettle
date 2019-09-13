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

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

/**
 * Convenience class used to distinguish action inputs from action outputs.
 * 
 * @author Angelo Rodriguez
 * 
 */
public class ActionOutput extends AbstractActionIOElement implements IActionOutput {

  public ActionOutput( Element ioElement, IActionParameterMgr actionInputProvider ) {
    super( ioElement, actionInputProvider );
  }

  /**
   * @return the mapped name if it exists, otherwise the input/output name is returned.
   */
  public String getPublicName() {
    String mapping = getMapping();
    return ( ( mapping != null ) && ( mapping.trim().length() > 0 ) ) ? mapping.trim() : ioElement.getName();
  }

  public String getVariableName() {
    return getPublicName();
  }

  public void setValue( Object value ) {
    if ( actionInputProvider != null ) {
      actionInputProvider.setOutputValue( this, value );
    }
  }

}
