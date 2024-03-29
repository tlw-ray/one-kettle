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

package org.pentaho.di.job.entries.mysqlbulkload;

import java.util.Arrays;
import java.util.List;

import org.junit.ClassRule;
import org.pentaho.di.job.entry.loadSave.JobEntryLoadSaveTestSupport;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class JobEntryMysqlBulkLoadLoadSaveTest extends JobEntryLoadSaveTestSupport<JobEntryMysqlBulkLoad> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  @Override
  protected Class<JobEntryMysqlBulkLoad> getJobEntryClass() {
    return JobEntryMysqlBulkLoad.class;
  }

  @Override
  protected List<String> listCommonAttributes() {
    return Arrays.asList("schemaname", "tablename", "filename", "separator", "enclosed",
            "escaped", "linestarted", "lineterminated", "replacedata", "ignorelines", "listattribut",
            "localInfile", "prorityvalue", "addFileToResult", "database");
  }

}
