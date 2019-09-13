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

package org.pentaho.di.core.logging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.slf4j.Logger;

import java.util.function.Function;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.pentaho.di.core.logging.LogLevel.BASIC;
import static org.pentaho.di.core.logging.LogLevel.ERROR;


@RunWith ( MockitoJUnitRunner.class )
public class Slf4jLoggingEventListenerTest {

  @Mock private Logger transLogger, jobLogger, diLogger;
  @Mock private KettleLoggingEvent logEvent;
  @Mock private LoggingObjectInterface loggingObject;
  @Mock private LogMessage message;
  @Mock private Function<String, LoggingObjectInterface> logObjProvider;

  private String logChannelId = "logChannelId";
  private String msgText = "message";
  private String messageSub = "subject";
  private LogLevel logLevel = BASIC;


  private Slf4jLoggingEventListener listener = new Slf4jLoggingEventListener();

  @Before
  public void before() {
    listener.transLogger = transLogger;
    listener.jobLogger = jobLogger;
    listener.diLogger = diLogger;
    listener.logObjProvider = logObjProvider;
    when( logEvent.getMessage() ).thenReturn( message );
    when( message.getLogChannelId() ).thenReturn( logChannelId );
    when( message.getLevel() ).thenReturn( logLevel );
    when( message.getMessage() ).thenReturn( msgText );
    when( message.getSubject() ).thenReturn( messageSub );
  }

  @Test
  public void testAddLogEventNoRegisteredLogObject() {
    listener.eventAdded( logEvent );
    verify( diLogger ).info( messageSub + " " + msgText );

    when( message.getLevel() ).thenReturn( ERROR );
    listener.eventAdded( logEvent );
    verify( diLogger ).error( messageSub + " " + msgText );
    verifyZeroInteractions( transLogger );
    verifyZeroInteractions( jobLogger );
  }

  @Test
  public void testAddLogEventTrans() {
    when( logObjProvider.apply( logChannelId ) ).thenReturn( loggingObject );
    when( loggingObject.getLogChannelId() ).thenReturn( logChannelId );
    when( loggingObject.getObjectType() ).thenReturn( LoggingObjectType.TRANS );
    when( loggingObject.getFilename() ).thenReturn( "filename" );
    when( message.getLevel() ).thenReturn( LogLevel.BASIC );
    listener.eventAdded( logEvent );


    verify( transLogger ).info( "[filename]  " + msgText );
    when( message.getLevel() ).thenReturn( LogLevel.ERROR );
    listener.eventAdded( logEvent );
    verify( transLogger ).error( "[filename]  " + msgText );
    verifyZeroInteractions( diLogger );
    verifyZeroInteractions( jobLogger );
  }

  @Test
  public void testAddLogEventJob() {
    when( logObjProvider.apply( logChannelId ) ).thenReturn( loggingObject );
    when( loggingObject.getLogChannelId() ).thenReturn( logChannelId );
    when( loggingObject.getObjectType() ).thenReturn( LoggingObjectType.JOB );
    when( loggingObject.getFilename() ).thenReturn( "filename" );
    when( message.getLevel() ).thenReturn( LogLevel.BASIC );
    listener.eventAdded( logEvent );


    verify( jobLogger ).info( "[filename]  " + msgText );

    when( message.getLevel() ).thenReturn( LogLevel.ERROR );
    listener.eventAdded( logEvent );
    verify( jobLogger ).error( "[filename]  " + msgText );
    verifyZeroInteractions( diLogger );
    verifyZeroInteractions( transLogger );
  }

}
