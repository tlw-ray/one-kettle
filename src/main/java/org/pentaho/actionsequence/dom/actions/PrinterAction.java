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

import java.net.URI;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;
import org.pentaho.actionsequence.dom.IActionOutput;
import org.pentaho.actionsequence.dom.IActionResource;

public class PrinterAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.PrintComponent"; //$NON-NLS-1$
  public static final String PRINTER_NAME = "printer-name"; //$NON-NLS-1$
  public static final String COPIES_ELEMENT = "copies"; //$NON-NLS-1$
  public static final String PRINT_FILE = "printFile"; //$NON-NLS-1$
  public static final String FILE_TO_PRINT = "file-to-print"; //$NON-NLS-1$
  public static final String REPORT_OUTPUT = "report-output"; //$NON-NLS-1$
  public static final String DEFAULT_PRINTER = "default-printer"; // Parameter name uset to store the last printer selected //$NON-NLS-1$

  protected static final String[] EXPECTED_RESOURCES = new String[] { PRINT_FILE };

  protected static final String[] EXPECTED_INPUTS = new String[] { PRINTER_NAME, COPIES_ELEMENT, PRINT_FILE };

  public PrinterAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public PrinterAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  public void setPrintfile( IActionInputSource value ) {
    setActionInputValue( PRINT_FILE, value );
  }

  public IActionInput getPrintfile() {
    return getInput( PRINT_FILE );
  }

  public IActionResource setResourcesPrintFile( URI uri, String mimeType ) {
    return setResourceUri( PRINT_FILE, uri, mimeType );
  }

  public IActionResource getResourcesPrintFile() {
    return getResource( PRINT_FILE );
  }

  public void setCopies( IActionInputSource value ) {
    setActionInputValue( COPIES_ELEMENT, value );
  }

  public IActionInput getCopies() {
    return getInput( COPIES_ELEMENT );
  }

  public void setPrinterName( IActionInputSource value ) {
    setActionInputValue( PRINTER_NAME, value );
  }

  public IActionInput getPrinterName() {
    return getInput( PRINTER_NAME );
  }

  public void setReportOutput( IActionInputSource value ) {
    setActionInputValue( REPORT_OUTPUT, value );
  }

  public IActionInput getReportOutput() {
    return getInput( REPORT_OUTPUT );
  }

  public void setDefaultPrinter( IActionInputSource value ) {
    setActionInputValue( DEFAULT_PRINTER, value );
  }

  public IActionInput getDefaultPrinter() {
    return getInput( DEFAULT_PRINTER );
  }

  public IActionResource setResourcesFileToPrint( URI uri, String mimeType ) {
    return setResourceUri( FILE_TO_PRINT, uri, mimeType );
  }

  public IActionResource getResourcesFileToPrint() {
    return getResource( FILE_TO_PRINT );
  }

  public void setOutputPrinterName( String publicOutputName ) {
    setOutput( PRINTER_NAME, publicOutputName, ActionSequenceDocument.STRING_TYPE );
  }

  public IActionOutput getOutputPrinterName() {
    return getOutput( PRINTER_NAME );
  }

  public void setOutputDefaultPrinter( String publicOutputName ) {
    setOutput( DEFAULT_PRINTER, publicOutputName, ActionSequenceDocument.STRING_TYPE );
  }

  public IActionOutput getOutputDefaultPrinter() {
    return getOutput( DEFAULT_PRINTER );
  }
}
