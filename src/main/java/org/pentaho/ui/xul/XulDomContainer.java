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

package org.pentaho.ui.xul;

import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.dom.Document;
import org.pentaho.ui.xul.impl.XulEventHandler;

import java.util.List;
import java.util.Map;

/**
 * @author OEM
 * 
 */
public interface XulDomContainer {

  void addDocument(Document document);

  Document getDocumentRoot();

  Document getDocument(int idx);

  XulDomContainer loadFragment(String xulLocation) throws XulException;

  /**
   * @deprecated We are getting away from using xul-instantiated event handlers. It is prefered that the
   *             application set the event handler via {@link XulDomContainer#addEventHandler(XulEventHandler)}
   */
  void addEventHandler(String id, String eventClassName) throws XulException;

  XulDomContainer loadFragment(String xulLocation, Object bundle) throws XulException;

  void loadFragment(String id, String src) throws XulException;

  XulEventHandler getEventHandler(String key) throws XulException;

  /**
   * Registers an event handler with elements of this container. Attributes of command-type elements within a xul
   * file can refer to an event handler by name. See {@link XulEventHandler#getName()}. Adding an event handler by
   * a name that is already registered will completely mask out and replace the original event handler.
   * 
   * @param handler
   *          - a XulEventHandler
   */
  void addEventHandler(XulEventHandler handler);

  @Deprecated
  void addBinding(Binding binding);

  void addInitializedBinding(Binding b);

  void removeBinding(Binding binding);

  void initialize();

  boolean isInitialized();

  void close();

  boolean isClosed();

  XulLoader getXulLoader();

  void mergeContainer(XulDomContainer container);

  Map<String, XulEventHandler> getEventHandlers();

  /**
   * Execute the method passed, with any args as parameters. This invokation is used for plumbing event handlers to
   * the event methods.
   * 
   * @param method
   *          The method to execute
   * @param args
   *          Any parameters needed for the method.
   * 
   * @return the invoked method's return object
   */
  Object invoke(String method, Object[] args) throws XulException;

  /**
   * Accommodates those objects that require a parenting on construction. Set the root parent before parsing.
   * 
   * @param context
   *          root context
   */
  void setOuterContext(Object context);

  Object getOuterContext();

  @Deprecated
  Binding createBinding(XulEventSource source, String sourceAttr, String targetId, String targetAttr);

  @Deprecated
  Binding createBinding(String source, String sourceAttr, String targetId, String targetAttr);

  void loadOverlay(String src) throws XulException;

  void loadOverlay(String src, Object resourceBundle) throws XulException;

  void removeOverlay(String src) throws XulException;

  void invokeLater(Runnable runnable);

  boolean isRegistered(String widgetHandlerName);

  void setResourceBundles(List<Object> resourceBundles);

  List<Object> getResourceBundles();

  void loadPerspective( String id );

  void addPerspective( XulPerspective perspective );

  // TODO: create wrapper.
  void registerClassLoader( Object loader );

  void deRegisterClassLoader( Object loader );

  void setSettingsManager( XulSettingsManager settings );

  XulSettingsManager getSettingsManager();

}
