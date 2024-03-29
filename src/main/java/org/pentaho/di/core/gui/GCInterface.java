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

package org.pentaho.di.core.gui;

import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.trans.step.StepMeta;

public interface GCInterface extends PrimitiveGCInterface {

  void drawJobEntryIcon(int x, int y, JobEntryCopy jobEntryCopy, float magnification);

  void drawJobEntryIcon(int x, int y, JobEntryCopy jobEntryCopy);

  void drawStepIcon(int x, int y, StepMeta stepMeta, float magnification);

  void drawStepIcon(int x, int y, StepMeta stepMeta);
}
