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
 * The interface for a Xul textbox widget.
 * 
 * @author nbaker
 * 
 */
public interface XulTextbox extends XulComponent {

  /**
   * The maximum value that the number box may be set to. The default value is Infinity.
   * 
   * @param max
   */
  void setMax(String max);

  /**
   * The maximum value that the number box may be set to. The default value is Infinity.
   * 
   * @return max
   */
  String getMax();

  /**
   * The minimum value that the number box may be set to. The default value is 0.
   * 
   * @param min
   */
  void setMin(String min);

  /**
   * The minimum value that the number box may be set to. The default value is 0.
   * 
   * @return min
   */
  String getMin();

  /**
   * Sets the value that will display as default in the textbox.
   * 
   * @param str
   *          The textbox's default value.
   */
  void setValue(String str);

  /**
   * 
   * @return The value entered into the textbox.
   */
  String getValue();

  /**
   * 
   * @param dis
   *          If true, disable this button. Otherwise, attribute should be removed.
   */
  void setDisabled(boolean dis);

  /**
   * XUL's attribute is "disabled", thus this acts exactly the opposite of SWT/Swing/AWT. If the property is not
   * available, then the control is enabled.
   * 
   * @return boolean true if the control is disabled.
   */
  boolean isDisabled();

  /**
   * 
   * @return int The maximum number of characters that the textbox allows to be entered.
   */
  int getMaxlength();

  /**
   * Sets the maximum length of the string that can be entered into the textbox.
   * 
   * @param length
   *          The number of characters allowed.
   */
  void setMaxlength(int length);

  /**
   * If true, the textbox displays multiple lines. The text entered in the textbox will wrap, and scrollbars are
   * available.
   * 
   * @return If true, is multiline; otherwise is a single line textbox.
   */
  boolean isMultiline();

  /**
   * Sets whether the textbox will be multiline or single; Single is the default.
   * 
   * @param multi
   *          If true, set to multiple line.
   */
  void setMultiline(boolean multi);

  boolean isReadonly();

  void setReadonly(boolean readOnly);

  String getType();

  void setType(String type);

  void selectAll();

  void setFocus();

  /**
   * 
   * @return the native control that controls the text manipulation... could be the same as the managedObject, but
   *         does not have to be. (This is added as a bit of a hack for PDI variable support.)
   */
  Object getTextControl();

  /**
   * Sets the method that will be invoked when the text in this component changes. Also hooks up any listeners for
   * this event.
   * 
   * @param method
   *          the method to execute when the input is changed.
   */
  void setOninput(String method);
}
