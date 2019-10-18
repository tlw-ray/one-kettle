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

package org.pentaho.ui.xul.components;

import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.binding.InlineBindingExpression;
import org.pentaho.ui.xul.util.ColumnType;

import java.util.List;

public interface XulTreeCol extends XulComponent {

  void setEditable(boolean edit);

  boolean isEditable();

  void setFixed(boolean fixed);

  boolean isFixed();

  void setHidden(boolean hide);

  boolean isHidden();

  void setLabel(String label);

  String getLabel();

  void setPrimary(boolean primo);

  boolean isPrimary();

  void setSortActive(boolean sort);

  boolean isSortActive();

  void setSortDirection(String dir);

  String getSortDirection();

  void setSrc(String srcUrl);

  String getSrc();

  void setType(String type);

  String getType();

  ColumnType getColumnType();

  void setWidth(int width);

  int getWidth();

  void autoSize();

  String getCustomeditor();

  void setCustomeditor(String customClass);

  void setBinding(String binding);

  String getBinding();

  void setChildrenbinding(String childProperty);

  String getChildrenbinding();

  String getCombobinding();

  void setCombobinding(String property);

  String getColumntypebinding();

  void setColumntypebinding(String property);

  List<InlineBindingExpression> getBindingExpressions();

  String getDisabledbinding();

  void setDisabledbinding(String property);

  String getImagebinding();

  void setImagebinding(String img);

  String getComparatorbinding();

  void setComparatorbinding(String comp);

  String getExpandedbinding();

  void setExpandedbinding(String bind);

  String getTooltipbinding();

  void setTooltipbinding(String bind);

  String getClassnameBinding();

  void setClassnameBinding(String classname);
}
