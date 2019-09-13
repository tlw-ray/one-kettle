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

import junit.framework.TestCase;

import org.dom4j.DocumentHelper;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;

/**
 * Tests for the <code>SqlQueryAction</code> class.
 * 
 * @author dkincade
 */
public class SqlQueryActionTest extends TestCase {

  /**
   * Tests the ability to manipulate the query timeout
   */
  public void testQueryTimeout() throws Exception {
    // Create a document without a query timeout
    ActionSequenceDocument doc = createQueryDocument();
    assertNotNull( doc );
    assertNotNull( doc.getExecutableChildren() );
    assertEquals( 1, doc.getExecutableChildren().length );
    assertEquals( SqlQueryAction.class, doc.getExecutableChildren()[0].getClass() );
    SqlQueryAction action = (SqlQueryAction) doc.getExecutableChildren()[0];

    // Check the resulting XML for no query timeout information
    assertEquals( -1, doc.getDocument().asXML().indexOf( "<timeout>" ) ); //$NON-NLS-1$

    // Add the query timeout
    action.setQueryTimeout( new ActionInputConstant( 10, null ) );

    // Check the resulting XML for the query timeout information
    String xml = doc.getDocument().asXML();
    assertTrue( xml.indexOf( "<timeout>" ) > 0 ); //$NON-NLS-1$

    // Reload a new document with that xml
    ActionSequenceDocument doc2 = new ActionSequenceDocument( DocumentHelper.parseText( xml ) );
    assertNotNull( doc2 );
    assertNotNull( doc2.getExecutableChildren() );
    assertEquals( 1, doc2.getExecutableChildren().length );
    assertEquals( SqlQueryAction.class, doc.getExecutableChildren()[0].getClass() );
    SqlQueryAction action2 = (SqlQueryAction) doc.getExecutableChildren()[0];
    assertNotNull( action2.getQueryTimeout() );
    assertEquals( new Integer( 10 ), action2.getQueryTimeout().getIntValue() );
  }

  /**
   * Creates a correct base instance of an action sequence document with one empty SqlQueryAction.
   */
  private ActionSequenceDocument createQueryDocument() throws Exception {
    ActionSequenceDocument document = new ActionSequenceDocument();
    document.addAction( SqlQueryAction.class );
    assertEquals( 0, document.validate().length );
    // action.setQueryTimeout(new ActionInputConstant(10));
    // String xml = actionSequenceDocument.getDocument().asXML();
    // actionSequenceDocument = new ActionSequenceDocument(DocumentHelper.parseText(xml));
    // actionSequenceDocument.getExecutableChildren();
    // assertEquals(0, errors.length);
    return document;
  }
}
