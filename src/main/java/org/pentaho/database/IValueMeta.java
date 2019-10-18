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

package org.pentaho.database;

public interface IValueMeta extends Cloneable {
  /** Value type indicating that the value has no type set */
  int TYPE_NONE = 0;

  /** Value type indicating that the value contains a floating point double precision number. */
  int TYPE_NUMBER = 1;

  /** Value type indicating that the value contains a text String. */
  int TYPE_STRING = 2;

  /** Value type indicating that the value contains a Date. */
  int TYPE_DATE = 3;

  /** Value type indicating that the value contains a boolean. */
  int TYPE_BOOLEAN = 4;

  /** Value type indicating that the value contains a long integer. */
  int TYPE_INTEGER = 5;

  /** Value type indicating that the value contains a floating point precision number with arbitrary precision. */
  int TYPE_BIGNUMBER = 6;

  /** Value type indicating that the value contains an Object. */
  int TYPE_SERIALIZABLE = 7;

  /** Value type indicating that the value contains binary data: BLOB, CLOB, ... */
  int TYPE_BINARY = 8;

  String getName();

  void setName(String name);

  int getLength();

  void setLength(int length);

  int getPrecision();

  void setPrecision(int precision);

  void setLength(int length, int precision);

  int getType();

  void setType(int type);

  /**
   * @return a copy of this value meta object
   */
  IValueMeta clone();
}
