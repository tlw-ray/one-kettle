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
 * Copyright (c) 2002-2018 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.ui.xul.containers;

public interface XulDialog extends XulRoot {

  void setButtons(String buttons);

  String getButtons();

  void setButtonlabelcancel(String label);

  String getButtonlabelcancel();

  void setButtonlabelaccept(String label);

  String getButtonlabelaccept();

  void setButtonlabelextra1(String label);

  String getButtonlabelextra1();

  void setButtonlabelextra2(String label);

  String getButtonlabelextra2();

  void setOndialogaccept(String command);

  String getOndialogaccept();

  void setOndialogcancel(String command);

  String getOndialogcancel();

  void setOndialogextra1(String command);

  String getOndialogextra1();

  void setOndialogextra2(String command);

  String getOndialogextra2();

  void setButtonalign(String align);

  String getButtonalign();

  void show();

  void hide();

  boolean isHidden();

  void setVisible(boolean visible);

  Boolean getResizable();

  void setResizable(Boolean resizable);

  void setModal(Boolean modal);

  void setPack(boolean pack);

  boolean isPack();

  void center();
}
