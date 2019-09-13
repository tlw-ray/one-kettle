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

package org.pentaho.actionsequence.dom.actions;

import java.util.ArrayList;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceValidationError;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionInputVariable;
import org.pentaho.actionsequence.dom.IActionSequenceValidationError;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public abstract class AbstractChartAction extends ActionDefinition {

  public static final String CHART_TYPE_XPATH = "chart-attributes/chart-type"; //$NON-NLS-1$
  public static final String COMPONENT_NAME = "org.pentaho.component.ChartComponent"; //$NON-NLS-1$

  public static final String CHART_ATTRIBUTES_ELEMENT = "chart-attributes"; //$NON-NLS-1$
  public static final String CHART_DATA_ELEMENT = "chart-data"; //$NON-NLS-1$
  public static final String CHART_WIDTH_ELEMENT = "width"; //$NON-NLS-1$
  public static final String CHART_HEIGHT_ELEMENT = "height"; //$NON-NLS-1$
  public static final String CHART_TITLE_ELEMENT = "title"; //$NON-NLS-1$
  public static final String CHART_SUBTITLE_ELEMENT = "subtitle"; //$NON-NLS-1$
  public static final String CHART_FONT_ELEMENT = "font-family"; //$NON-NLS-1$
  public static final String CHART_FONT_SIZE_ELEMENT = "size"; //$NON-NLS-1$
  public static final String CHART_BY_ROW_ELEMENT = "by-row"; //$NON-NLS-1$
  public static final String CHART_PAINT_BORDER_ELEMENT = "border-paint"; //$NON-NLS-1$
  public static final String CHART_SUBTITLE_XPATH = CHART_ATTRIBUTES_ELEMENT + "/" + CHART_SUBTITLE_ELEMENT; //$NON-NLS-1$
  public static final String CHART_BORDER_COLOR_XPATH = CHART_ATTRIBUTES_ELEMENT + "/" + CHART_PAINT_BORDER_ELEMENT; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_XPATH = "chart-attributes/title-font"; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_BOLD_ELEMENT = "is-bold"; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_ITALIC_ELEMENT = "is-italic"; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_BOLD_XPATH = CHART_TITLE_FONT_XPATH + "/" + CHART_TITLE_FONT_BOLD_ELEMENT; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_ITALIC_XPATH = CHART_TITLE_FONT_XPATH
      + "/" + CHART_TITLE_FONT_ITALIC_ELEMENT; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_FAMILY_XPATH = CHART_TITLE_FONT_XPATH + "/" + CHART_FONT_ELEMENT; //$NON-NLS-1$
  public static final String CHART_TITLE_FONT_SIZE_XPATH = CHART_TITLE_FONT_XPATH + "/" + CHART_FONT_SIZE_ELEMENT; //$NON-NLS-1$
  public static final String CHART_BORDER_VISIBLE_ELEMENT = "border-visible"; //$NON-NLS-1$
  public static final String CHART_BORDER_VISIBLE_XPATH = "chart-attributes/" + CHART_BORDER_VISIBLE_ELEMENT; //$NON-NLS-1$
  public static final String CHART_FONT_SIZE = "font-size"; //$NON-NLS-1$
  public static final String CHART_TITLE_ITALIC = "title-italic"; //$NON-NLS-1$
  public static final String CHART_TITLE_BOLD = "title-bold"; //$NON-NLS-1$

  public static final String[] EXPECTED_INPUTS = new String[] { CHART_DATA_ELEMENT, CHART_WIDTH_ELEMENT,
    CHART_HEIGHT_ELEMENT, CHART_TITLE_ELEMENT, CHART_SUBTITLE_ELEMENT, CHART_FONT_ELEMENT, CHART_FONT_SIZE_ELEMENT,
    CHART_BY_ROW_ELEMENT, CHART_PAINT_BORDER_ELEMENT };

  public AbstractChartAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public AbstractChartAction( String componentName ) {
    super( componentName );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public void setChartData( IActionInputVariable value ) {
    setActionInputValue( CHART_DATA_ELEMENT, value );
  }

  public IActionInput getChartData() {
    return getInput( CHART_DATA_ELEMENT );
  }

  public void setWidth( IActionInputSource value ) {
    setActionInputValue( CHART_WIDTH_ELEMENT, value );
  }

  public IActionInput getWidth() {
    return getInput( CHART_WIDTH_ELEMENT );
  }

  public void setHeight( IActionInputSource value ) {
    setActionInputValue( CHART_HEIGHT_ELEMENT, value );
  }

  public IActionInput getHeight() {
    return getInput( CHART_HEIGHT_ELEMENT );
  }

  public void setTitle( IActionInputSource value ) {
    setActionInputValue( CHART_TITLE_ELEMENT, value );
  }

  public IActionInput getTitle() {
    return getInput( CHART_TITLE_ELEMENT );
  }

  public void setTitleBold( ActionInputConstant value ) {
    setComponentDefinition( CHART_TITLE_FONT_BOLD_XPATH, value != null ? Boolean.toString( value
        .getBooleanValue( false ) ) : "false" ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getTitleBold() {
    String value = getComponentDefinitionValue( CHART_TITLE_FONT_BOLD_XPATH );
    return value != null ? new ActionInputConstant( new Boolean( value ), this.actionParameterMgr )
        : IActionInput.NULL_INPUT;
  }

  public void setTitleItalic( ActionInputConstant value ) {
    setComponentDefinition( CHART_TITLE_FONT_ITALIC_XPATH, value != null ? Boolean.toString( value
        .getBooleanValue( false ) ) : "false" ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getTitleItalic() {
    String value = getComponentDefinitionValue( CHART_TITLE_FONT_ITALIC_XPATH );
    return value != null ? new ActionInputConstant( new Boolean( value ), this.actionParameterMgr )
        : IActionInput.NULL_INPUT;
  }

  public void setByRow( IActionInputSource value ) {
    setActionInputValue( CHART_BY_ROW_ELEMENT, value );
  }

  public IActionInput getByRow() {
    return getInput( CHART_BY_ROW_ELEMENT );
  }

  public void setBorderVisible( ActionInputConstant value ) {
    setComponentDefinition( CHART_BORDER_VISIBLE_XPATH, value != null ? Boolean
        .toString( value.getBooleanValue( false ) ) : "false" ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getBorderVisible() {
    String value = getComponentDefinitionValue( CHART_BORDER_VISIBLE_XPATH );
    return value != null ? new ActionInputConstant( new Boolean( value ), this.actionParameterMgr )
        : IActionInput.NULL_INPUT;
  }

  public void setFontFamily( ActionInputConstant value ) {
    setComponentDefinition( CHART_TITLE_FONT_FAMILY_XPATH, value != null ? value.getStringValue() : null ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getFontFamily() {
    String value = getComponentDefinitionValue( CHART_TITLE_FONT_FAMILY_XPATH );
    return value != null ? new ActionInputConstant( value, this.actionParameterMgr ) : IActionInput.NULL_INPUT;
  }

  public void setSubtitle( ActionInputConstant value ) {
    setComponentDefinition( CHART_SUBTITLE_XPATH, value != null ? value.getStringValue() : null ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getSubtitle() {
    String value = getComponentDefinitionValue( CHART_SUBTITLE_XPATH );
    return value != null ? new ActionInputConstant( value, this.actionParameterMgr ) : IActionInput.NULL_INPUT;
  }

  public void setFontSize( ActionInputConstant value ) {
    setComponentDefinition( CHART_TITLE_FONT_SIZE_XPATH, value != null ? value.getStringValue() : null ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getFontSize() {
    String value = getComponentDefinitionValue( CHART_SUBTITLE_XPATH );
    return value != null ? new ActionInputConstant( value, this.actionParameterMgr ) : IActionInput.NULL_INPUT;
  }

  public void setBorderPaint( ActionInputConstant value ) {
    setComponentDefinition( CHART_BORDER_COLOR_XPATH, value != null ? value.getStringValue() : null ); //$NON-NLS-1$//$NON-NLS-2$
  }

  public ActionInputConstant getBorderPaint() {
    String value = getComponentDefinitionValue( CHART_SUBTITLE_XPATH );
    return value != null ? new ActionInputConstant( value, this.actionParameterMgr ) : IActionInput.NULL_INPUT;
  }

  public void setChartType( String value ) {
    setComponentDefinition( CHART_TYPE_XPATH, value );
  }

  public String getChartType() {
    return getComponentDefinitionValue( CHART_TYPE_XPATH );
  }

  public IActionSequenceValidationError[] validate() {
    ArrayList errors = new ArrayList();
    ActionSequenceValidationError validationError = validateInput( CHART_DATA_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing chart data input parameter.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Chart data input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Chart data is uninitialized.";
          break;
      }
      errors.add( validationError );
    }

    validationError = validateResource( CHART_ATTRIBUTES_ELEMENT );
    if ( validationError != null ) {
      switch ( validationError.errorCode ) {
        case ActionSequenceValidationError.INPUT_MISSING:
          validationError.errorMsg = "Missing chart attributes input parameter.";
          break;
        case ActionSequenceValidationError.INPUT_REFERENCES_UNKNOWN_VAR:
          validationError.errorMsg = "Chart attributes input parameter references unknown variable.";
          break;
        case ActionSequenceValidationError.INPUT_UNINITIALIZED:
          validationError.errorMsg = "Chart attributes are uninitialized.";
          break;
      }
      errors.add( validationError );
    }
    return (ActionSequenceValidationError[]) errors.toArray( new ActionSequenceValidationError[0] );
  }
}
