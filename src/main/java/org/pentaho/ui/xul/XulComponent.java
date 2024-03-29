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

package org.pentaho.ui.xul;

import org.pentaho.ui.xul.binding.BindingProvider;
import org.pentaho.ui.xul.dom.Element;

import java.beans.PropertyChangeListener;

/**
 * The base interface for any XUL widget.
 * 
 * @author nbaker
 * 
 */
public interface XulComponent extends Element, XulEventSource {

  /**
   * The manageObject is the rendering control or container that corresponds to this XUL component.
   * 
   * @return the impl control that represents this XUL component under the covers.
   */
  Object getManagedObject();

  void setManagedObject(Object managed);

  /**
   * The name is the tag name that this component corresponds to in XUL XML.
   * 
   * @return the XUL tag name.
   */
  String getName();

  /**
   * Every element in XUL can have a unique id
   * 
   * @param id
   *          sets the component's id
   */
  void setId(String id);

  /**
   * 
   * @return the id for this component.
   */
  String getId();

  /**
   * From the XUL specification: http://www.xulplanet.com/references/elemref/ref_XULElement.html#attr_flex
   * Indicates the flexibility of the element, which indicates how an element's container distributes remaining
   * empty space among its children. Flexible elements grow and shrink to fit their given space. Elements with
   * larger flex values will be made larger than elements with lower flex values, at the ratio determined by the
   * two elements. The actual value is not relevant unless there are other flexible elements within the same
   * container. Once the default sizes of elements in a box are calculated, the remaining space in the box is
   * divided among the flexible elements, according to their flex ratios.
   * 
   * @return the flex value for this component
   */
  int getFlex();

  /**
   * This field makes sense only relative to the values of its siblings. NOTE that if only one sibling has a flex
   * value, then that sibling gets ALL the extra space in the container, no matter what the flex value is.
   * 
   * @param flex
   */
  void setFlex(int flex);

  /**
   * Sets the method that will be invoked when this component loses focus. Also hooks up any listeners for this
   * event.
   * 
   * @param method
   *          the method to execute when the focus is lost.
   */
  void setOnblur(String method);

  /**
   * Gets the method that will be invoked when this component loses focus. Also hooks up any listeners for this
   * event.
   */
  String getOnblur();

  /**
   * Set the width of this control
   * 
   */
  void setWidth(int width);

  /**
   * Returns the width of the component
   * 
   * @return the component's width
   */
  int getWidth();

  /**
   * Set the height of the component
   * 
   */
  void setHeight(int height);

  /**
   * Returns the height of the component
   * 
   * @return the component's height
   */
  int getHeight();

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Sets the enablement state of the component
   * 
   * @param disabled
   *          sets this components enabled state
   * 
   */
  void setDisabled(boolean disabled);

  /**
   * XUL's attribute is "disabled", thus this acts exactly the opposite of SWT/Swing/AWT. If the property is not
   * available, then the control is enabled.
   * 
   * @return boolean true if the control is disabled.
   */
  boolean isDisabled();

  void setTooltiptext(String tooltip);

  String getTooltiptext();

  void setBgcolor(String bgcolor);

  String getBgcolor();

  void setPadding(int padding);

  int getPadding();

  void setSpacing(int spacing);

  int getSpacing();

  void adoptAttributes(XulComponent component);

  String getInsertbefore();

  void setInsertbefore(String id);

  String getInsertafter();

  void setInsertafter(String id);

  int getPosition();

  void setPosition(int pos);

  boolean getRemoveelement();

  void setRemoveelement(boolean flag);

  boolean isVisible();

  void setVisible(boolean visible);

  /**
   * Called by the parser when the document is fully parsed. Some implementations require knowledge of parents
   * above the document, or only behave properly when an unbroken chain to the root is in place.
   */
  void onDomReady();

  /**
   * Specifies the alignment of children when the size of the container is greater than the size of it's children.
   * 
   * @param align
   *          one of [start, center, end].
   */
  void setAlign(String align);

  /**
   * Returns the alignment of children.
   * 
   * @return String specifying the alignment [start, center, end].
   */
  String getAlign();

  /**
   * Sets the ID of the popup menu to show on context action (right-click, etc)
   * 
   * @param id
   */
  void setContext(String id);

  String getContext();

  /**
   * Sets the ID of the popup menu to show when control is right-clicked. (drop-downs)
   * 
   * @param id
   */
  void setPopup(String id);

  String getPopup();

  /**
   * Sets the ID of the menu to show when the control is clicked
   * 
   * @param id
   */
  void setMenu(String id);

  String getMenu();

  /**
   * Sets the ondrag event handler, also notifies the element that it should be draggable
   * 
   * @param ondrag
   *          the controller method to call.
   */
  void setOndrag(String ondrag);

  String getOndrag();

  /**
   * Sets the drageffect value for dnd (move and copy currently supported) When specified, this should use the pen:
   * syntax
   * 
   * @param drageffect
   *          move or copy
   */
  void setDrageffect(String drageffect);

  String getDrageffect();

  /**
   * Sets the ondrop event handler, also notifies the element that it should accept drop events.
   * 
   * @param ondrop
   *          the controller method to call.
   */
  void setOndrop(String ondrop);

  String getOndrop();

  void setDropvetoer(String dropVetoMethod);

  String getDropvetoer();

  void setBindingProvider(BindingProvider bindingProvider);
}
