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

package org.pentaho.di.core.compress.gzip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.compress.CompressionPluginType;
import org.pentaho.di.core.compress.CompressionProvider;
import org.pentaho.di.core.compress.CompressionProviderFactory;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class GZIPCompressionOutputStreamTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  public static final String PROVIDER_NAME = "GZip";

  public CompressionProviderFactory factory = null;
  public GZIPCompressionOutputStream outStream = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    PluginRegistry.addPluginType( CompressionPluginType.getInstance() );
    PluginRegistry.init( false );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    factory = CompressionProviderFactory.getInstance();
    CompressionProvider provider = factory.getCompressionProviderByName( PROVIDER_NAME );
    ByteArrayOutputStream in = new ByteArrayOutputStream();
    outStream = new GZIPCompressionOutputStream( in, provider );
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCtor() {
    assertNotNull( outStream );
  }

  @Test
  public void getCompressionProvider() {
    CompressionProvider provider = outStream.getCompressionProvider();
    assertEquals( provider.getName(), PROVIDER_NAME );
  }

  @Test
  public void testClose() throws IOException {
    CompressionProvider provider = outStream.getCompressionProvider();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    outStream = new GZIPCompressionOutputStream( out, provider ) {
    };
    outStream.close();
    try {
      outStream.write( "This will throw an Exception if the stream is already closed".getBytes() );
      fail();
    } catch ( IOException e ) {
      //Success, The Output Stream was already closed
    }
  }

  @Test
  public void testWrite() throws IOException {
    CompressionProvider provider = outStream.getCompressionProvider();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    outStream = new GZIPCompressionOutputStream( out, provider );
    outStream.write( "Test".getBytes() );
  }

  @Test
  public void testAddEntry() throws IOException {
    CompressionProvider provider = outStream.getCompressionProvider();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    outStream = new GZIPCompressionOutputStream( out, provider );
    outStream.addEntry( null, null );
  }
}
