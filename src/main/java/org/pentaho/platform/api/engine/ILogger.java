/*!
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 *
 * Copyright (c) 2002-2018 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.platform.api.engine;

/**
 * The Logger is the main interface into the platform's logging subsystem.
 * <p>
 * Note: Documentation taken from <a href="http://logging.apache.org/log4j/docs/api/index.html"
 * target="_blank">Log4j Javadoc documentation.</a>
 */
public interface ILogger {

  /**
   * The TRACE has the lowest possible rank and is intended to turn on all logging.
   */
  int TRACE = 1;

  /**
   * The DEBUG Level designates fine-grained informational events that are most useful to debug an application.
   */
  int DEBUG = 2;

  /**
   * The INFO level designates informational messages that highlight the progress of the application at
   * coarse-grained level.
   */
  int INFO = 3;

  /**
   * The WARN level designates potentially harmful situations.
   */
  int WARN = 4;

  /**
   * The ERROR level designates error events that might still allow the application to continue running.
   */
  int ERROR = 5;

  /**
   * The FATAL level designates very severe error events that will presumably lead the application to abort.
   */
  int FATAL = 6;

  int UNKNOWN = 100;

  String SOLUTION_LOG = "solution"; //$NON-NLS-1$

  String ACTIVITY_LOG = "activity"; //$NON-NLS-1$

  String INSTANCE_LOG = "instance"; //$NON-NLS-1$

  String SESSION_LOG = "session"; //$NON-NLS-1$

  /**
   * Return the logging level for this Logger.
   * 
   * @return logging level
   */
  int getLoggingLevel();

  /**
   * Set the logging level for this Logger.
   * <p>
   * Valid logging levels are {@link #TRACE TRACE}, {@link #DEBUG DEBUG}, {@link #INFO INFO}, {@link #WARN WARN},
   * {@link #ERROR ERROR}, and {@link #FATAL FATAL}.
   * 
   * @param loggingLevel
   */
  void setLoggingLevel(int loggingLevel);

  /**
   * Log a message object with the {@link #TRACE TRACE} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void trace(String message);

  /**
   * Log a message object with the {@link #DEBUG DEBUG} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void debug(String message);

  /**
   * Log a message object with the {@link #INFO INFO} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void info(String message);

  /**
   * Log a message object with the {@link #WARN WARN} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void warn(String message);

  /**
   * Log a message object with the {@link #ERROR ERROR} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void error(String message);

  /**
   * Log a message object with the {@link #FATAL FATAL} Level.
   * 
   * @param message
   *          the message object to log.
   */
  void fatal(String message);

  /**
   * Log a message with the {@link #TRACE TRACE} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void trace(String message, Throwable error);

  /**
   * Log a message with the {@link #DEBUG DEBUG} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void debug(String message, Throwable error);

  /**
   * Log a message with the {@link #INFO INFO} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void info(String message, Throwable error);

  /**
   * Log a message with the {@link #WARN WARN} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void warn(String message, Throwable error);

  /**
   * Log a message with the {@link #ERROR ERROR} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void error(String message, Throwable error);

  /**
   * Log a message with the {@link #FATAL FATAL} level including the stack trace of the Throwable error passed as
   * parameter.
   * 
   * @param message
   *          the message object to log.
   * @param error
   *          the exception to log, including its stack trace.
   */
  void fatal(String message, Throwable error);

}
