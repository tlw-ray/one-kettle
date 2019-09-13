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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

/**
 * Convenience class used to distinguish action sequence inputs from action sequence outputs.
 * 
 * @author Angelo Rodriguez
 * 
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ActionSequenceOutput extends AbstractIOElement implements IActionSequenceOutput {

  protected ActionSequenceOutput( Element outputElement, IActionParameterMgr actionInputProvider ) {
    super( outputElement, actionInputProvider );
  }

  public IActionSequenceOutputDestination[] getDestinations() {
    ArrayList outputDestinations = new ArrayList();
    List destinationElements = ioElement.selectNodes( ActionSequenceDocument.OUTPUTS_DESTINATIONS_NAME + "/*" ); //$NON-NLS-1$
    for ( Iterator iter = destinationElements.iterator(); iter.hasNext(); ) {
      outputDestinations.add( new ActionSequenceOutputDestination( (Element) iter.next(), actionInputProvider ) );
    }
    return (IActionSequenceOutputDestination[]) outputDestinations.toArray( new ActionSequenceOutputDestination[0] );
  }

  public IActionSequenceOutputDestination addDestination( String destination, String name ) {
    Element destinationParent =
        DocumentHelper.makeElement( ioElement, ActionSequenceDocument.OUTPUTS_DESTINATIONS_NAME );
    Element newDestinationElement = destinationParent.addElement( destination );
    newDestinationElement.setText( name );
    IActionSequenceOutputDestination actionSequenceOutputDestination =
        new ActionSequenceOutputDestination( newDestinationElement, actionInputProvider );
    ActionSequenceDocument.fireIoChanged( this );
    return actionSequenceOutputDestination;
  }

  public boolean isOutputParameter() {
    List<Attribute> attribs = ioElement.attributes();
    for ( Attribute attrib : attribs ) {
      if ( attrib.getName().equals( IS_OUTPUT_PARAM_ATTR ) ) {
        String outParamTxt = attrib.getValue();
        return Boolean.parseBoolean( outParamTxt );
      }
    }
    // default if not present
    return true;
  }

  public void setOutputParameter( boolean isOutputParameter ) {
    List<Attribute> attribs = ioElement.attributes();
    for ( Attribute attrib : attribs ) {
      if ( attrib.getName().equals( IS_OUTPUT_PARAM_ATTR ) ) {
        attrib.setValue( Boolean.toString( isOutputParameter ) );
        ActionSequenceDocument.fireIoChanged( this );
        return;
      }
    }
    // not found, create new
    ioElement.addAttribute( IS_OUTPUT_PARAM_ATTR, Boolean.toString( isOutputParameter ) );
    ActionSequenceDocument.fireIoChanged( this );
  }

}
