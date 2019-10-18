/*!
 *
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
 *
 * Copyright (c) 2002-2018 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.platform.api.engine;

import java.util.List;

public interface IPentahoSystemStartupActions {

  void sessionStartup(final IPentahoSession session);

  void sessionStartup(final IPentahoSession session, IParameterProvider sessionParameters);

  void globalStartup();

  void globalStartup(final IPentahoSession session);

  /**
   * Registers server actions that will be invoked when a session is created. NOTE: it is completely up to the
   * {@link IPentahoSession} implementation whether to advise the system of it's creation via
   * {@link PentahoSystem#sessionStartup(IPentahoSession)}.
   * 
   * @param actions
   *          the server actions to execute on session startup
   */
  void setSessionStartupActions(List<ISessionStartupAction> actions);

  void clearGlobals();

  Object putInGlobalAttributesMap(final Object key, final Object value);

  Object removeFromGlobalAttributesMap(final Object key);

  IParameterProvider getGlobalParameters();

}
