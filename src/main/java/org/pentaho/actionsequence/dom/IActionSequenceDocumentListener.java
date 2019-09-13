/*
 * Copyright 2002 - 2017 Hitachi Vantara.  All rights reserved.
 * 
 * This software was developed by Hitachi Vantara and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. TThe Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.actionsequence.dom;

/**
 * Classes that implement this interface can be notified of changes to an action sequence.
 * 
 * @author Angelo Rodriguez
 * 
 */
public interface IActionSequenceDocumentListener {

  void ioAdded( IAbstractIOElement io );

  void ioRemoved( Object parent, IAbstractIOElement io );

  void ioRenamed( IAbstractIOElement io );

  void ioChanged( IAbstractIOElement io );

  void resourceAdded( Object resource );

  void resourceRemoved( Object parent, Object resource );

  void resourceRenamed( Object resource );

  void resourceChanged( Object resource );

  void actionAdded( IActionDefinition action );

  void actionRemoved( Object parent, IActionDefinition action );

  void actionRenamed( IActionDefinition action );

  void actionChanged( IActionDefinition action );

  void controlStatementAdded( IActionControlStatement controlStatement );

  void controlStatementRemoved( Object parent, IActionControlStatement controlStatement );

  void controlStatementChanged( IActionControlStatement controlStatement );

  void headerChanged( IActionSequenceDocument actionSequenceDocument );
}
