/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.compatibility;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This interface provides a way to look at a Number, String, Integer, Date... the same way. The methods mentioned in
 * this interface are common to all Value types.
 *
 * @author Matt
 * @since 15-10-2004
 */
public interface ValueInterface {
  int getType();

  String getTypeDesc();

  String getString();

  double getNumber();

  Date getDate();

  boolean getBoolean();

  long getInteger();

  BigDecimal getBigNumber();

  Serializable getSerializable();

  byte[] getBytes();

  void setString(String string);

  void setNumber(double number);

  void setDate(Date date);

  void setBoolean(boolean bool);

  void setInteger(long number);

  void setBigNumber(BigDecimal number);

  void setSerializable(Serializable ser);

  void setBytes(byte[] b);

  int getLength();

  int getPrecision();

  void setLength(int length);

  void setPrecision(int precision);

  void setLength(int length, int precision);

  Object clone();
}
