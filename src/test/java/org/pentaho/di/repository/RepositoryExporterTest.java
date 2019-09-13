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

package org.pentaho.di.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.apache.commons.vfs2.FileObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.imp.ImportRules;
import org.pentaho.di.imp.rule.ImportRuleInterface;
import org.pentaho.di.imp.rules.JobHasDescriptionImportRule;
import org.pentaho.di.imp.rules.TransformationHasDescriptionImportRule;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.utils.TestUtils;
import org.xml.sax.InputSource;

public class RepositoryExporterTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  @Mock
  Repository repository;
  @Mock
  LogChannelInterface log;
  @Mock
  private RepositoryDirectoryInterface root;

  private FileObject fileObject;
  private String xmlFileName;

  @Before
  public void beforeTest() throws IOException, KettleException {
    MockitoAnnotations.initMocks( this );
    Mockito.when( repository.getLog() ).thenReturn( log );
    ObjectId id = new ObjectId() {
      @Override
      public String getId() {
        return "1";
      }
    };
    Mockito.when( root.getDirectoryIDs() ).thenReturn( new ObjectId[] { id } );
    Mockito.when( root.findDirectory( Mockito.any( ObjectId.class ) ) ).thenReturn( root );

    Mockito.when( repository.getJobNames( Mockito.any( ObjectId.class ), Mockito.anyBoolean() ) ).thenReturn(
      new String[] { "job1" } );
    Mockito.when( repository.getTransformationNames( Mockito.any( ObjectId.class ), Mockito.anyBoolean() ) )
      .thenReturn( new String[] { "trans1" } );

    Mockito.when( root.getPath() ).thenReturn( "path" );

    // here we are stubbing load jobs from repository
    Answer<JobMeta> jobLoader = new Answer<JobMeta>() {
      @Override
      public JobMeta answer( InvocationOnMock invocation ) throws Throwable {
        String jobName = String.class.cast( invocation.getArguments()[ 0 ] );
        JobMeta jobMeta = Mockito.mock( JobMeta.class );
        Mockito.when( jobMeta.getXML() ).thenReturn( "<" + jobName + ">" + "found" + "</" + jobName + ">" );
        Mockito.when( jobMeta.getName() ).thenReturn( jobName );
        return jobMeta;
      }
    };

    Mockito.when(
      repository.loadJob( Mockito.anyString(), Mockito.any( RepositoryDirectoryInterface.class ), Mockito
        .any( ProgressMonitorListener.class ), Mockito.anyString() ) ).thenAnswer( jobLoader );

    // and this is for transformation load
    Answer<TransMeta> transLoader = new Answer<TransMeta>() {
      @Override
      public TransMeta answer( InvocationOnMock invocation ) throws Throwable {
        String transName = String.class.cast( invocation.getArguments()[ 0 ] );
        TransMeta transMeta = Mockito.mock( TransMeta.class );
        Mockito.when( transMeta.getXML() ).thenReturn( "<" + transName + ">" + "found" + "</" + transName + ">" );
        Mockito.when( transMeta.getName() ).thenReturn( transName );
        return transMeta;
      }
    };
    Mockito.when(
      repository.loadTransformation( Mockito.anyString(), Mockito.any( RepositoryDirectoryInterface.class ), Mockito
        .any( ProgressMonitorListener.class ), Mockito.anyBoolean(), Mockito.anyString() ) ).thenAnswer( transLoader );

    // export file
    xmlFileName = TestUtils.createRamFile( getClass().getSimpleName() + "/export.xml" );
    fileObject = TestUtils.getFileObject( xmlFileName );
  }

  /**
   * Test that jobs can be exported with feedback
   *
   * @throws Exception
   */
  @Test
  public void testExportJobsWithFeedback() throws Exception {
    RepositoryExporter exporter = new RepositoryExporter( repository );
    List<ExportFeedback> feedback =
      exporter.exportAllObjectsWithFeedback( null, xmlFileName, root, RepositoryExporter.ExportType.JOBS.toString() );

    Assert.assertEquals( "Feedback contains all items recorded", 2, feedback.size() );
    ExportFeedback fb = feedback.get( 1 );

    Assert.assertEquals( "Job1 was exproted", "job1", fb.getItemName() );
    Assert.assertEquals( "Repository path for Job1 is specified", "path", fb.getItemPath() );

    String res = this.validateXmlFile( fileObject.getContent().getInputStream(), "//job1" );
    Assert.assertEquals( "Export xml contains exported job xml", "found", res );
  }

  /**
   * Test that transformations can be exported with feedback
   *
   * @throws Exception
   */
  @Test
  public void testExportTransformationsWithFeedback() throws Exception {
    RepositoryExporter exporter = new RepositoryExporter( repository );
    List<ExportFeedback> feedback =
      exporter.exportAllObjectsWithFeedback( null, xmlFileName, root, RepositoryExporter.ExportType.TRANS.toString() );

    Assert.assertEquals( "Feedback contains all items recorded", 2, feedback.size() );
    ExportFeedback fb = feedback.get( 1 );

    Assert.assertEquals( "Job1 was exproted", "trans1", fb.getItemName() );
    Assert.assertEquals( "Repository path for Job1 is specified", "path", fb.getItemPath() );

    String res = this.validateXmlFile( fileObject.getContent().getInputStream(), "//trans1" );
    Assert.assertEquals( "Export xml contains exported job xml", "found", res );
  }

  /**
   * Test that we can have some feedback on rule violations
   *
   * @throws KettleException
   */
  @Test
  public void testExportAllRulesViolation() throws Exception {
    RepositoryExporter exporter = new RepositoryExporter( repository );
    exporter.setImportRulesToValidate( getImportRules() );

    List<ExportFeedback> feedback =
      exporter.exportAllObjectsWithFeedback( null, xmlFileName, root, RepositoryExporter.ExportType.ALL.toString() );

    Assert.assertEquals( "Feedback contains all items recorded", 3, feedback.size() );

    for ( ExportFeedback feed : feedback ) {
      if ( feed.isSimpleString() ) {
        continue;
      }
      Assert.assertEquals( "all items rejected: " + feed.toString(), ExportFeedback.Status.REJECTED, feed.getStatus() );
    }
    Assert.assertTrue( "Export file is deleted", !fileObject.exists() );
  }

  /**
   * PDI-7734 - EE Repository export with Rules: When it fails, no UI feedback is given and the file is incomplete
   * <p/>
   * this tests bachward compatibility mode when attempt to export repository is called from code that does not support
   * feddbacks.
   *
   * @throws KettleException
   */
  @Test( expected = KettleException.class )
  public void testExportAllExceptionThrown() throws KettleException {
    RepositoryExporter exporter = new RepositoryExporter( repository );
    exporter.setImportRulesToValidate( getImportRules() );

    try {
      exporter.exportAllObjects( null, xmlFileName, root, RepositoryExporter.ExportType.ALL.toString() );
    } catch ( KettleException e ) {
      // some debugging palce e.getStackTrace();
      throw e;
    }
  }

  private ImportRules getImportRules() {
    ImportRules imp = new ImportRules();
    List<ImportRuleInterface> impRules = new ArrayList<ImportRuleInterface>( 1 );
    JobHasDescriptionImportRule rule = new JobHasDescriptionImportRule();
    rule.setEnabled( true );
    rule.setMinLength( 19000 );
    impRules.add( rule );
    TransformationHasDescriptionImportRule trRule = new TransformationHasDescriptionImportRule() {
      public String toString() {
        return "stub to avoid call to Plugin registry";
      }
    };
    trRule.setEnabled( true );
    trRule.setMinLength( 19001 );
    impRules.add( trRule );
    imp.setRules( impRules );
    return imp;
  }

  private String validateXmlFile( InputStream is, String xpath ) throws Exception {
    XPath xPath = XPathFactory.newInstance().newXPath();
    XPathExpression expression = xPath.compile( xpath );
    BufferedReader in = new BufferedReader( new InputStreamReader( is, "UTF8" ) );
    InputSource input = new InputSource( in );
    String result = expression.evaluate( input );

    return result;
  }
}
