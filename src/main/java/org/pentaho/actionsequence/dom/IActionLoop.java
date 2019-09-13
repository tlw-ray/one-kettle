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

/**
 * A wrapper class for an action loop.
 * 
 * @author Angelo Rodriguez
 * 
 */
public interface IActionLoop extends IActionControlStatement {

  /**
   * Set the name of the parameter that is being looped on.
   * 
   * @param loopOn
   *          the parameter name. If null the loop parameter is removed.
   */
  void setLoopOn( String loopOn );

  /**
   * @return loopOn the name of the parameter that is being looped on
   */
  String getLoopOn();

  /**
   * @return whether a peek operation will be performed to loop on a result set
   */
  Boolean getLoopUsingPeek();

  /**
   * Determines if a peek operation will be performed to loop on a result set. Only if the result set is scrollable that
   * is we CAN go back to the first record should we reset to the first record. This is to resolve multiple levels of
   * looping on resultset.
   * 
   * @param usePeek
   *          determines if a peek operation will be performed to loop on a result set
   */
  void setLoopUsingPeek( Boolean usePeek );

}
