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

/**
 * Interface to be implement by objects that wrap action sequence elements
 * 
 * @author Angelo Rodriguez
 * 
 */
public interface IActionSequenceElement {

  /**
   * @return the action sequence element wrapped by this object
   */
  Element getElement();

  /**
   * Removes the element from the action sequence
   */
  void delete();

  /**
   * @return an action sequence document that wraps the dom document containing the element wrapped by this object.
   */
  IActionSequenceDocument getDocument();
}
