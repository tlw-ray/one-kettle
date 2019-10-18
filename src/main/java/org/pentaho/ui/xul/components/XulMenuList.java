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

package org.pentaho.ui.xul.components;

import org.pentaho.ui.xul.XulContainer;
import org.pentaho.ui.xul.XulDomException;

import java.util.Collection;

/**
 * @author aphillips
 * 
 */
public interface XulMenuList<T> extends XulContainer {

  void replaceAllItems(Collection<T> tees) throws XulDomException;

  String getSelectedItem();

  void setSelectedItem(T t);

  void setSelectedIndex(int idx);

  int getSelectedIndex();

  void setOncommand(String command);

  void setElements(Collection<T> elements);

  Collection<T> getElements();

  void setBinding(String binding);

  String getBinding();

  void setEditable(boolean editable);

  boolean getEditable();

  /**
   * Returns the user entered value in the case of an editable menulist
   * 
   * @return String user entered value
   */
  String getValue();

  /**
   * Sets teh value of the menulist if it's editable.
   * 
   * @param value
   */
  void setValue(String value);
}
