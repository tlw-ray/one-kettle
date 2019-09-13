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

package org.pentaho.di.ui.job.entries.trans;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.ObjectLocationSpecificationMethod;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.extension.ExtensionPointHandler;
import org.pentaho.di.core.extension.KettleExtensionPoint;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryObject;
import org.pentaho.di.repository.RepositoryObjectType;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.ConstUI;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.dialog.SimpleMessageDialog;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.ComboVar;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.ui.util.DialogHelper;
import org.pentaho.di.ui.util.DialogUtils;
import org.pentaho.di.ui.util.SwtSvgImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This dialog allows you to edit the transformation job entry (JobEntryTrans)
 *
 * @author Matt
 * @since 19-06-2003
 */
public class JobEntryTransDialog extends JobEntryBaseDialog implements JobEntryDialogInterface {
  private static Class<?> PKG = JobEntryTrans.class; // for i18n purposes, needed by Translator2!!

  protected JobEntryTrans jobEntry;

  private static final String[] FILE_FILTERLOGNAMES = new String[] {
    BaseMessages.getString( PKG, "JobTrans.Fileformat.TXT" ),
    BaseMessages.getString( PKG, "JobTrans.Fileformat.LOG" ),
    BaseMessages.getString( PKG, "JobTrans.Fileformat.All" ) };

  public JobEntryTransDialog( Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta ) {
    super( parent, jobEntryInt, rep, jobMeta );
    jobEntry = (JobEntryTrans) jobEntryInt;
  }

  public JobEntryInterface open() {
    Shell parent = getParent();
    display = parent.getDisplay();

    shell = new Shell( parent, props.getJobsDialogStyle() );
    props.setLook( shell );
    JobDialog.setShellImage( shell, jobEntry );

    backupChanged = jobEntry.hasChanged();

    createElements();

    // Detect [X] or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    getData();
    setActive();

    BaseStepDialog.setSize( shell );

    int width = 750;
    int height = Const.isWindows() ? 730 : 720;

    shell.setSize( width, height );
    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return jobEntry;
  }

  protected void createElements() {
    super.createElements();
    shell.setText( BaseMessages.getString( PKG, "JobTrans.Header" ) );

    wlPath.setText( BaseMessages.getString( PKG, "JobTrans.JobStep.Transformation.Label" ) );
    wPassParams.setText( BaseMessages.getString( PKG, "JobTrans.PassAllParameters.Label" ) );

    wClearRows = new Button( gExecution, SWT.CHECK );
    props.setLook( wClearRows );
    wClearRows.setText( BaseMessages.getString( PKG, "JobTrans.ClearResultList.Label" ) );
    FormData fdbClearRows = new FormData();
    fdbClearRows.left = new FormAttachment( 0, 0 );
    fdbClearRows.top = new FormAttachment( wEveryRow, 10 );
    wClearRows.setLayoutData( fdbClearRows );

    wClearFiles = new Button( gExecution, SWT.CHECK );
    props.setLook( wClearFiles );
    wClearFiles.setText( BaseMessages.getString( PKG, "JobTrans.ClearResultFiles.Label" ) );
    FormData fdbClearFiles = new FormData();
    fdbClearFiles.left = new FormAttachment( 0, 0 );
    fdbClearFiles.top = new FormAttachment( wClearRows, 10 );
    wClearFiles.setLayoutData( fdbClearFiles );

    wWaitingToFinish = new Button( gExecution, SWT.CHECK );
    props.setLook( wWaitingToFinish );
    wWaitingToFinish.setText( BaseMessages.getString( PKG, "JobTrans.WaitToFinish.Label" ) );
    FormData fdWait = new FormData();
    fdWait.top = new FormAttachment( wClearFiles, 10 );
    fdWait.left = new FormAttachment( 0, 0 );
    wWaitingToFinish.setLayoutData( fdWait );

    wFollowingAbortRemotely = new Button( gExecution, SWT.CHECK );
    props.setLook( wFollowingAbortRemotely );
    wFollowingAbortRemotely.setText( BaseMessages.getString( PKG, "JobTrans.AbortRemote.Label" ) );
    FormData fdFollow = new FormData();
    fdFollow.top = new FormAttachment( wWaitingToFinish, 10 );
    fdFollow.left = new FormAttachment( 0, 0 );
    wFollowingAbortRemotely.setLayoutData( fdFollow );

    Composite cRunConfiguration = new Composite( wOptions, SWT.NONE );
    cRunConfiguration.setLayout( new FormLayout() );
    props.setLook( cRunConfiguration );
    FormData fdLocal = new FormData();
    fdLocal.top = new FormAttachment( 0 );
    fdLocal.right = new FormAttachment( 100 );
    fdLocal.left = new FormAttachment( 0 );

    cRunConfiguration.setBackground( shell.getBackground() ); // the default looks ugly
    cRunConfiguration.setLayoutData( fdLocal );

    Label wlRunConfiguration = new Label( cRunConfiguration, SWT.LEFT );
    props.setLook( wlRunConfiguration );
    wlRunConfiguration.setText( "Run configuration:" );
    FormData fdlRunConfiguration = new FormData();
    fdlRunConfiguration.top = new FormAttachment( 0 );
    fdlRunConfiguration.left = new FormAttachment( 0 );
    wlRunConfiguration.setLayoutData( fdlRunConfiguration );

    wRunConfiguration = new ComboVar( jobMeta, cRunConfiguration, SWT.BORDER );
    props.setLook( wRunConfiguration );
    FormData fdRunConfiguration = new FormData();
    fdRunConfiguration.width = 200;
    fdRunConfiguration.top = new FormAttachment( wlRunConfiguration, 5 );
    fdRunConfiguration.left = new FormAttachment( 0 );
    wRunConfiguration.setLayoutData( fdRunConfiguration );

    fdgExecution.top = new FormAttachment( cRunConfiguration, 10 );

    wbGetParams.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent arg0 ) {
        getParameters( null ); // force reload from file specification
      }
    } );

    wbBrowse.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        if ( rep != null ) {
          selectTransformation();
        } else {
          pickFileVFS();
        }
      }
    } );

    wbLogFilename.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        selectLogFile( FILE_FILTERLOGNAMES );
      }
    } );
  }

  protected JobEntryBase getJobEntry() {
    return jobEntry;
  }

  protected Image getImage() {
    return SwtSvgImageUtil
      .getImage( shell.getDisplay(), getClass().getClassLoader(), "TRN.svg", ConstUI.ICON_SIZE,
        ConstUI.ICON_SIZE );
  }

  protected boolean getArgFromPrev() {
    return jobEntry.argFromPrevious;
  }

  protected String[] getArguments() {
    return jobEntry.arguments;
  }

  protected String[] getParamters() {
    return jobEntry.parameters;
  }

  private void getParameters( TransMeta inputTransMeta ) {
    try {
      if ( inputTransMeta == null ) {
        JobEntryTrans jet = new JobEntryTrans();
        getInfo( jet );
        inputTransMeta = jet.getTransMeta( rep, metaStore, jobMeta );
      }
      String[] parameters = inputTransMeta.listParameters();

      String[] existing = wParameters.getItems( 1 );

      for ( int i = 0; i < parameters.length; i++ ) {
        if ( Const.indexOfString( parameters[ i ], existing ) < 0 ) {
          TableItem item = new TableItem( wParameters.table, SWT.NONE );
          item.setText( 1, parameters[ i ] );
        }
      }
      wParameters.removeEmptyRows();
      wParameters.setRowNums();
      wParameters.optWidth( true );
    } catch ( Exception e ) {
      new ErrorDialog( shell,
        BaseMessages.getString( PKG, "JobEntryTransDialog.Exception.UnableToLoadTransformation.Title" ),
        BaseMessages.getString( PKG, "JobEntryTransDialog.Exception.UnableToLoadTransformation.Message" ), e );
    }

  }

  private void selectTransformation() {
    RepositoryObject repositoryObject = DialogHelper.selectRepositoryObject( "*.ktr", log );

    if ( repositoryObject != null ) {
      String path = DialogUtils
        .getPath( jobMeta.getRepositoryDirectory().getPath(), repositoryObject.getRepositoryDirectory().getPath() );
      String fullPath = ( path.equals( "/" ) ? "/" : path + "/" ) + repositoryObject.getName();
      wPath.setText( fullPath );
    }
  }

  protected void pickFileVFS() {

    FileDialog dialog = new FileDialog( shell, SWT.OPEN );
    dialog.setFilterExtensions( Const.STRING_TRANS_FILTER_EXT );
    dialog.setFilterNames( Const.getTransformationFilterNames() );
    String prevName = jobMeta.environmentSubstitute( wPath.getText() );
    String parentFolder = null;
    try {
      parentFolder =
        KettleVFS.getFilename( KettleVFS
          .getFileObject( jobMeta.environmentSubstitute( jobMeta.getFilename() ) ).getParent() );
    } catch ( Exception e ) {
      // not that important
    }
    if ( !Utils.isEmpty( prevName ) ) {
      try {
        if ( KettleVFS.fileExists( prevName ) ) {
          dialog.setFilterPath( KettleVFS.getFilename( KettleVFS.getFileObject( prevName ).getParent() ) );
        } else {

          if ( !prevName.endsWith( ".ktr" ) ) {
            prevName = getEntryName( Const.trim( wPath.getText() ) + ".ktr" );
          }
          if ( KettleVFS.fileExists( prevName ) ) {
            specificationMethod = ObjectLocationSpecificationMethod.FILENAME;
            wPath.setText( prevName );
            return;
          } else {
            // File specified doesn't exist. Ask if we should create the file...
            //
            MessageBox mb = new MessageBox( shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION );
            mb.setMessage( BaseMessages.getString( PKG, "JobTrans.Dialog.CreateTransformationQuestion.Message" ) );
            mb.setText( BaseMessages.getString( PKG, "JobTrans.Dialog.CreateTransformationQuestion.Title" ) ); // Sorry!
            int answer = mb.open();
            if ( answer == SWT.YES ) {

              Spoon spoon = Spoon.getInstance();
              spoon.newTransFile();
              TransMeta transMeta = spoon.getActiveTransformation();
              transMeta.initializeVariablesFrom( jobEntry );
              transMeta.setFilename( jobMeta.environmentSubstitute( prevName ) );
              wPath.setText( prevName );
              specificationMethod = ObjectLocationSpecificationMethod.FILENAME;
              spoon.saveFile();
              return;
            }
          }
        }
      } catch ( Exception e ) {
        dialog.setFilterPath( parentFolder );
      }
    } else if ( !Utils.isEmpty( parentFolder ) ) {
      dialog.setFilterPath( parentFolder );
    }

    String fname = dialog.open();
    if ( fname != null ) {
      File file = new File( fname );
      String name = file.getName();
      String parentFolderSelection = file.getParentFile().toString();

      if ( !Utils.isEmpty( parentFolder ) && parentFolder.equals( parentFolderSelection ) ) {
        wPath.setText( getEntryName( name ) );
      } else {
        wPath.setText( fname );
      }

    }
  }

  String getEntryName( String name ) {
    return "${"
      + Const.INTERNAL_VARIABLE_ENTRY_CURRENT_DIRECTORY + "}/" + name;
  }

  public void dispose() {
    WindowProperty winprop = new WindowProperty( shell );
    props.setScreen( winprop );
    shell.dispose();
  }

  public void getData() {
    wName.setText( Const.NVL( jobEntry.getName(), "" ) );

    specificationMethod = jobEntry.getSpecificationMethod();
    switch ( specificationMethod ) {
      case FILENAME:
        wPath.setText( Const.NVL( jobEntry.getFilename(), "" ) );
        break;
      case REPOSITORY_BY_NAME:
        String dirPath = Const.NVL( jobEntry.getDirectory(), "" );
        String transPath = Const.NVL( jobEntry.getTransname(), "" );
        String fullPath = ( StringUtils.isBlank( dirPath ) ? "" : dirPath + "/" ) + transPath;
        wPath.setText( fullPath );
        break;
      case REPOSITORY_BY_REFERENCE:
        referenceObjectId = jobEntry.getTransObjectId();
        if ( rep != null && jobEntry.getTransObjectId() != null ) {
          getByReferenceData( jobEntry.getTransObjectId() );
        }
        break;
      default:
        break;
    }

    // Arguments
    if ( jobEntry.arguments != null ) {
      for ( int i = 0; i < jobEntry.arguments.length; i++ ) {
        TableItem ti = wFields.table.getItem( i );
        if ( jobEntry.arguments[ i ] != null ) {
          ti.setText( 1, jobEntry.arguments[ i ] );
        }
      }
      wFields.setRowNums();
      wFields.optWidth( true );
    }

    // Parameters
    if ( jobEntry.parameters != null ) {
      for ( int i = 0; i < jobEntry.parameters.length; i++ ) {
        TableItem ti = wParameters.table.getItem( i );
        if ( !Utils.isEmpty( jobEntry.parameters[ i ] ) ) {
          ti.setText( 1, Const.NVL( jobEntry.parameters[ i ], "" ) );
          ti.setText( 2, Const.NVL( jobEntry.parameterFieldNames[ i ], "" ) );
          ti.setText( 3, Const.NVL( jobEntry.parameterValues[ i ], "" ) );
        }
      }
      wParameters.setRowNums();
      wParameters.optWidth( true );
    }

    wPassParams.setSelection( jobEntry.isPassingAllParameters() );

    if ( jobEntry.logfile != null ) {
      wLogfile.setText( jobEntry.logfile );
    }
    if ( jobEntry.logext != null ) {
      wLogext.setText( jobEntry.logext );
    }

    wPrevious.setSelection( jobEntry.argFromPrevious );
    wPrevToParams.setSelection( jobEntry.paramsFromPrevious );
    wEveryRow.setSelection( jobEntry.execPerRow );
    wSetLogfile.setSelection( jobEntry.setLogfile );
    wAddDate.setSelection( jobEntry.addDate );
    wAddTime.setSelection( jobEntry.addTime );
    wClearRows.setSelection( jobEntry.clearResultRows );
    wClearFiles.setSelection( jobEntry.clearResultFiles );
    wWaitingToFinish.setSelection( jobEntry.isWaitingToFinish() );
    wFollowingAbortRemotely.setSelection( jobEntry.isFollowingAbortRemotely() );
    wAppendLogfile.setSelection( jobEntry.setAppendLogfile );

    wbLogFilename.setSelection( jobEntry.setAppendLogfile );

    wCreateParentFolder.setSelection( jobEntry.createParentFolder );
    if ( jobEntry.logFileLevel != null ) {
      wLoglevel.select( jobEntry.logFileLevel.getLevel() );
    }

    List<String> runConfigurations = new ArrayList<>();
    try {
      ExtensionPointHandler
        .callExtensionPoint( Spoon.getInstance().getLog(), KettleExtensionPoint.SpoonRunConfiguration.id,
          new Object[] { runConfigurations, TransMeta.XML_TAG } );
    } catch ( KettleException e ) {
      // Ignore errors
    }

    wRunConfiguration.setItems( runConfigurations.toArray( new String[ 0 ] ) );
    if ( Utils.isEmpty( jobEntry.getRunConfiguration() ) ) {
      wRunConfiguration.select( 0 );
    } else {
      wRunConfiguration.setText( jobEntry.getRunConfiguration() );
    }

    wName.selectAll();
    wName.setFocus();
  }

  private void getByReferenceData( ObjectId transObjectId ) {
    try {
      RepositoryObject transInf = rep.getObjectInformation( transObjectId, RepositoryObjectType.TRANSFORMATION );
      String path =
        DialogUtils.getPath( jobMeta.getRepositoryDirectory().getPath(), transInf.getRepositoryDirectory().getPath() );
      String fullPath =
        Const.NVL( path, "" ) + "/" + Const.NVL( transInf.getName(), "" );
      wPath.setText( fullPath );
    } catch ( KettleException e ) {
      new ErrorDialog( shell,
        BaseMessages.getString( PKG, "JobEntryTransDialog.Exception.UnableToReferenceObjectId.Title" ),
        BaseMessages.getString( PKG, "JobEntryTransDialog.Exception.UnableToReferenceObjectId.Message" ), e );
    }
  }

  protected void cancel() {
    jobEntry.setChanged( backupChanged );

    jobEntry = null;
    dispose();
  }

  private void getInfo( JobEntryTrans jet ) throws KettleException {
    jet.setName( wName.getText() );
    if ( rep != null ) {
      specificationMethod = ObjectLocationSpecificationMethod.REPOSITORY_BY_NAME;
    } else {
      specificationMethod = ObjectLocationSpecificationMethod.FILENAME;
    }
    jet.setSpecificationMethod( specificationMethod );
    switch ( specificationMethod ) {
      case FILENAME:
        jet.setFileName( wPath.getText() );
        if ( jet.getFilename().isEmpty() ) {
          throw new KettleException( BaseMessages.getString( PKG,
            "JobTrans.Dialog.Exception.NoValidMappingDetailsFound" ) );
        }

        jet.setDirectory( null );
        jet.setTransname( null );
        jet.setTransObjectId( null );
        break;
      case REPOSITORY_BY_NAME:
        String transPath = wPath.getText();
        String transName = transPath;
        String directory = "";
        int index = transPath.lastIndexOf( "/" );
        if ( index != -1 ) {
          transName = transPath.substring( index + 1 );
          directory = index == 0 ? "/" : transPath.substring( 0, index );
        }
        jet.setDirectory( directory );
        jet.setTransname( transName );
        jet.setFileName( null );
        jet.setTransObjectId( null );
        break;
      default:
        break;
    }

    int nritems = wFields.nrNonEmpty();
    int nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String arg = wFields.getNonEmpty( i ).getText( 1 );
      if ( arg != null && arg.length() != 0 ) {
        nr++;
      }
    }
    jet.arguments = new String[ nr ];
    nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String arg = wFields.getNonEmpty( i ).getText( 1 );
      if ( arg != null && arg.length() != 0 ) {
        jet.arguments[ nr ] = arg;
        nr++;
      }
    }

    // Do the parameters
    nritems = wParameters.nrNonEmpty();
    nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String param = wParameters.getNonEmpty( i ).getText( 1 );
      if ( param != null && param.length() != 0 ) {
        nr++;
      }
    }
    jet.parameters = new String[ nr ];
    jet.parameterFieldNames = new String[ nr ];
    jet.parameterValues = new String[ nr ];
    nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String param = wParameters.getNonEmpty( i ).getText( 1 );
      String fieldName = wParameters.getNonEmpty( i ).getText( 2 );
      String value = wParameters.getNonEmpty( i ).getText( 3 );

      jet.parameters[ nr ] = param;

      if ( !Utils.isEmpty( Const.trim( fieldName ) ) ) {
        jet.parameterFieldNames[ nr ] = fieldName;
      } else {
        jet.parameterFieldNames[ nr ] = "";
      }

      if ( !Utils.isEmpty( Const.trim( value ) ) ) {
        jet.parameterValues[ nr ] = value;
      } else {
        jet.parameterValues[ nr ] = "";
      }

      nr++;
    }

    jet.setPassingAllParameters( wPassParams.getSelection() );

    jet.logfile = wLogfile.getText();
    jet.logext = wLogext.getText();

    if ( wLoglevel.getSelectionIndex() >= 0 ) {
      jet.logFileLevel = LogLevel.values()[ wLoglevel.getSelectionIndex() ];
    } else {
      jet.logFileLevel = LogLevel.BASIC;
    }

    jet.argFromPrevious = wPrevious.getSelection();
    jet.paramsFromPrevious = wPrevToParams.getSelection();
    jet.execPerRow = wEveryRow.getSelection();
    jet.setLogfile = wSetLogfile.getSelection();
    jet.addDate = wAddDate.getSelection();
    jet.addTime = wAddTime.getSelection();
    jet.clearResultRows = wClearRows.getSelection();
    jet.clearResultFiles = wClearFiles.getSelection();
    jet.createParentFolder = wCreateParentFolder.getSelection();
    jet.setRunConfiguration( wRunConfiguration.getText() );
    jet.setAppendLogfile = wAppendLogfile.getSelection();
    jet.setWaitingToFinish( wWaitingToFinish.getSelection() );
    jet.setFollowingAbortRemotely( wFollowingAbortRemotely.getSelection() );

    TransExecutionConfiguration executionConfiguration = new TransExecutionConfiguration();
    executionConfiguration.setRunConfiguration( jet.getRunConfiguration() );
    try {
      ExtensionPointHandler.callExtensionPoint( jobEntry.getLogChannel(), KettleExtensionPoint.SpoonTransBeforeStart.id,
        new Object[] { executionConfiguration, jobMeta, jobMeta, null } );
    } catch ( KettleException e ) {
      // Ignore errors
    }

    try {
      ExtensionPointHandler.callExtensionPoint( jobEntry.getLogChannel(), KettleExtensionPoint.JobEntryTransSave.id,
        new Object[] { jobMeta, jet.getRunConfiguration() } );
    } catch ( KettleException e ) {
      // Ignore errors
    }

    jet.setClustering( executionConfiguration.isExecutingClustered() );
    if ( executionConfiguration.getRemoteServer() != null ) {
      jet.setRemoteSlaveServerName( executionConfiguration.getRemoteServer().getName() );
    }
    jet.setLoggingRemoteWork( executionConfiguration.isLogRemoteExecutionLocally() );
  }

  protected void ok() {
    if ( Utils.isEmpty( wName.getText() ) ) {
      final Dialog dialog = new SimpleMessageDialog( shell,
        BaseMessages.getString( PKG, "System.StepJobEntryNameMissing.Title" ),
        BaseMessages.getString( PKG, "System.JobEntryNameMissing.Msg" ), MessageDialog.ERROR );
      dialog.open();
      return;
    }
    jobEntry.setName( wName.getText() );

    try {
      getInfo( jobEntry );
    } catch ( KettleException e ) {
      // suppress exceptions at this time - we will let the runtime report on any errors
    }
    jobEntry.setChanged();
    dispose();
  }
}
