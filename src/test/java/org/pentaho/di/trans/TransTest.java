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

package org.pentaho.di.trans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.StepLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMetaDataCombi;

public class TransTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  int count = 10000;
  Trans trans;
  TransMeta meta;

  @BeforeClass
  public static void beforeClass() throws KettleException {
    KettleEnvironment.init();
  }

  @Before
  public void beforeTest() throws KettleException {
    meta = new TransMeta();
    trans = new Trans( meta );
    trans.setLog( mock( LogChannelInterface.class ) );
    trans.prepareExecution( null );
    trans.startThreads();
  }

  /**
   * PDI-14948 - Execution of trans with no steps never ends
   */
  @Test( timeout = 1000 )
  public void transWithNoStepsIsNotEndless() throws Exception {
    Trans transWithNoSteps = new Trans( new TransMeta() );
    transWithNoSteps = spy( transWithNoSteps );

    transWithNoSteps.prepareExecution( new String[] {} );

    transWithNoSteps.startThreads();

    // check trans lifecycle is not corrupted
    verify( transWithNoSteps ).fireTransStartedListeners();
    verify( transWithNoSteps ).fireTransFinishedListeners();
  }

  @Test
  public void testFindDatabaseWithEncodedConnectionName() {
    DatabaseMeta dbMeta1 =
        new DatabaseMeta( "encoded_DBConnection", "Oracle", "localhost", "access", "test", "111", "test", "test" );
    dbMeta1.setDisplayName( "encoded.DBConnection" );
    meta.addDatabase( dbMeta1 );

    DatabaseMeta dbMeta2 =
        new DatabaseMeta( "normalDBConnection", "Oracle", "localhost", "access", "test", "111", "test", "test" );
    dbMeta2.setDisplayName( "normalDBConnection" );
    meta.addDatabase( dbMeta2 );

    DatabaseMeta databaseMeta = meta.findDatabase( dbMeta1.getDisplayName() );
    assertNotNull( databaseMeta );
    assertEquals( databaseMeta.getName(), "encoded_DBConnection" );
    assertEquals( databaseMeta.getDisplayName(), "encoded.DBConnection" );
  }

  /**
   * PDI-10762 - Trans and TransMeta leak
   */
  @Test
  public void testLoggingObjectIsNotLeakInMeta() {
    String expected = meta.log.getLogChannelId();
    meta.clear();
    String actual = meta.log.getLogChannelId();
    assertEquals( "Use same logChannel for empty constructors, or assign General level for clear() calls",
        expected, actual );
  }

  /**
   * PDI-10762 - Trans and TransMeta leak
   */
  @Test
  public void testLoggingObjectIsNotLeakInTrans() throws KettleException {
    Repository rep = mock( Repository.class );
    RepositoryDirectoryInterface repInt = mock( RepositoryDirectoryInterface.class );
    when(
      rep.loadTransformation( anyString(), any( RepositoryDirectoryInterface.class ),
        any( ProgressMonitorListener.class ), anyBoolean(), anyString() ) ).thenReturn( meta );
    when( rep.findDirectory( anyString() ) ).thenReturn( repInt );

    Trans trans = new Trans( meta, rep, "junit", "junitDir", "fileName" );
    assertEquals( "Log channel General assigned", LogChannel.GENERAL.getLogChannelId(), trans.log
        .getLogChannelId() );
  }

  /**
   * PDI-5229 - ConcurrentModificationException when restarting transformation Test that listeners can be accessed
   * concurrently during transformation finish
   * 
   * @throws KettleException
   * @throws InterruptedException
   */
  @Test
  public void testTransFinishListenersConcurrentModification() throws KettleException, InterruptedException {
    CountDownLatch start = new CountDownLatch( 1 );
    TransFinishListenerAdder add = new TransFinishListenerAdder( trans, start );
    TransFinishListenerFirer firer = new TransFinishListenerFirer( trans, start );
    startThreads( add, firer, start );
    assertEquals( "All listeners are added: no ConcurrentModificationException", count, add.c );
    assertEquals( "All Finish listeners are iterated over: no ConcurrentModificationException", count, firer.c );
  }

  /**
   * Test that listeners can be accessed concurrently during transformation start
   * 
   * @throws InterruptedException
   */
  @Test
  public void testTransStartListenersConcurrentModification() throws InterruptedException {
    CountDownLatch start = new CountDownLatch( 1 );
    TransFinishListenerAdder add = new TransFinishListenerAdder( trans, start );
    TransStartListenerFirer starter = new TransStartListenerFirer( trans, start );
    startThreads( add, starter, start );
    assertEquals( "All listeners are added: no ConcurrentModificationException", count, add.c );
    assertEquals( "All Start listeners are iterated over: no ConcurrentModificationException", count, starter.c );
  }

  /**
   * Test that transformation stop listeners can be accessed concurrently
   * 
   * @throws InterruptedException
   */
  @Test
  public void testTransStoppedListenersConcurrentModification() throws InterruptedException {
    CountDownLatch start = new CountDownLatch( 1 );
    TransStoppedCaller stopper = new TransStoppedCaller( trans, start );
    TransStopListenerAdder adder = new TransStopListenerAdder( trans, start );
    startThreads( stopper, adder, start );
    assertEquals( "All transformation stop listeners is added", count, adder.c );
    assertEquals( "All stop call success", count, stopper.c );
  }

  @Test
  public void testPDI12424ParametersFromMetaAreCopiedToTrans() throws KettleException, URISyntaxException, IOException {
    String testParam = "testParam";
    String testParamValue = "testParamValue";
    TransMeta mockTransMeta = mock( TransMeta.class );
    when( mockTransMeta.listVariables() ).thenReturn( new String[] {} );
    when( mockTransMeta.listParameters() ).thenReturn( new String[] { testParam } );
    when( mockTransMeta.getParameterValue( testParam ) ).thenReturn( testParamValue );
    FileObject ktr = KettleVFS.createTempFile( "parameters", ".ktr", "ram://" );
    try ( OutputStream outputStream = ktr.getContent().getOutputStream( true ) ) {
      InputStream inputStream = new ByteArrayInputStream( "<transformation></transformation>".getBytes() );
      IOUtils.copy( inputStream, outputStream );
    }
    Trans trans = new Trans( mockTransMeta, null, null, null, ktr.getURL().toURI().toString() );
    assertEquals( testParamValue, trans.getParameterValue( testParam ) );
  }

  @Test
  public void testRecordsCleanUpMethodIsCalled() throws Exception {
    Database mockedDataBase = mock( Database.class );
    Trans trans = mock( Trans.class );

    StepLogTable stepLogTable = StepLogTable.getDefault( mock( VariableSpace.class ), mock( HasDatabasesInterface.class )  );
    stepLogTable.setConnectionName( "connection" );

    TransMeta transMeta = new TransMeta(  );
    transMeta.setStepLogTable( stepLogTable );

    when( trans.getTransMeta() ).thenReturn( transMeta );
    when( trans.createDataBase( any( DatabaseMeta.class ) ) ).thenReturn( mockedDataBase );
    when( trans.getSteps() ).thenReturn( new ArrayList<StepMetaDataCombi>() );

    doCallRealMethod().when( trans ).writeStepLogInformation();
    trans.writeStepLogInformation();

    verify( mockedDataBase ).cleanupLogRecords( stepLogTable );
  }

  @Test
  public void testFireTransFinishedListeners() throws Exception {
    Trans trans = new Trans();
    TransListener mockListener = mock( TransListener.class );
    trans.setTransListeners( Collections.singletonList( mockListener ) );

    trans.fireTransFinishedListeners();

    verify( mockListener ).transFinished( trans );
  }

  @Test( expected = KettleException.class )
  public void testFireTransFinishedListenersExceptionOnTransFinished() throws Exception {
    Trans trans = new Trans();
    TransListener mockListener = mock( TransListener.class );
    doThrow( KettleException.class ).when( mockListener ).transFinished( trans );
    trans.setTransListeners( Collections.singletonList( mockListener ) );

    trans.fireTransFinishedListeners();
  }

  @Test
  public void testFinishStatus() throws Exception {
    while ( trans.isRunning() ) {
      Thread.sleep( 1 );
    }
    assertEquals( Trans.STRING_FINISHED, trans.getStatus() );
  }

  @Test
  public void testSafeStop() {
    StepInterface step = mock( StepInterface.class );
    when( step.isSafeStopped() ).thenReturn( false );
    when( step.getStepname() ).thenReturn( "stepName" );

    StepMetaDataCombi stepMetaDataCombi = new StepMetaDataCombi();
    stepMetaDataCombi.step = step;

    trans.setSteps( Collections.singletonList( stepMetaDataCombi ) );
    Result result = trans.getResult();
    assertFalse( result.isSafeStop() );

    when( step.isSafeStopped() ).thenReturn( true );
    result = trans.getResult();
    assertTrue( result.isSafeStop() );
  }

  private void startThreads( Runnable one, Runnable two, CountDownLatch start ) throws InterruptedException {
    Thread th = new Thread( one );
    Thread tt = new Thread( two );
    th.start();
    tt.start();
    start.countDown();
    th.join();
    tt.join();
  }

  private abstract class TransKicker implements Runnable {
    protected Trans tr;
    protected int c = 0;
    protected CountDownLatch start;
    protected int max = count;

    TransKicker( Trans tr, CountDownLatch start ) {
      this.tr = tr;
      this.start = start;
    }

    protected boolean isStopped() {
      c++;
      return c >= max;
    }
  }

  private class TransStoppedCaller extends TransKicker {
    TransStoppedCaller( Trans tr, CountDownLatch start ) {
      super( tr, start );
    }

    @Override
    public void run() {
      try {
        start.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException();
      }
      while ( !isStopped() ) {
        trans.stopAll();
      }
    }
  }

  private class TransStopListenerAdder extends TransKicker {
    TransStopListenerAdder( Trans tr, CountDownLatch start ) {
      super( tr, start );
    }

    @Override
    public void run() {
      try {
        start.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException();
      }
      while ( !isStopped() ) {
        trans.addTransStoppedListener( transStoppedListener );
      }
    }
  }

  private class TransFinishListenerAdder extends TransKicker {
    TransFinishListenerAdder( Trans tr, CountDownLatch start ) {
      super( tr, start );
    }

    @Override
    public void run() {
      try {
        start.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException();
      }
      // run
      while ( !isStopped() ) {
        tr.addTransListener( listener );
      }
    }
  }

  private class TransFinishListenerFirer extends TransKicker {
    TransFinishListenerFirer( Trans tr, CountDownLatch start ) {
      super( tr, start );
    }

    @Override
    public void run() {
      try {
        start.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException();
      }
      // run
      while ( !isStopped() ) {
        try {
          tr.fireTransFinishedListeners();
          // clean array blocking queue
          tr.waitUntilFinished();
        } catch ( KettleException e ) {
          throw new RuntimeException();
        }
      }
    }
  }

  private class TransStartListenerFirer extends TransKicker {
    TransStartListenerFirer( Trans tr, CountDownLatch start ) {
      super( tr, start );
    }

    @Override
    public void run() {
      try {
        start.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException();
      }
      // run
      while ( !isStopped() ) {
        try {
          tr.fireTransStartedListeners();
        } catch ( KettleException e ) {
          throw new RuntimeException();
        }
      }
    }
  }

  private final TransListener listener = new TransListener() {
    @Override
    public void transStarted( Trans trans ) throws KettleException {
    }

    @Override
    public void transActive( Trans trans ) {
    }

    @Override
    public void transFinished( Trans trans ) throws KettleException {
    }
  };

  private final TransStoppedListener transStoppedListener = new TransStoppedListener() {
    @Override
    public void transStopped( Trans trans ) {
    }

  };

  @Test
  public void testNewTransformationsWithContainerObjectId() throws Exception {
    TransMeta meta = mock( TransMeta.class );
    doReturn( new String[] { "X", "Y", "Z" } ).when( meta ).listVariables();
    doReturn( new String[] { "A", "B", "C" } ).when( meta ).listParameters();
    doReturn( "XYZ" ).when( meta ).getVariable( anyString() );
    doReturn( "" ).when( meta ).getParameterDescription( anyString() );
    doReturn( "" ).when( meta ).getParameterDefault( anyString() );
    doReturn( "ABC" ).when( meta ).getParameterValue( anyString() );

    String carteId = UUID.randomUUID().toString();
    doReturn( carteId ).when( meta ).getContainerObjectId();

    Trans trans = new Trans( meta );

    assertEquals( carteId, trans.getContainerObjectId() );
  }

  /**
   * This test demonstrates the issue fixed in PDI-17436.
   * When a job is scheduled twice, it gets the same log channel Id and both logs get merged
   */
  @Test
  public void testTwoTransformationsGetSameLogChannelId() throws Exception {
    TransMeta meta = mock( TransMeta.class );
    doReturn( new String[] { "X", "Y", "Z" } ).when( meta ).listVariables();
    doReturn( new String[] { "A", "B", "C" } ).when( meta ).listParameters();
    doReturn( "XYZ" ).when( meta ).getVariable( anyString() );
    doReturn( "" ).when( meta ).getParameterDescription( anyString() );
    doReturn( "" ).when( meta ).getParameterDefault( anyString() );
    doReturn( "ABC" ).when( meta ).getParameterValue( anyString() );

    Trans trans1 = new Trans( meta );
    Trans trans2 = new Trans( meta );

    assertEquals( trans1.getLogChannelId(), trans2.getLogChannelId() );
  }

  /**
   * This test demonstrates the fix for PDI-17436.
   * Two schedules -> two Carte object Ids -> two log channel Ids
   */
  @Test
  public void testTwoTransformationsGetDifferentLogChannelIdWithDifferentCarteId() throws Exception {
    TransMeta meta1 = mock( TransMeta.class );
    doReturn( new String[] { "X", "Y", "Z" } ).when( meta1 ).listVariables();
    doReturn( new String[] { "A", "B", "C" } ).when( meta1 ).listParameters();
    doReturn( "XYZ" ).when( meta1 ).getVariable( anyString() );
    doReturn( "" ).when( meta1 ).getParameterDescription( anyString() );
    doReturn( "" ).when( meta1 ).getParameterDefault( anyString() );
    doReturn( "ABC" ).when( meta1 ).getParameterValue( anyString() );

    TransMeta meta2 = mock( TransMeta.class );
    doReturn( new String[] { "X", "Y", "Z" } ).when( meta2 ).listVariables();
    doReturn( new String[] { "A", "B", "C" } ).when( meta2 ).listParameters();
    doReturn( "XYZ" ).when( meta2 ).getVariable( anyString() );
    doReturn( "" ).when( meta2 ).getParameterDescription( anyString() );
    doReturn( "" ).when( meta2 ).getParameterDefault( anyString() );
    doReturn( "ABC" ).when( meta2 ).getParameterValue( anyString() );


    String carteId1 = UUID.randomUUID().toString();
    String carteId2 = UUID.randomUUID().toString();

    doReturn( carteId1 ).when( meta1 ).getContainerObjectId();
    doReturn( carteId2 ).when( meta2 ).getContainerObjectId();

    Trans trans1 = new Trans( meta1 );
    Trans trans2 = new Trans( meta2 );

    assertNotEquals( trans1.getContainerObjectId(), trans2.getContainerObjectId() );
    assertNotEquals( trans1.getLogChannelId(), trans2.getLogChannelId() );
  }
}
