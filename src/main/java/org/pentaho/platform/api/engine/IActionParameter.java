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

package org.pentaho.platform.api.engine;

import org.pentaho.commons.connection.IPentahoResultSet;

import java.util.List;
import java.util.Map;

/**
 * An <code>IActionParameter</code> represents one input or output in an <tt>IActionSequence</tt>. The
 * <tt>IActionParameter</tt> is made up of a name or key, and a value.
 */
public interface IActionParameter {

  /**
   * Parameter type of <tt>String</tt>
   */
  String TYPE_STRING = "string"; //$NON-NLS-1$

  /**
   * Parameter type of <code>int</code>
   */
  String TYPE_INTEGER = "integer"; //$NON-NLS-1$

  /**
   * Parameter type of <tt>List</tt>.
   */
  String TYPE_LIST = "list"; //$NON-NLS-1$

  /**
   * Parameter type indicating streamable content. @see
   * <tt>RuntimeContext#getOutputStream(java.lang.String,java.lang.String,java.lang.String)</tt>
   */
  String TYPE_CONTENT = "content"; //$NON-NLS-1$

  /**
   * Parameter type of <tt>Date</tt>
   */
  String TYPE_DATE = "date"; //$NON-NLS-1$

  /**
   * Parameter type of <tt>IPentahoResultSet</tt>
   */
  String TYPE_RESULT_SET = "resultset"; //$NON-NLS-1$

  /**
   * Parameter type of <tt>BigDecimal</tt>
   */
  String TYPE_DECIMAL = "bigdecimal"; //$NON-NLS-1$

  /**
   * Parameter type indicating any type of <tt>Object</tt>
   */
  String TYPE_OBJECT = "object"; //$NON-NLS-1$

  /** This parameter allows prompting */
  int PROMPT_ALLOWED = 0;

  /** This parameter needs to be prompted for a value */
  int PROMPT_NEEDED = 1;

  /** This parameter does not allow prompting */
  int PROMPT_NEVER = 2;

  /** A component has already specified a prompt for this parameter */
  int PROMPT_PENDING = 3;

  /**
   * Get the name, or the key for this ActionParameter.
   * 
   * @return the ActionParameter name
   */
  String getName();

  /**
   * Get the value for this ActionParameter as type String.
   * 
   * @return the ActionParameter value as a String. getType() should be referenced first to be sure the value type
   *         is TYPE_STRING.
   */
  String getStringValue();

  /**
   * Get the value for this ActionParameter as a generic Java Object.
   * 
   * @return the ActionParameter value as an Object
   */
  Object getValue();

  /**
   * Get the value for this ActionParameter as a java.util.List.
   * 
   * @return the ActionParameter value as a List. getType() should be referenced first to be sure the value type is
   *         TYPE_LIST.
   */
  @SuppressWarnings( "rawtypes" )
  List getValueAsList();

  /**
   * Get the value for this ActionParameter as a IPentahoResultSet
   * 
   * @return the IPentahoResultSet getType() should be referenced first to be sure the value type is
   *         TYPE_RESULT_SET.
   */
  IPentahoResultSet getValueAsResultSet();

  /**
   * Return the value type as one of the constants available in this class.
   * 
   * @return valid return values are TYPE_STRING, TYPE_INTEGER, TYPE_LIST, TYPE_CONTENT or TYPE_DATE
   */
  String getType();

  /**
   * Sets the value object for this ActionParameter.
   * 
   * @param value
   *          the value Object to be set.
   */
  void setValue(Object value);

  /**
   * @return List of where the parameter may come from (request, session, etc)
   */
  @SuppressWarnings( "rawtypes" )
  List getVariables();

  /**
   * Check if this ActionParameter has a default value set.
   * 
   * @return true if there is a default value, otherwise false
   */
  boolean hasDefaultValue();

  /**
   * Check to se if a value has been set for this parameter. Default value does not count;
   * 
   * @return true if this parameter has a non default value
   */
  boolean hasValue();

  /**
   * Check to see if the value returned from thisActionParameter is indeed the default value instead of a value
   * that was set.
   * 
   * @return true if the parameter is using the default value, false otherwise
   */
  boolean isDefaultValue();

  /**
   * Check to see if the value (includes the default value) is null.
   * 
   * @return true if the value is null, otherwise false
   */
  boolean isNull();

  /**
   * See if we need to do any cleanup here
   * 
   */
  void dispose();

  /**
   * Returns the prompt status for this parameter.
   * 
   * @return the status.
   * @see IActionParameter#PROMPT_ALLOWED
   * @see IActionParameter#PROMPT_NEVER
   * @see IActionParameter#PROMPT_NEEDED
   * @see IActionParameter#PROMPT_PENDING
   */
  int getPromptStatus();

  /**
   * Sets the prompt status for this parameter.
   * 
   * @param status
   *          The status to set.
   * @return true if the set was successful or false if the current setting cannot be changed.
   * @see IActionParameter#PROMPT_ALLOWED
   * @see IActionParameter#PROMPT_NEVER
   * @see IActionParameter#PROMPT_NEEDED
   * @see IActionParameter#PROMPT_PENDING
   */
  boolean setPromptStatus(int status);

  // // Selection Support
  /*
   * Check to see if selections are set for this Parameter
   */
  boolean hasSelections();

  /**
   * The display name to use when building a prompt.
   * 
   * @return The display name for the prompt.
   */
  String getSelectionDisplayName();

  /**
   * When building a parameter prompt page, what is the name of the prompt
   * 
   * @param value
   * @return name for the value
   */
  String getSelectionNameForValue(String value);

  /**
   * 
   * @return Whether parameter should be displayed in output.
   */
  boolean isOutputParameter();

  /**
   * @deprecated Unused in the platform
   */
  @Deprecated
  @SuppressWarnings( "rawtypes" )
  Map getSelectionNameMap();

  /**
   * @deprecated Unused in the platform
   */
  @Deprecated
  @SuppressWarnings( "rawtypes" )
  List getSelectionValues();

  /**
   * @deprecated Unused in the platform
   */
  @Deprecated
  @SuppressWarnings( "rawtypes" )
  void setParamSelections(List selValues, Map selNames, String displayname);

}
