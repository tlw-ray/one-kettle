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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;
import org.pentaho.commons.connection.IPentahoStreamSource;

public interface IActionParameterMgr {
  Object getInputValue( IActionInput actionInput );

  String replaceParameterReferences( String inputString );

  IPentahoStreamSource getDataSource( IActionResource actionResource ) throws FileNotFoundException;

  InputStream getInputStream( IActionResource actionResource ) throws FileNotFoundException;

  IPentahoStreamSource getDataSource( IActionInput actionInput );

  void setOutputValue( IActionOutput actionOutput, Object value );

  String getString( IActionResource actionResource ) throws IOException;
}
