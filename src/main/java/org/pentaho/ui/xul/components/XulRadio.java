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

package org.pentaho.ui.xul.components;

import org.pentaho.ui.xul.XulComponent;

/**
 * The interface for the radio XUL widget.
 * 
 * @author aphillips
 */
public interface XulRadio extends XulComponent {

  /**
   * Sets/clears the check in the box.
   * 
   * @param checked
   *          if true, checks the box, clears it otherwise.
   */
  void setSelected(boolean checked);

  /**
   * 
   * @return is the box checked?
   */
  boolean isSelected();

  /**
   * The label that appears next to the radio.
   * 
   * @return the radio label.
   */
  String getLabel();

  /**
   * Sets the label that appears next to the radio.
   * 
   * @param label
   *          the radio label that should display.
   */
  void setLabel(String label);

  /**
   * XUL's attribute is "disabled", thus this acts exactly the opposite of SWT/Swing/AWT. If the property is not
   * available, then the control is enabled.
   * 
   * @return boolean true if the control is disabled.
   */
  boolean isDisabled();

  /**
   * 
   * @param dis
   *          If true, disable this button. Otherwise, attribute should be removed.
   */
  void setDisabled(boolean dis);

  /**
   * According to XUL spec, this is the appropriate event to listen to for radio state changes from a user
   * interface. Note that to remain pure to the spec, this will not report a state change set via code or script.
   * 
   * @see radioStateChange at http://www.xulplanet.com/references/elemref/ref_EventHandlers.html
   * 
   * @param method
   *          The method to execute when the radio is checked or unchecked.
   */
  void setCommand(final String method);

  /**
   * The value of the radio button.
   * 
   * @return the radio value.
   */
  String getValue();

  /**
   * Sets the value of the radio button.
   */
  void setValue(String aValue);
}
