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

import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

/**
 * A wrapper class for an action sequence input or output element.
 * 
 * @author Angelo Rodriguez
 * 
 */
public interface IAbstractIOElement extends IActionSequenceElement {

  String TYPE_NAME = "type"; //$NON-NLS-1$

  String getName();

  /**
   * Sets the name of the action sequence input/output
   * 
   * @param ioName
   *          the input/output name
   */
  void setName( String ioName );

  /**
   * @return the type of input/output
   */
  String getType();

  /**
   * Sets the type of the IO type.
   * 
   * @param ioType
   *          the io type
   */
  void setType( String ioType );

  IActionParameterMgr getParameterMgr();
}
