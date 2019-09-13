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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.dom4j.Element;
import org.pentaho.commons.connection.IPentahoStreamSource;

public interface IActionResource extends IActionIOElement {

  /**
   * The Resource is a solution file
   */
  int SOLUTION_FILE_RESOURCE = 1;

  /**
   * The resource is a URL
   */
  int URL_RESOURCE = 2;

  /**
   * The resource is an arbitrary file
   */
  int FILE_RESOURCE = 3;

  /**
   * The resource type is unknown
   */
  int UNKNOWN_RESOURCE = 4;

  /**
   * The resource type is an embedded string
   */
  int STRING = 5;

  /**
   * The resource type is embedded xml
   */
  int XML = 6;

  URI getUri();

  void setURI( URI uri );

  String getMimeType();

  void setMimeType( String mimeType );

  IPentahoStreamSource getDataSource() throws FileNotFoundException;

  InputStream getInputStream() throws FileNotFoundException;

  void delete();

  void setMapping( String publicName );

  void setName( String resourceName );

  void setType( String ioType );

  Element getElement();

  String getMapping();

  String getName();

  String getPublicName();

  String getType();

}
