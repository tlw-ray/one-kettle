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

public class SimpleActionInputVariable implements IActionInputVariable {

  public String name;
  public String type;

  public SimpleActionInputVariable() {
  }

  public SimpleActionInputVariable( String name, String type ) {
    this.name = name;
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public String getVariableName() {
    return name;
  }

  public void setType( String type ) {
    this.type = type;
  }

  public void setVariableName( String name ) {
    this.name = name;
  }

  public Boolean getBooleanValue() {
    return null;
  }

  public boolean getBooleanValue( boolean defaultValue ) {
    return defaultValue;
  }

  public Integer getIntValue() {
    return null;
  }

  public int getIntValue( int defaultValue ) {
    return defaultValue;
  }

  public String getStringValue() {
    return null;
  }

  public String getStringValue( boolean replaceParamReferences, String defaultValue ) {
    return defaultValue;
  }

  public String getStringValue( boolean replaceParamReferences ) {
    return null;
  }

  public String getStringValue( String defaultValue ) {
    return defaultValue;
  }

  public Object getValue() {
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName( String ioName ) {
    name = ioName;

  }

  public void delete() {
  }

  public IActionSequenceDocument getDocument() {
    return null;
  }

  public Element getElement() {
    return null;
  }

  public IActionParameterMgr getParameterMgr() {
    return null;
  }

}
