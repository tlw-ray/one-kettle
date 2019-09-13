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

public interface IActionInput {
  ActionInputConstant NULL_INPUT = new ActionInputConstant( (Object) null, null );

  Object getValue();

  String getStringValue();

  String getStringValue( String defaultValue );

  String getStringValue( boolean replaceParamReferences );

  String getStringValue( boolean replaceParamReferences, String defaultValue );

  Boolean getBooleanValue();

  boolean getBooleanValue( boolean defaultValue );

  Integer getIntValue();

  int getIntValue( int defaultValue );

  String getName();
}
