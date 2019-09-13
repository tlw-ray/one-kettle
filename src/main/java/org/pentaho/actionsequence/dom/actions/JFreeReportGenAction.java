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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;

public class JFreeReportGenAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "com.pentaho.component.JFreeReportGeneratorComponent"; //$NON-NLS-1$
  public static final String RESULT_SET_ELEMENT = "result-set"; //$NON-NLS-1$
  public static final String COMPONENT_SETTINGS_ELEMENT = "component-settings"; //$NON-NLS-1$
  public static final String TEMPLATE_PATH_PROP = "template-path"; //$NON-NLS-1$
  public static final String ORIENTATION_PROP = "orientation"; //$NON-NLS-1$
  public static final String NULL_STRING_PROP = "null-string"; //$NON-NLS-1$
  public static final String HORIZONTAL_OFFSET_PROP = "horizontal-offset"; //$NON-NLS-1$
  public static final String REPORTNAME_PROP = "report-name"; //$NON-NLS-1$
  public static final String CREATE_SUBTOTALS_PROP = "create-sub-totals"; //$NON-NLS-1$
  public static final String CREATE_GRANDTOTALS_PROP = "create-grand-totals"; //$NON-NLS-1$
  public static final String CREATE_ROWBANDING_PROP = "create-row-banding"; //$NON-NLS-1$
  public static final String TOTALCOLUMN_NAME_PROP = "total-column-name"; //$NON-NLS-1$
  public static final String TOTALCOLUMN_WIDTH_PROP = "total-column-width"; //$NON-NLS-1$
  public static final String TOTALCOLUMN_FORMAT_PROP = "total-column-format"; //$NON-NLS-1$
  public static final String ROWBANDING_COLOR_PROP = "row-banding-color"; //$NON-NLS-1$
  public static final String CREATE_TOTALCOLUMN_PROP = "create-total-column"; //$NON-NLS-1$
  public static final String COLUMN_HEADER_BACKGROUND_COLOR_PROP = "column-header-background-color"; //$NON-NLS-1$
  public static final String COLUMN_HEADER_FOREGROUND_COLOR_PROP = "column-header-foreground-color"; //$NON-NLS-1$
  public static final String COLUMN_HEADER_FONT_FACE_PROP = "column-header-font-face"; //$NON-NLS-1$
  public static final String COLUMN_HEADER_FONT_SIZE_PROP = "column-header-font-size"; //$NON-NLS-1$
  public static final String COLUMN_HEADER_GAP_PROP = "column-header-gap"; //$NON-NLS-1$
  public static final String SPACER_WIDTH_PROP = "spacer-width"; //$NON-NLS-1$
  public static final String REPORT_DEFINITION = "report-definition"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { RESULT_SET_ELEMENT, COMPONENT_SETTINGS_ELEMENT };

  public JFreeReportGenAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public JFreeReportGenAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  /*
   * Get the element from the COMPONENT_SETTINGS or ComponentDef section XML part.
   */
  private IActionInput getComponentValue( String elementName ) {
    IActionInput value = getInput( elementName );
    try {
      if ( value == ActionInputConstant.NULL_INPUT ) {
        value = getInput( COMPONENT_SETTINGS_ELEMENT );
        Document doc = DocumentHelper.parseText( value.getStringValue() );
        Node componentNode = doc.getRootElement();
        value =
            new ActionInputConstant( componentNode.selectSingleNode( elementName ).getText(), this.actionParameterMgr );
      }
    } catch ( Exception e ) {
      value = ActionInputConstant.NULL_INPUT;
    }
    return value;
  }

  public void setResultSet( IActionInputSource value ) {
    setActionInputValue( RESULT_SET_ELEMENT, value );
  }

  public IActionInput getResultSet() {
    return getComponentValue( RESULT_SET_ELEMENT );
  }

  public void setComponentSettings( IActionInputSource value ) {
    setActionInputValue( COMPONENT_SETTINGS_ELEMENT, value );
  }

  public IActionInput getComponentSettings() {
    return getInput( COMPONENT_SETTINGS_ELEMENT );
  }

  public void setTemplatePath( IActionInputSource value ) {
    setActionInputValue( TEMPLATE_PATH_PROP, value );
  }

  public IActionInput getTemplatePath() {
    return getComponentValue( TEMPLATE_PATH_PROP );
  }

  public void setOrientation( IActionInputSource value ) {
    setActionInputValue( ORIENTATION_PROP, value );
  }

  public IActionInput getOrientation() {
    return getComponentValue( ORIENTATION_PROP );
  }

  public void setNullString( IActionInputSource value ) {
    setActionInputValue( NULL_STRING_PROP, value );
  }

  public IActionInput getNullString() {
    return getComponentValue( NULL_STRING_PROP );
  }

  public void setHorizontalOffset( IActionInputSource value ) {
    setActionInputValue( HORIZONTAL_OFFSET_PROP, value );
  }

  public IActionInput getHorizontalOffset() {
    return getComponentValue( HORIZONTAL_OFFSET_PROP );
  }

  public void setReportName( IActionInputSource value ) {
    setActionInputValue( REPORTNAME_PROP, value );
  }

  public IActionInput getReportName() {
    return getComponentValue( REPORTNAME_PROP );
  }

  public void setCreateSubTotals( IActionInputSource value ) {
    setActionInputValue( CREATE_SUBTOTALS_PROP, value );
  }

  public IActionInput getCreateSubTotals() {
    return getComponentValue( CREATE_SUBTOTALS_PROP );
  }

  public void setCreateGrandTotals( IActionInputSource value ) {
    setActionInputValue( CREATE_GRANDTOTALS_PROP, value );
  }

  public IActionInput getCreateGrandTotals() {
    return getComponentValue( CREATE_GRANDTOTALS_PROP );
  }

  public void setCreateRowBanding( IActionInputSource value ) {
    setActionInputValue( CREATE_ROWBANDING_PROP, value );
  }

  public IActionInput getCreateRowBanding() {
    return getComponentValue( CREATE_ROWBANDING_PROP );
  }

  public void setTotalColumnName( IActionInputSource value ) {
    setActionInputValue( TOTALCOLUMN_NAME_PROP, value );
  }

  public IActionInput getTotalColumnName() {
    return getComponentValue( CREATE_ROWBANDING_PROP );
  }

  public void setTotalColumnWidth( IActionInputSource value ) {
    setActionInputValue( TOTALCOLUMN_WIDTH_PROP, value );
  }

  public IActionInput getTotalColumnWidth() {
    return getComponentValue( TOTALCOLUMN_WIDTH_PROP );
  }

  public void setTotalColumnFormat( IActionInputSource value ) {
    setActionInputValue( TOTALCOLUMN_FORMAT_PROP, value );
  }

  public IActionInput getTotalColumnFormat() {
    return getComponentValue( TOTALCOLUMN_FORMAT_PROP );
  }

  public void setRowBandingColor( IActionInputSource value ) {
    setActionInputValue( ROWBANDING_COLOR_PROP, value );
  }

  public IActionInput getRowBandingColor() {
    return getComponentValue( ROWBANDING_COLOR_PROP );
  }

  public void setCreateTotalColumn( IActionInputSource value ) {
    setActionInputValue( CREATE_TOTALCOLUMN_PROP, value );
  }

  public IActionInput getCreateTotalColumn() {
    return getComponentValue( CREATE_TOTALCOLUMN_PROP );
  }

  public void setColumnHeaderBackgroundColor( IActionInputSource value ) {
    setActionInputValue( COLUMN_HEADER_BACKGROUND_COLOR_PROP, value );
  }

  public IActionInput getColumnHeaderBackgroundColor() {
    return getComponentValue( COLUMN_HEADER_BACKGROUND_COLOR_PROP );
  }

  public void setColumnHeaderForegroundColor( IActionInputSource value ) {
    setActionInputValue( COLUMN_HEADER_FOREGROUND_COLOR_PROP, value );
  }

  public IActionInput getColumnHeaderForegroundColor() {
    return getComponentValue( COLUMN_HEADER_FOREGROUND_COLOR_PROP );
  }

  public void setColumnHeaderFontFace( IActionInputSource value ) {
    setActionInputValue( COLUMN_HEADER_FONT_FACE_PROP, value );
  }

  public IActionInput getColumnHeaderFontFace() {
    return getComponentValue( COLUMN_HEADER_FONT_FACE_PROP );
  }

  public void setColumnHeaderFontSize( IActionInputSource value ) {
    setActionInputValue( COLUMN_HEADER_FONT_SIZE_PROP, value );
  }

  public IActionInput getColumnHeaderFontSize() {
    return getComponentValue( COLUMN_HEADER_FONT_SIZE_PROP );
  }

  public void setColumnHeaderGap( IActionInputSource value ) {
    setActionInputValue( COLUMN_HEADER_GAP_PROP, value );
  }

  public IActionInput getColumnHeaderGap() {
    return getComponentValue( COLUMN_HEADER_GAP_PROP );
  }

  public void setSpacerWidth( IActionInputSource value ) {
    setActionInputValue( SPACER_WIDTH_PROP, value );
  }

  public IActionInput getSpacerWidth() {
    return getComponentValue( SPACER_WIDTH_PROP );
  }

  public void setOutputReportDefinition( String publicOutputName ) {
    setOutput( REPORT_DEFINITION, publicOutputName, ActionSequenceDocument.CONTENT_TYPE );
  }

  public IActionOutput getOutputReportDefinition() {
    return getOutput( REPORT_DEFINITION );
  }
}
