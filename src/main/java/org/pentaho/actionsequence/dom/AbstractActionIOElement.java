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

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.pentaho.actionsequence.dom.actions.ActionDefinition;
import org.pentaho.actionsequence.dom.actions.ActionFactory;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

/**
 * A wrapper class for an action definition input or output element.
 * 
 * @author Angelo Rodriguez
 * 
 */
public abstract class AbstractActionIOElement extends AbstractIOElement implements IActionIOElement {

  public AbstractActionIOElement( Element ioElement, IActionParameterMgr actionInputProvider ) {
    super( ioElement, actionInputProvider );
  }

  /**
   * @return the name to which the input/output is mapped. Returns an empty string if the input/output is not mapped.
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
   * Sets the name to which the input/output is mapped.
   * 
   * @param mapping
   *          the mapped name. If null any existing mapping is removed.
   */
  public void setMapping( String mapping ) {
    if ( ( mapping == null ) || ( mapping.trim().length() == 0 ) || mapping.trim().equals( getName() ) ) {
      if ( ioElement.attribute( ActionSequenceDocument.MAPPING_NAME ) != null ) {
        ioElement.addAttribute( ActionSequenceDocument.MAPPING_NAME, null );
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      mapping = mapping.trim();
      if ( !mapping.equals( ioElement.attributeValue( ActionSequenceDocument.MAPPING_NAME ) ) ) {
        ioElement.addAttribute( ActionSequenceDocument.MAPPING_NAME, mapping );
        ActionSequenceDocument.fireIoChanged( this );
      }
    }
  }

  /**
   * @return the action definition to which this input/output belongs.
   */
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
  //
  // /**
  // * @return the mapped name if it exists, otherwise the input/output name is returned.
  // */
  // public String getPublicName() {
  // String mapping = getMapping();
  // return ((mapping != null) && (mapping.trim().length() > 0)) ? mapping.trim() : ioElement.getName();
  // }
}
