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

package org.pentaho.platform.api.action;

import org.apache.commons.logging.Log;

/**
 * The interface for an Action that wants to be provided with a logger.
 * 
 * @see IAction
 * @author aphillips
 * @since 3.6
 */
public interface ILoggingAction extends IAction {

  /**
   * Sets an apache commons logger for the Action component to use
   * 
   * @param log
   *          the commons logging log that the Action can write to
   */
  void setLogger(Log log);

}
