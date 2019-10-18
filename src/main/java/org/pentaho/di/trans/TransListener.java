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

package org.pentaho.di.trans;

import org.pentaho.di.core.exception.KettleException;

public interface TransListener {

  /**
   * This transformation started
   *
   * @param trans
   * @throws KettleException
   */
  void transStarted( Trans trans ) throws KettleException;

  /**
   * This transformation went from an in-active to an active state.
   *
   * @param trans
   * @throws KettleException
   */
  void transActive(Trans trans);

  /**
   * The transformation has finished.
   *
   * @param trans
   * @throws KettleException
   */
  void transFinished(Trans trans) throws KettleException;
}
