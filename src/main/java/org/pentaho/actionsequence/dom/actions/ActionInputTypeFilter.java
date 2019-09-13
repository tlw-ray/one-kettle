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

import java.util.ArrayList;
import java.util.Arrays;

import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ActionInputTypeFilter implements IActionInputFilter {

  ArrayList types = new ArrayList();
  boolean includeConstants = false;

  public ActionInputTypeFilter( String[] types, boolean includeConstants ) {
    if ( types != null ) {
      this.types.addAll( Arrays.asList( types ) );
    }
    this.includeConstants = includeConstants;
  }

  public ActionInputTypeFilter( String[] types ) {
    this( types, false );
  }

  public ActionInputTypeFilter( String type ) {
    this( new String[] { type }, false );
  }

  public boolean accepts( IActionInput actionInput ) {
    boolean result = false;
    if ( includeConstants && ( actionInput instanceof ActionInputConstant ) ) {
      ActionInputConstant constant = (ActionInputConstant) actionInput;
      if ( types.contains( ActionSequenceDocument.STRING_TYPE ) ) {
        result = constant.getValue() instanceof String;
      } else if ( types.contains( ActionSequenceDocument.LONG_TYPE )
          || types.contains( ActionSequenceDocument.INTEGER_TYPE )
          || types.contains( ActionSequenceDocument.BIGDECIMAL_TYPE ) ) {
        if ( constant.getValue() instanceof String ) {
          try {
            Integer.parseInt( constant.getStringValue() );
            result = true;
          } catch ( Exception ex ) {
            result = false;
          }
        }
      }
    } else {
      result = ( actionInput instanceof ActionInput ) && ( types.contains( ( (ActionInput) actionInput ).getType() ) );
    }
    return result;
  }

}
