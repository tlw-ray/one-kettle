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

public interface XulFileDialog extends XulComponent {
  enum RETURN_CODE {
    OK, CANCEL, ERROR
  }

  enum SEL_TYPE {
    SINGLE, MULTIPLE
  }

  enum VIEW_TYPE {
    FILES_DIRECTORIES, DIRECTORIES
  }

  RETURN_CODE showOpenDialog();

  RETURN_CODE showOpenDialog(Object f);

  RETURN_CODE showSaveDialog();

  RETURN_CODE showSaveDialog(Object f);

  Object getFile();

  Object[] getFiles();

  void setSelectionMode(SEL_TYPE type);

  SEL_TYPE getSelectionMode();

  void setViewType(VIEW_TYPE type);

  VIEW_TYPE getViewType();

  void setModalParent(Object parent);
}
