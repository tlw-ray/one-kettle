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

package org.pentaho.di.ui.job.entry;

import org.eclipse.swt.custom.CCombo;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.ui.core.database.dialog.DatabaseDialog;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andrey Khayrutdinov
 */
public class JobEntryDialog_ConnectionLine_Test {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  private static String INITIAL_NAME = "qwerty";
  private static String INPUT_NAME = "asdfg";

  private static String INITIAL_HOST = "1.2.3.4";
  private static String INPUT_HOST = "5.6.7.8";

  @BeforeClass
  public static void initKettle() throws Exception {
    KettleEnvironment.init();
  }


  @Test
  public void adds_WhenConnectionNameIsUnique() throws Exception {
    JobMeta jobMeta = new JobMeta();

    invokeAddConnectionListener( jobMeta, INPUT_NAME );

    assertOnlyDbExists( jobMeta, INPUT_NAME, INPUT_HOST );
  }

  @Test
  public void ignores_WhenConnectionNameIsUsed() throws Exception {
    JobMeta jobMeta = new JobMeta();
    jobMeta.addDatabase( createDefaultDatabase() );

    invokeAddConnectionListener( jobMeta, null );

    assertOnlyDbExists( jobMeta, INITIAL_NAME, INITIAL_HOST );
  }

  private void invokeAddConnectionListener( JobMeta jobMeta, String answeredName ) throws Exception {
    JobEntryDialog dialog = mock( JobEntryDialog.class );
    when( dialog.showDbDialogUnlessCancelledOrValid( anyDbMeta(), anyDbMeta() ) )
      .thenAnswer( new PropsSettingAnswer( answeredName, INPUT_HOST ) );

    dialog.jobMeta = jobMeta;
    dialog.new AddConnectionListener( mock( CCombo.class ) ).widgetSelected( null );
  }


  @Test
  public void edits_WhenNotRenamed() throws Exception {
    JobMeta jobMeta = new JobMeta();
    jobMeta.addDatabase( createDefaultDatabase() );

    invokeEditConnectionListener( jobMeta, INITIAL_NAME );

    assertOnlyDbExists( jobMeta, INITIAL_NAME, INPUT_HOST );
  }

  @Test
  public void edits_WhenNewNameIsUnique() throws Exception {
    JobMeta jobMeta = new JobMeta();
    jobMeta.addDatabase( createDefaultDatabase() );

    invokeEditConnectionListener( jobMeta, INPUT_NAME );

    assertOnlyDbExists( jobMeta, INPUT_NAME, INPUT_HOST );
  }

  @Test
  public void ignores_WhenNewNameIsUsed() throws Exception {
    JobMeta jobMeta = new JobMeta();
    jobMeta.addDatabase( createDefaultDatabase() );

    invokeEditConnectionListener( jobMeta, null );

    assertOnlyDbExists( jobMeta, INITIAL_NAME, INITIAL_HOST );
  }

  private void invokeEditConnectionListener( JobMeta jobMeta, String answeredName ) throws Exception {
    JobEntryDialog dialog = mock( JobEntryDialog.class );
    when( dialog.showDbDialogUnlessCancelledOrValid( anyDbMeta(), anyDbMeta() ) )
      .thenAnswer( new PropsSettingAnswer( answeredName, INPUT_HOST ) );

    CCombo combo = mock( CCombo.class );
    when( combo.getText() ).thenReturn( INITIAL_NAME );

    dialog.jobMeta = jobMeta;
    dialog.new EditConnectionListener( combo ).widgetSelected( null );
  }


  private DatabaseMeta createDefaultDatabase() {
    DatabaseMeta existing = new DatabaseMeta();
    existing.setName( INITIAL_NAME );
    existing.setHostname( INITIAL_HOST );
    return existing;
  }

  private void assertOnlyDbExists( JobMeta jobMeta, String name, String host ) {
    assertEquals( 1, jobMeta.getDatabases().size() );
    assertEquals( name, jobMeta.getDatabase( 0 ).getName() );
    assertEquals( host, jobMeta.getDatabase( 0 ).getHostname() );
  }

  private static DatabaseMeta anyDbMeta() {
    return any( DatabaseMeta.class );
  }

  private static class PropsSettingAnswer implements Answer<String> {
    private final String name;
    private final String host;

    public PropsSettingAnswer( String name, String host ) {
      this.name = name;
      this.host = host;
    }

    @Override
    public String answer( InvocationOnMock invocation ) throws Throwable {
      DatabaseMeta meta = (DatabaseMeta) invocation.getArguments()[ 0 ];
      meta.setName( name );
      meta.setHostname( host );
      return name;
    }
  }


  @Test
  public void showDbDialog_ReturnsNull_OnCancel() throws Exception {
    // null as input emulates cancelling
    test_showDbDialogUnlessCancelledOrValid_ShownOnce( null, null );
  }

  @Test
  public void showDbDialog_ReturnsInputName_WhenItIsUnique() throws Exception {
    test_showDbDialogUnlessCancelledOrValid_ShownOnce( INPUT_NAME, INPUT_NAME );
  }

  @Test
  public void showDbDialog_ReturnsInputName_WhenItIsUnique_WithSpaces() throws Exception {
    String input = " " + INPUT_NAME + " ";
    test_showDbDialogUnlessCancelledOrValid_ShownOnce( input, INPUT_NAME );
  }

  @Test
  public void showDbDialog_ReturnsExistingName_WhenNameWasNotChanged() throws Exception {
    // this is the case of editing when name was not changed (e.g., host was updated)
    test_showDbDialogUnlessCancelledOrValid_ShownOnce( INITIAL_NAME, INITIAL_NAME );
  }

  private void test_showDbDialogUnlessCancelledOrValid_ShownOnce( String inputName,
                                                                  String expectedResult ) throws Exception {
    DatabaseDialog databaseDialog = mock( DatabaseDialog.class );
    when( databaseDialog.open() ).thenReturn( inputName );

    JobMeta jobMeta = new JobMeta();
    DatabaseMeta db = createDefaultDatabase();
    jobMeta.addDatabase( db );

    JobEntryDialog dialog = mock( JobEntryDialog.class );
    dialog.databaseDialog = databaseDialog;
    dialog.jobMeta = jobMeta;
    when( dialog.showDbDialogUnlessCancelledOrValid( anyDbMeta(), anyDbMeta() ) ).thenCallRealMethod();

    String result = dialog.showDbDialogUnlessCancelledOrValid( (DatabaseMeta) db.clone(), db );
    assertEquals( expectedResult, result );

    // database dialog should be shown only once
    verify( databaseDialog, times( 1 ) ).open();
  }

  @Test
  public void showDbDialog_LoopsUntilUniqueValueIsInput() throws Exception {
    DatabaseMeta db1 = createDefaultDatabase();

    DatabaseMeta db2 = createDefaultDatabase();
    db2.setName( INPUT_NAME );

    JobMeta jobMeta = new JobMeta();
    jobMeta.addDatabase( db1 );
    jobMeta.addDatabase( db2 );

    final String expectedResult = INPUT_NAME + "2";

    DatabaseDialog databaseDialog = mock( DatabaseDialog.class );
    when( databaseDialog.open() )
      // duplicate
      .thenReturn( INPUT_NAME )
      // duplicate with spaces
      .thenReturn( INPUT_NAME + " " )
      // duplicate in other case
      .thenReturn( INPUT_NAME.toUpperCase() )
      // unique value
      .thenReturn( expectedResult );


    JobEntryDialog dialog = mock( JobEntryDialog.class );
    dialog.databaseDialog = databaseDialog;
    dialog.jobMeta = jobMeta;
    when( dialog.showDbDialogUnlessCancelledOrValid( anyDbMeta(), anyDbMeta() ) ).thenCallRealMethod();

    // try to rename db1 ("qwerty")
    String result = dialog.showDbDialogUnlessCancelledOrValid( (DatabaseMeta) db1.clone(), db1 );
    assertEquals( expectedResult, result );

    // database dialog should be shown four times
    verify( databaseDialog, times( 4 ) ).open();
    // and the error message should be shown three times
    verify( dialog, times( 3 ) ).showDbExistsDialog( anyDbMeta() );
  }
}
