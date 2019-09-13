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

import java.util.HashMap;

import javax.swing.table.TableModel;

/**
 * A wrapper class for an action definition input or output element.
 * 
 * @author Angelo Rodriguez
 * 
 */
@SuppressWarnings( { "rawtypes" } )
public interface IActionSequenceInput extends IActionInputVariable, IAbstractIOElement {

  int REQUEST_INPUT_SOURCE_ID = 1;
  int SESSION_INPUT_SOURCE_ID = 2;
  int RUNTIME_INPUT_SOURCE_ID = 3;
  int GLOBAL_INPUT_SOURCE_ID = 4;

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.designstudio.dom.ActionSequenceIO#setType(java.lang.String)
   */
  void setType( String ioType );

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  void setDefaultValue( String defValue );

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  void setDefaultValue( String[] defValue );

  /**
   * Sets the input default value.
   * 
   * @param paramMap
   *          the default value
   */
  void setDefaultValue( HashMap paramMap );

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  void setDefaultValue( TableModel defValue );

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   * @param usePropertyMapList
   *          indicates whether the property map list element or result set element should be used to save the default
   *          value.
   */
  void setDefaultValue( TableModel defValue, boolean usePropertyMapList );

  /**
   * @return the default value or null if none exists.
   */
  Object getDefaultValue();

  IActionSequenceInputSource[] getSources();

  IActionSequenceInputSource addSource( String origin, String name );

  IActionSequenceInputSource addSource( int index, String origin, String name );

  String getVariableName();

}
