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

package org.pentaho.di.kitchen;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.pentaho.di.base.CommandExecutorCodes;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.logging.FileLoggingEventListener;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.parameters.NamedParams;
import org.pentaho.di.core.parameters.NamedParamsDefault;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.core.util.ExecutorUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.Job;
import org.pentaho.di.metastore.MetaStoreConst;
import org.pentaho.di.pan.CommandLineOption;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.metastore.stores.delegate.DelegatingMetaStore;


public class Kitchen {
  private static Class<?> PKG = Kitchen.class; // for i18n purposes, needed by Translator2!!

  public static final String STRING_KITCHEN = "Kitchen";

  private static KitchenCommandExecutor commandExecutor;

  private static FileLoggingEventListener fileAppender;

  public static void main( String[] a ) throws Exception {
    final ExecutorService executor = ExecutorUtil.getExecutor();
    final RepositoryPluginType repositoryPluginType = RepositoryPluginType.getInstance();

    final Future<Map.Entry<KettlePluginException, Future<KettleException>>> repositoryRegisterFuture =
      executor.submit( new Callable<Map.Entry<KettlePluginException, Future<KettleException>>>() {

        @Override
        public Map.Entry<KettlePluginException, Future<KettleException>> call() throws Exception {
          PluginRegistry.addPluginType( repositoryPluginType );
          try {
            KettleClientEnvironment.getInstance().setClient( KettleClientEnvironment.ClientType.KITCHEN );
            KettleClientEnvironment.init();
          } catch ( KettlePluginException e ) {
            return new AbstractMap.SimpleImmutableEntry<KettlePluginException, Future<KettleException>>( e, null );
          }

          Future<KettleException> kettleEnvironmentInitFuture =
            executor.submit( new Callable<KettleException>() {

              @Override
              public KettleException call() throws Exception {
                try {
                  KettleClientEnvironment.getInstance().setClient( KettleClientEnvironment.ClientType.KITCHEN );
                  KettleEnvironment.init();
                } catch ( KettleException e ) {
                  return e;
                }
                return null;
              }
            } );
          return new AbstractMap.SimpleImmutableEntry<KettlePluginException, Future<KettleException>>( null, kettleEnvironmentInitFuture );
        }
      } );

    List<String> args = new ArrayList<String>();
    for ( int i = 0; i < a.length; i++ ) {
      if ( a[i].length() > 0 ) {
        args.add( a[i] );
      }
    }

    DelegatingMetaStore metaStore = new DelegatingMetaStore();
    metaStore.addMetaStore( MetaStoreConst.openLocalPentahoMetaStore() );
    metaStore.setActiveMetaStoreName( metaStore.getName() );

    RepositoryMeta repositoryMeta = null;
    Job job = null;

    StringBuilder optionRepname, optionUsername, optionTrustUser, optionPassword, optionJobname, optionDirname, initialDir;
    StringBuilder optionFilename, optionLoglevel, optionLogfile, optionLogfileOld, optionListdir;
    StringBuilder optionListjobs, optionListrep, optionNorep, optionVersion, optionListParam, optionExport;
    NamedParams optionParams = new NamedParamsDefault();
    NamedParams customOptions = new NamedParamsDefault();

    CommandLineOption maxLogLinesOption =
      new CommandLineOption(
        "maxloglines", BaseMessages.getString( PKG, "Kitchen.CmdLine.MaxLogLines" ), new StringBuilder() );
    CommandLineOption maxLogTimeoutOption =
      new CommandLineOption(
        "maxlogtimeout", BaseMessages.getString( PKG, "Kitchen.CmdLine.MaxLogTimeout" ), new StringBuilder() );

    CommandLineOption[] options =
      new CommandLineOption[]{
        new CommandLineOption( "rep", BaseMessages.getString( PKG, "Kitchen.CmdLine.RepName" ), optionRepname =
          new StringBuilder() ),
        new CommandLineOption(
          "user", BaseMessages.getString( PKG, "Kitchen.CmdLine.RepUsername" ), optionUsername =
          new StringBuilder() ),
        new CommandLineOption(
          "trustuser", BaseMessages.getString( PKG, "Kitchen.ComdLine.RepUsername" ), optionTrustUser =
          new StringBuilder() ),
        new CommandLineOption(
          "pass", BaseMessages.getString( PKG, "Kitchen.CmdLine.RepPassword" ), optionPassword =
          new StringBuilder() ),
        new CommandLineOption(
          "job", BaseMessages.getString( PKG, "Kitchen.CmdLine.RepJobName" ), optionJobname =
          new StringBuilder() ),
        new CommandLineOption( "dir", BaseMessages.getString( PKG, "Kitchen.CmdLine.RepDir" ), optionDirname =
          new StringBuilder() ),
        new CommandLineOption(
          "file", BaseMessages.getString( PKG, "Kitchen.CmdLine.XMLJob" ), optionFilename =
          new StringBuilder() ),
        new CommandLineOption(
          "level", BaseMessages.getString( PKG, "Kitchen.CmdLine.LogLevel" ), optionLoglevel =
          new StringBuilder() ),
        new CommandLineOption(
          "logfile", BaseMessages.getString( PKG, "Kitchen.CmdLine.LogFile" ), optionLogfile =
          new StringBuilder() ),
        new CommandLineOption(
          "log", BaseMessages.getString( PKG, "Kitchen.CmdLine.LogFileOld" ), optionLogfileOld =
          new StringBuilder(), false, true ),
        new CommandLineOption(
          "listdir", BaseMessages.getString( PKG, "Kitchen.CmdLine.ListDir" ), optionListdir =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "listjobs", BaseMessages.getString( PKG, "Kitchen.CmdLine.ListJobsDir" ), optionListjobs =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "listrep", BaseMessages.getString( PKG, "Kitchen.CmdLine.ListAvailableReps" ), optionListrep =
          new StringBuilder(), true, false ),
        new CommandLineOption( "norep", BaseMessages.getString( PKG, "Kitchen.CmdLine.NoRep" ), optionNorep =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "version", BaseMessages.getString( PKG, "Kitchen.CmdLine.Version" ), optionVersion =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "param", BaseMessages.getString( PKG, "Kitchen.ComdLine.Param" ), optionParams, false ),
        new CommandLineOption(
          "listparam", BaseMessages.getString( PKG, "Kitchen.ComdLine.ListParam" ), optionListParam =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "export", BaseMessages.getString( PKG, "Kitchen.ComdLine.Export" ), optionExport =
          new StringBuilder(), true, false ),
        new CommandLineOption(
          "initialDir", null, initialDir =
          new StringBuilder(), false, true ),
        new CommandLineOption(
          "custom", BaseMessages.getString( PKG, "Kitchen.ComdLine.Custom" ), customOptions, false ),
        maxLogLinesOption, maxLogTimeoutOption, };

    if ( args.size() == 2 ) { // 2 internal hidden argument (flag and value)
      CommandLineOption.printUsage( options );
      exitJVM( 9 );
    }

    LogChannelInterface log = new LogChannel( STRING_KITCHEN );

    CommandLineOption.parseArguments( args, options, log );

    configureLogging( maxLogLinesOption, maxLogTimeoutOption );

    String kettleRepname = Const.getEnvironmentVariable( "KETTLE_REPOSITORY", null );
    String kettleUsername = Const.getEnvironmentVariable( "KETTLE_USER", null );
    String kettlePassword = Const.getEnvironmentVariable( "KETTLE_PASSWORD", null );

    if ( !Utils.isEmpty( kettleRepname ) ) {
      optionRepname = new StringBuilder( kettleRepname );
    }
    if ( !Utils.isEmpty( kettleUsername ) ) {
      optionUsername = new StringBuilder( kettleUsername );
    }
    if ( !Utils.isEmpty( kettlePassword ) ) {
      optionPassword = new StringBuilder( kettlePassword );
    }

    if ( Utils.isEmpty( optionLogfile ) && !Utils.isEmpty( optionLogfileOld ) ) {
      // if the old style of logging name is filled in, and the new one is not
      // overwrite the new by the old
      optionLogfile = optionLogfileOld;
    }

    Map.Entry<KettlePluginException, Future<KettleException>> repositoryRegisterResults =
      repositoryRegisterFuture.get();
    // It's a singleton map with one key-value pair (a Pair collection)
    KettlePluginException repositoryRegisterException = repositoryRegisterResults.getKey();
    if ( repositoryRegisterException != null ) {
      throw repositoryRegisterException;
    }
    Future<KettleException> kettleInitFuture = repositoryRegisterResults.getValue();

    if ( !Utils.isEmpty( optionLogfile ) ) {
      fileAppender = new FileLoggingEventListener( optionLogfile.toString(), true );
      KettleLogStore.getAppender().addLoggingEventListener( fileAppender );
    } else {
      fileAppender = null;
    }

    if ( !Utils.isEmpty( optionLoglevel ) ) {
      log.setLogLevel( LogLevel.getLogLevelForCode( optionLoglevel.toString() ) );
      log.logMinimal( BaseMessages.getString( PKG, "Kitchen.Log.LogLevel", log.getLogLevel().getDescription() ) );
    }

    // Start the action...
    //
    int returnCode = CommandExecutorCodes.Kitchen.SUCCESS.getCode();

    try {

      if ( getCommandExecutor() == null ) {
        setCommandExecutor( new KitchenCommandExecutor( PKG, log, kettleInitFuture ) ); // init
      }

      if ( !Utils.isEmpty( optionVersion ) ) {
        getCommandExecutor().printVersion();
        if ( a.length == 1 ) {
          exitJVM( CommandExecutorCodes.Pan.KETTLE_VERSION_PRINT.getCode() );
        }
      }

      returnCode = getCommandExecutor().execute( optionRepname.toString(), optionNorep.toString(), optionUsername.toString(),
            optionTrustUser.toString(), optionPassword.toString(), optionDirname.toString(), optionFilename.toString(), optionJobname.toString(), optionListjobs.toString(),
            optionListdir.toString(), optionExport.toString(), initialDir.toString(), optionListrep.toString(), optionListParam.toString(),
            optionParams, customOptions, args.toArray( new String[ args.size() ] ) );

    } catch ( Throwable t ) {
      t.printStackTrace();
      returnCode = CommandExecutorCodes.Pan.UNEXPECTED_ERROR.getCode();

    } finally {
      if ( fileAppender != null ) {
        fileAppender.close();
        KettleLogStore.getAppender().removeLoggingEventListener( fileAppender );
      }
    }

    exitJVM( returnCode );

  }

  private static <T extends Throwable> void blockAndThrow( Future<T> future ) throws T {
    try {
      T e = future.get();
      if ( e != null ) {
        throw e;
      }
    } catch ( InterruptedException e ) {
      throw new RuntimeException( e );
    } catch ( ExecutionException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * Configure the central log store from the provided command line options
   *
   * @param maxLogLinesOption   Option for maximum log lines
   * @param maxLogTimeoutOption Option for log timeout
   * @throws KettleException Error parsing command line arguments
   */
  public static void configureLogging( final CommandLineOption maxLogLinesOption,
                                       final CommandLineOption maxLogTimeoutOption ) throws KettleException {
    int maxLogLines = parseIntArgument( maxLogLinesOption, 0 );
    if ( Utils.isEmpty( maxLogLinesOption.getArgument() ) ) {
      maxLogLines = Const.toInt( EnvUtil.getSystemProperty( Const.KETTLE_MAX_LOG_SIZE_IN_LINES ), 5000 );
    }
    int maxLogTimeout = parseIntArgument( maxLogTimeoutOption, 0 );
    if ( Utils.isEmpty( maxLogTimeoutOption.getArgument() ) ) {
      maxLogTimeout = Const.toInt( EnvUtil.getSystemProperty( Const.KETTLE_MAX_LOG_TIMEOUT_IN_MINUTES ), 1440 );
    }
    KettleLogStore.init( maxLogLines, maxLogTimeout );
  }

  /**
   * Parse an argument as an integer.
   *
   * @param option Command Line Option to parse argument of
   * @param def    Default if the argument is not set
   * @return The parsed argument or the default if the argument was not specified
   * @throws KettleException Error parsing provided argument as an integer
   */
  protected static int parseIntArgument( final CommandLineOption option, final int def ) throws KettleException {
    if ( !Utils.isEmpty( option.getArgument() ) ) {
      try {
        return Integer.parseInt( option.getArgument().toString() );
      } catch ( NumberFormatException ex ) {
        throw new KettleException( BaseMessages.getString( PKG, "Kitchen.Error.InvalidNumberArgument", option
          .getOption(), option.getArgument() ) );
      }
    }
    return def;
  }

  private static final void exitJVM( int status ) {

    System.exit( status );
  }

  public static KitchenCommandExecutor getCommandExecutor() {
    return commandExecutor;
  }

  public static void setCommandExecutor( KitchenCommandExecutor commandExecutor ) {
    Kitchen.commandExecutor = commandExecutor;
  }
}
