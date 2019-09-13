/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
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
package org.pentaho.di.job.entries.dostounix;

import java.util.Arrays;

import java.util.List;
import java.util.Map;

import org.junit.ClassRule;
import org.pentaho.di.job.entry.loadSave.JobEntryLoadSaveTestSupport;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class JobEntryDosToUnixTest extends JobEntryLoadSaveTestSupport<JobEntryDosToUnix> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  @Override
  protected Class<JobEntryDosToUnix> getJobEntryClass() {
    return JobEntryDosToUnix.class;
  }

  @Override
  protected List<String> listCommonAttributes() {
    return Arrays.asList(
        "nr_errors_less_than",
        "success_condition",
        "resultfilenames" );
  }

  @Override
  protected Map<String, String> createGettersMap() {
    return toMap(
        "nr_errors_less_than", "getNrErrorsLessThan",
        "success_condition", "getSuccessCondition",
        "resultfilenames", "getResultFilenames" );
  }

  @Override
  protected Map<String, String> createSettersMap() {
    return toMap(
        "nr_errors_less_than", "setNrErrorsLessThan",
        "success_condition", "setSuccessCondition",
        "resultfilenames", "setResultFilenames" );
  }
}
