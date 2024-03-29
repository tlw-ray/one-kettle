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
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.binding.Binding;

/**
 * @author NBaker
 * 
 */
public interface Document extends Element {
  XulComponent getRootElement();

  XulComponent createElement(String elementName) throws XulException;

  boolean isRegistered(String elementName);

  void setXulDomContainer(XulDomContainer container);

  void addOverlay(String src) throws XulException;

  void removeOverlay(String src) throws XulException;

  void loadFragment(String id, String src) throws XulException;

  @Deprecated
  void addBinding(Binding bind);

  void addInitializedBinding(Binding b);

  void invokeLater(Runnable runnable);

  void loadPerspective( String id );
}
