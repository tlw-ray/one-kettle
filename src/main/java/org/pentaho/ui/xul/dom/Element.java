/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

/**
 * 
 */

package org.pentaho.ui.xul.dom;

import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomException;

import java.util.List;

/**
 * @author NBaker
 * 
 */
public interface Element {
  String getText();

  String getName();

  Document getDocument();

  XulComponent getParent();

  XulComponent getFirstChild();

  List<XulComponent> getChildNodes();

  void setNamespace(String prefix, String uri);

  Namespace getNamespace();

  XulComponent getElementById(String id);

  XulComponent getElementByXPath(String path);

  List<XulComponent> getElementsByTagName(String tagName);

  void addChild(Element element);

  void addChildAt(Element element, int idx);

  void removeChild(Element element);

  Object getElementObject();

  List<Attribute> getAttributes();

  void setAttributes(List<Attribute> attribute);

  void setAttribute(Attribute attribute);

  void setAttribute(String name, String value);

  String getAttributeValue(String attributeName);

  void replaceChild(XulComponent oldElement, XulComponent newElement) throws XulDomException;

}
