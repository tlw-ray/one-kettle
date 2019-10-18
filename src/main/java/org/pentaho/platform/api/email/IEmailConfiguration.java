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

package org.pentaho.platform.api.email;

public interface IEmailConfiguration {
  boolean isAuthenticate();

  void setAuthenticate(final boolean authenticate);

  boolean isDebug();

  void setDebug(final boolean debug);

  String getDefaultFrom();

  void setDefaultFrom(final String defaultFrom);

  String getFromName();

  void setFromName(String fromName);

  String getSmtpHost();

  void setSmtpHost(final String smtpHost);

  Integer getSmtpPort();

  void setSmtpPort(final Integer smtpPort);

  String getSmtpProtocol();

  void setSmtpProtocol(final String smtpProtocol);

  String getUserId();

  void setUserId(final String userId);

  String getPassword();

  void setPassword(final String password);

  boolean isUseSsl();

  void setUseSsl(final boolean useSsl);

  boolean isUseStartTls();

  void setUseStartTls(final boolean useStartTls);

  boolean isSmtpQuitWait();

  void setSmtpQuitWait(final boolean smtpQuitWait);
}
