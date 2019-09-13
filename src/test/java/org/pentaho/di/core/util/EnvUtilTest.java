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

package org.pentaho.di.core.util;

import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.junit.rules.RestorePDIEnvironment;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Yury_Bakhmutski on 11/4/2015.
 */
public class EnvUtilTest {
  @ClassRule public static RestorePDIEnvironment env = new RestorePDIEnvironment();

  @Test
  public void vfsUserDirIsRoot_IsPublishedOnInitialisation() throws Exception {
    EnvUtil.environmentInit();
    //See PDI-14522, PDI-14821
    // don't check the exact value, because the initialisation depends on local settings
    // instead, simply check the value exists
    assertNotNull( Variables.getADefaultVariableSpace().getVariable( Const.VFS_USER_DIR_IS_ROOT ) );
    assertNotNull( System.getProperty( Const.VFS_USER_DIR_IS_ROOT ) );
  }

  @Test
  public void createLocale_Null() throws Exception {
    assertNull( EnvUtil.createLocale( null ) );
  }

  @Test
  public void createLocale_Empty() throws Exception {
    assertNull( EnvUtil.createLocale( "" ) );
  }

  @Test
  public void createLocale_SingleCode() throws Exception {
    assertEquals( Locale.ENGLISH, EnvUtil.createLocale( "en" ) );
  }

  @Test
  public void createLocale_DoubleCode() throws Exception {
    assertEquals( Locale.US, EnvUtil.createLocale( "en_US" ) );
  }

  @Test
  public void createLocale_DoubleCode_Variant() throws Exception {
    assertEquals( new Locale( "no", "NO", "NY" ), EnvUtil.createLocale( "no_NO_NY" ) );
  }

  @Test
  public void testClearProperty() {
    String dummyPropertyTest = "KETTLE_CORE_JUNIT_DUMMY_PROPERTY_TEST_"+Math.random();
    try {
      assertNull( EnvUtil.getSystemProperty( dummyPropertyTest ) );
      System.setProperty( dummyPropertyTest, "dummyValue" );
      assertEquals( "dummyValue", EnvUtil.getSystemProperty( dummyPropertyTest ) );
      assertEquals( "dummyValue", EnvUtil.clearSystemProperty( dummyPropertyTest ) );
      assertNull( EnvUtil.getSystemProperty( dummyPropertyTest ) );
    } finally {
      //assures that the test property is removed from the System
      System.clearProperty( dummyPropertyTest );
    }
  }
}
