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

package org.pentaho.ui.xul;

public interface IMenuCustomization {

  enum CustomizationType {
    INSERT_BEFORE, INSERT_AFTER, FIRST_CHILD, LAST_CHILD, REPLACE, DELETE
  }

    enum ItemType {
    MENU_ITEM, SUBMENU
  }

    String getLabel();

  void setLabel(String label);

  String getAnchorId();

  void setAnchorId(String anchorId);

  String getId();

  void setId(String id);

  String getCommand();

  void setCommand(String command);

  CustomizationType getCustomizationType();

  void setCustomizationType(CustomizationType customizationType);

  ItemType getItemType();

  void setItemType(ItemType itemType);

}
