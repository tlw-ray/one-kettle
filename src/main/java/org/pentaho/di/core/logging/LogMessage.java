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

import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.Const;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.core.util.StringUtil;

public class LogMessage implements LogMessageInterface {
  private String logChannelId;
  private String message;
  private String subject;
  private Object[] arguments;
  private LogLevel level;
  private String copy;

  /**
   * Backward compatibility : no registry used, just log the subject as part of the message
   *
   * @param message
   * @param logChannelId
   */
  public LogMessage( String subject, LogLevel level ) {
    this.subject = subject;
    this.level = level;
    this.message = null;
    this.logChannelId = null;
  }

  /**
   * Recommended use :
   *
   * @param message
   * @param logChannelId
   * @param level
   *          the log level
   */
  public LogMessage( String message, String logChannelId, LogLevel level ) {
    this.message = message;
    this.logChannelId = logChannelId;
    this.level = level;
    lookupSubject();
  }

  public LogMessage( String message, String logChannelId, Object[] arguments, LogLevel level ) {
    this.message = message;
    this.logChannelId = logChannelId;
    this.arguments = arguments;
    this.level = level;
    lookupSubject();
  }

  private void lookupSubject() {
    // Derive the subject from the registry
    //
    LoggingObjectInterface loggingObject = LoggingRegistry.getInstance().getLoggingObject( logChannelId );
    boolean detailedLogTurnOn = "Y".equals(EnvUtil.getSystemProperty(Const.KETTLE_LOG_MARK_MAPPINGS));
    if ( loggingObject != null ) {
      if ( !detailedLogTurnOn ) {
        subject = loggingObject.getObjectName();
      } else {
        subject = getDetailedSubject( loggingObject );
      }
      copy = loggingObject.getObjectCopy();
    }
  }

  /**
   * @param loggingObject
   * @return
   */
  private String getDetailedSubject( LoggingObjectInterface loggingObject ) {

    List<String> subjects = getSubjectTree( loggingObject );
    return subjects.size() > 1 ? formatDetailedSubject( subjects ) : subjects.get( 0 );
  }

  /**
   * @param loggingObject
   */
  private List<String> getSubjectTree( LoggingObjectInterface loggingObject ) {
    List<String> subjects = new ArrayList<String>();
    while ( loggingObject != null ) {
      subjects.add( loggingObject.getObjectName() );
      loggingObject = loggingObject.getParent();
    }
    return subjects;
  }

  /**
   * @param string
   * @param subjects
   * @return
   */
  private String formatDetailedSubject( List<String> subjects ) {

    StringBuilder string = new StringBuilder();

    int currentStep = 0;
    int rootStep = subjects.size() - 1;

    for ( int i = rootStep - 1; i > currentStep; i-- ) {
      string.append( "[" ).append( subjects.get( i ) ).append( "]" ).append( "." );
    }
    string.append( subjects.get( currentStep ) );
    return string.toString();
  }

  @Override
  @Deprecated
  public String toString() {
    if ( StringUtils.isBlank( message ) ) {
      return subject;
    } else if ( StringUtils.isBlank( subject ) ) {
      return getMessage();
    }
    return String.format( "%s - %s", subject, getMessage() );
  }

  @Override
  public LogLevel getLevel() {
    return level;
  }

  @Deprecated
  public void setLevel( LogLevel level ) {
    this.level = level;
  }

  /**
   * @return The formatted message.
   */
  @Override
  public String getMessage() {
    String formatted = message;
    if ( arguments != null ) {
      // get all "tokens" enclosed by curly brackets within the message
      final List<String> tokens = new ArrayList<>();
      StringUtil.getUsedVariables( formatted, "{", "}", tokens, true );
      // perform MessageFormat.format( ... ) on each token, if we get an exception, we'll know that we have a
      // segment that isn't parsable by MessageFormat, likely a pdi variable name (${foo}) - in this case, we need to
      // escape the curly brackets in the message, so that MessageFormat does not complain
      for ( final String token : tokens ) {
        try {
          MessageFormat.format( "{" + token + "}", arguments );
        } catch ( final IllegalArgumentException iar ) {
          formatted = formatted.replaceAll( "\\{" + token + "\\}",  "\\'{'" + token + "\\'}'" );
        }
      }
      // now that we have escaped curly brackets in all invalid tokens, we can attempt to format the entire message
      formatted = MessageFormat.format( formatted, arguments );
    }
    return formatted;
  }

  /**
   * @param message
   *          the message to set
   */
  @Deprecated
  public void setMessage( String message ) {
    this.message = message;
  }

  /**
   * @return the subject
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * @param subject
   *          the subject to set
   */
  @Deprecated
  public void setSubject( String subject ) {
    this.subject = subject;
  }

  /**
   * @return the logChannelId
   */
  @Override
  public String getLogChannelId() {
    return logChannelId;
  }

  /**
   * @param logChannelId
   *          the logChannelId to set
   */
  @Deprecated
  public void setLogChannelId( String logChannelId ) {
    this.logChannelId = logChannelId;
  }

  /**
   * @return the arguments
   */
  @Override
  public Object[] getArguments() {
    return arguments;
  }

  /**
   * @param arguments
   *          the arguments to set
   */
  @Deprecated
  public void setArguments( Object[] arguments ) {
    this.arguments = arguments;
  }

  public boolean isError() {
    return level.isError();
  }

  @Override
  public String getCopy() {
    return copy;
  }

  public void setCopy( String copy ) {
    this.copy = copy;
  }
}
