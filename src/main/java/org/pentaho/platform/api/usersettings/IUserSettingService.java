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

package org.pentaho.platform.api.usersettings;

import org.pentaho.platform.api.engine.IPentahoInitializer;
import org.pentaho.platform.api.usersettings.pojo.IUserSetting;

import java.util.List;

public interface IUserSettingService extends IPentahoInitializer {
  void deleteUserSettings();

  // if a global setting exists, the user setting has priority
  List<IUserSetting> getUserSettings();

  IUserSetting getUserSetting(String settingName, String defaultValue);

  void setUserSetting(String settingName, String settingValue);

  // the implementation should allow only an administrator to set global user settings
  List<IUserSetting> getGlobalUserSettings();

  IUserSetting getGlobalUserSetting(String settingName, String defaultValue);

  void setGlobalUserSetting(String settingName, String settingValue);
}
