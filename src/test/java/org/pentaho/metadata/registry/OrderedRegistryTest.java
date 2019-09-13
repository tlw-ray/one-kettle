/*!
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
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.metadata.registry;


import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;


public class OrderedRegistryTest {
  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Test
  public void testSaveLoad() throws Exception {

    OrderedFileRegistry metadataRegistry = new OrderedFileRegistry();
    metadataRegistry.setFilePath( tmpFolder.getRoot().getAbsolutePath() + "mdregtest2.xml" );
    metadataRegistry.init();

    metadataRegistry.clear();

    Entity ktr1 = new Entity( "ktr1", "My Trans", Type.TYPE_TRANSFORMATION.getId() );
    String attrValue1 = "attribute value 1";
    byte[] bytes = new byte[] { 97, 05, 97 };
    String attrValue2 = "attribute\tvalue 2\n" + new String( bytes );
    ktr1.setAttribute( "attr1", attrValue1 );
    ktr1.setAttribute( "attr2", attrValue2 );
    Entity table1 = new Entity( "table1", null, Type.TYPE_PHYSICAL_TABLE.getId() );
    Entity model1 = new Entity( "model1", "my model", Type.TYPE_OLAP_MODEL.getId() );
    Entity view1 = new Entity( "view1", "my view", Type.TYPE_ANALYZER_VIEW.getId() );

    metadataRegistry.addEntity( ktr1 );
    metadataRegistry.addEntity( table1 );
    metadataRegistry.addEntity( model1 );
    metadataRegistry.addEntity( view1 );

    Link link1 =
        new Link( ktr1.getId(), ktr1.getTypeId(), Verb.VERB_POPULATES.getId(), table1.getId(), table1.getTypeId() );
    Link link2 =
        new Link( model1.getId(), model1.getTypeId(), Verb.VERB_USES.getId(), table1.getId(), table1.getTypeId() );
    Link link3 =
        new Link( view1.getId(), view1.getTypeId(), Verb.VERB_USES.getId(), model1.getId(), model1.getTypeId() );

    metadataRegistry.addLink( link1 );
    metadataRegistry.addLink( link2 );
    metadataRegistry.addLink( link3 );

    Entity tmp = metadataRegistry.getEntity( ktr1.getId(), Type.TYPE_TRANSFORMATION.getId() );
    Assert.assertEquals( "Entity id is wrong", ktr1.getId(), tmp.getId() );
    Assert.assertEquals( "links list is wrong size", 3, metadataRegistry.getLinks().size() );
    Assert.assertEquals( "Entity attribute is wrong", attrValue1, tmp.getAttribute( "attr1" ) );
    Assert.assertEquals( "Entity attribute is wrong", attrValue2, tmp.getAttribute( "attr2" ) );

    Assert.assertEquals( "Wrong number of verbs", 5, metadataRegistry.getVerbs().size() );
    Assert.assertEquals( "Wrong verb", Verb.VERB_POPULATES.getId(), metadataRegistry.getVerbs().get( 0 ).getId() );

    Assert.assertEquals( "Wrong number of namespaces", 5, metadataRegistry.getNamespaces().size() );
    Assert.assertEquals( "Wrong namespace", Namespace.NAMESPACE_GLOBAL.getId(), metadataRegistry.getNamespaces()
        .get( 0 ).getId() );

    Assert.assertEquals( "Wrong number of types", 13, metadataRegistry.getTypes().size() );
    Assert.assertEquals( "Wrong tpe", Type.TYPE_TRANSFORMATION.getId(), metadataRegistry.getTypes().get( 0 ).getId() );

    Assert.assertEquals( "Wrong number of type links", 18, metadataRegistry.getTypeLinks().size() );
    Assert.assertEquals( "Wrong tpe", Verb.VERB_USES.getId(), metadataRegistry.getTypeLinks().get( 0 ).getVerbId() );

    metadataRegistry.commit();

    metadataRegistry.clear();

    tmp = metadataRegistry.getEntity( ktr1.getId(), Type.TYPE_TRANSFORMATION.getId() );
    Assert.assertNull( "Entity id is wrong", tmp );
    Assert.assertEquals( "links list is wrong size", 0, metadataRegistry.getLinks().size() );

    metadataRegistry.load();
    tmp = metadataRegistry.getEntity( ktr1.getId(), Type.TYPE_TRANSFORMATION.getId() );
    Assert.assertNotNull( "Entity is null", tmp );
    Assert.assertEquals( "Entity id is wrong", ktr1.getId(), tmp.getId() );
    Assert.assertEquals( "links list is wrong size", 3, metadataRegistry.getLinks().size() );
    Assert.assertEquals( "Entity attribute is wrong", attrValue1, tmp.getAttribute( "attr1" ) );
    Assert.assertEquals( "Entity attribute is wrong", attrValue2, tmp.getAttribute( "attr2" ) );

  }

  @Test
  public void testAddToBuffer() throws Exception {
    String badStringToEscapeWithSurrogateCh = "[\"\'<>& \uD842\uDFB7]";
    String expectedAfterEncoding = "<entry key=\"[&#34;&#39;&lt;>&amp; \uD842\uDFB7]\">[&#34;&#39;&lt;&gt;&amp; \uD842\uDFB7]</entry>\n";
    OrderedFileRegistry metadataRegistry = new OrderedFileRegistry();
    StringBuffer stringBuffer = new StringBuffer();
    metadataRegistry.addToBuffer( badStringToEscapeWithSurrogateCh, badStringToEscapeWithSurrogateCh, stringBuffer );
    Assert.assertEquals( expectedAfterEncoding, stringBuffer.toString() );
  }
}
