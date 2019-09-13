/*
 * ! ******************************************************************************
 *
 *  Pentaho Data Integration
 *
 *  Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *
 * ******************************************************************************
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * *****************************************************************************
 */

package org.pentaho.di.engine.api.remote;

import org.pentaho.di.engine.api.model.LogicalModelElement;
import org.pentaho.di.engine.api.model.ModelType;

import java.io.Serializable;

/**
 * Placeholder event source for remote events.
 * <p>
 * {@link PDIEvent} objects usually have a reference to a logical-model event source.
 * Since these sources may not be serializable, and the client will have different references, this source can
 * be used as a placeholder when the event is tunneled back to to the client.
 * <p>
 * Created by hudak on 2/10/17.
 */
public final class RemoteSource implements LogicalModelElement, Serializable {
  private static final long serialVersionUID = -8344589338390125137L;
  private final String id;
  private final ModelType modelType;
  private final String requestUUID;

  public RemoteSource( String id ) {
    this.id = id;
    this.modelType = null;
    this.requestUUID = null;
  }

  public RemoteSource( ModelType modelType ) {
    this.modelType = modelType;
    this.id = null;
    this.requestUUID = null;
  }

  public RemoteSource( ModelType modelType, String id ) {
    this.modelType = modelType;
    this.id = id;
    this.requestUUID = null;
  }

  public RemoteSource( String requestUUID, ModelType modelType, String id ) {
    this.modelType = modelType;
    this.id = id;
    this.requestUUID = requestUUID;
  }

  @Override public String getId() {
    return id;
  }

  public ModelType getModelType() {
    return modelType;
  }

  public String getRequestUUID() {
    return requestUUID;
  }
}
