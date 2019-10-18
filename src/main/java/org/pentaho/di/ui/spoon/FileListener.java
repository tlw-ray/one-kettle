/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.ui.spoon;

import java.util.Locale;

import org.pentaho.di.core.EngineMetaInterface;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.w3c.dom.Node;

public interface FileListener {

  boolean open(Node transNode, String fname, boolean importfile) throws KettleMissingPluginsException;

  boolean save(EngineMetaInterface meta, String fname, boolean isExport);

  void syncMetaName(EngineMetaInterface meta, String name);

  boolean accepts(String fileName);

  boolean acceptsXml(String nodeName);

  String[] getSupportedExtensions();

  String[] getFileTypeDisplayNames(Locale locale);

  String getRootNodeName();
}
