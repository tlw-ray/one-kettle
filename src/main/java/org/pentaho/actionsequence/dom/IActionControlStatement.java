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

import org.dom4j.Element;

@SuppressWarnings( { "rawtypes" } )
public interface IActionControlStatement extends IActionSequenceExecutableStatement {

  Element getElement();

  /**
   * Adds a new child action definition to the end of this control statements list of children.
   * 
   * @param componentName
   *          the name of the component that processes the action definition
   * @return the newly created action definition
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  IActionDefinition addAction( Class actionDefinitionClass );

  /**
   * Adds a new child action definition to this control statement.
   * 
   * @param componentName
   *          the name of the component that processes the action definition
   * @param index
   *          the index of where to add the new action. If index is greater than the number of children then the new
   *          action is added at the end of the list of children.
   * @return the newly created action definition
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  IActionDefinition addAction( Class actionDefClass, int index );

  /**
   * @return the child actions and control statements
   */
  IActionSequenceExecutableStatement[] getChildren();

  /**
   * @return the control statement that contains this action definition or null if there is no parent control statement.
   */
  IActionControlStatement getParent();

  /**
   * Adds an action definition to the end of this control statements list of children. This control statement becomes
   * the new parent of the action definition.
   * 
   * @param actionDef
   *          the action definition to be added.
   */
  void add( IActionDefinition actionDef );

  /**
   * Adds a new child action definition to this control statement.
   * 
   * @param actionDef
   *          the action definition to be added.
   * @param index
   *          the index of where to add the new action. If index is greater than the number of children then the new
   *          action is added at the end of the list of children.
   */
  void add( IActionDefinition actionDef, int index );

  /**
   * Adds a control statement to the end of this control statments list of children. This control statement becomes the
   * new parent of the specified control statement.
   * 
   * @param controlStatement
   *          the control statment to be added.
   */
  void add( IActionControlStatement controlStatement );

  /**
   * Adds a control statement to this conrtol statement's list of children.
   * 
   * @param controlStatement
   *          the control statement to be added.
   * @param index
   *          the index of where to add the new control statement. If index is greater than the number of children then
   *          the new control statement is added at the end of the list of children.
   */
  void add( IActionControlStatement controlStatement, int index );

  /**
   * Creates a new child action loop to the end of this control statement's children.
   * 
   * @param loopOn
   *          the loop on variable name
   */
  IActionLoop addLoop( String loopOn );

  /**
   * Creates a new child action loop.
   * 
   * @param loopOn
   *          the loop on variable name
   * @param index
   *          the index where the new loop should be created. If index is greater than the number of children then the
   *          new loop is added at the end of the list of children.
   */
  IActionLoop addLoop( String loopOn, int index );

  /**
   * Creates a new child if statement to the end of this control statement's children.
   * 
   * @param condition
   *          the if condition
   */
  IActionIfStatement addIf( String condition );

  /**
   * Creates a new child if statement.
   * 
   * @param condition
   *          the if condition
   * @param index
   *          the index where the new if statement should be created. If index is greater than the number of children
   *          then the new if statement is added at the end of the list of children.
   */
  IActionIfStatement addIf( String condition, int index );

  /**
   * Returns the list of ActionSequenceInputs and ActionOutputs that are available as inputs to this control statement.
   * 
   * @param types
   *          the desired input type
   * @return the list of available inputs
   */
  IActionInputVariable[] getAvailInputVariables();

  /**
   * Returns the list of action definitions that precede this control statement in the action sequence.
   * 
   * @param types
   *          the desired input type
   * @return the preceding acton defintions.
   */
  IActionDefinition[] getPrecedingActionDefinitions();

  IActionSequenceExecutableStatement[] getPrecedingExecutableStatements();

  IActionSequenceValidationError[] validate();

  IActionSequenceValidationError[] validate( boolean validateDescendants );

  Element getControlElement();

}
