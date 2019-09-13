/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2016-2018 by Hitachi Vantara : http://www.pentaho.com
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

package org.pentaho.di.trans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.steps.loadsave.MemoryRepository;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.base.LoadSaveBase;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;

public class LoadSaveTester<T extends Partitioner> extends LoadSaveBase<T> {

  public LoadSaveTester( Class<T> clazz, List<String> commonAttributes, List<String> xmlAttributes,
      List<String> repoAttributes, Map<String, String> getterMap, Map<String, String> setterMap,
      Map<String, FieldLoadSaveValidator<?>> fieldLoadSaveValidatorAttributeMap,
      Map<String, FieldLoadSaveValidator<?>> fieldLoadSaveValidatorTypeMap ) {
    super( clazz, commonAttributes, xmlAttributes, repoAttributes, getterMap, setterMap,
        fieldLoadSaveValidatorAttributeMap, fieldLoadSaveValidatorTypeMap );
  }

  public LoadSaveTester( Class<T> clazz, List<String> commonAttributes ) {
    super( clazz, commonAttributes );
  }

  public void testSerialization() throws KettleException {
    testXmlRoundTrip();
    testRepoRoundTrip();
    testClone();
  }

  public void testXmlRoundTrip() throws KettleException {
    T metaToSave = createMeta();
    Map<String, FieldLoadSaveValidator<?>> validatorMap =
        createValidatorMapAndInvokeSetters( xmlAttributes, metaToSave );
    T metaLoaded = createMeta();
    String xml = "<step>" + metaToSave.getXML() + "</step>";
    InputStream is = new ByteArrayInputStream( xml.getBytes() );
    metaLoaded.loadXML( XMLHandler.getSubNode( XMLHandler.loadXMLFile( is, null, false, false ), "step" ) );
    validateLoadedMeta( xmlAttributes, validatorMap, metaToSave, metaLoaded );
  }

  public void testRepoRoundTrip() throws KettleException {
    T metaToSave = createMeta();
    Map<String, FieldLoadSaveValidator<?>> validatorMap =
        createValidatorMapAndInvokeSetters( repoAttributes, metaToSave );
    T metaLoaded = createMeta();
    Repository rep = new MemoryRepository();
    metaToSave.saveRep( rep, null, null );
    metaLoaded.loadRep( rep, null );
    validateLoadedMeta( repoAttributes, validatorMap, metaToSave, metaLoaded );
  }

  protected void testClone() {
    T metaToSave = createMeta();
    Map<String, FieldLoadSaveValidator<?>> validatorMap =
        createValidatorMapAndInvokeSetters( xmlAttributes, metaToSave );

    @SuppressWarnings( "unchecked" )
    T metaLoaded = (T) metaToSave.clone();
    validateLoadedMeta( xmlAttributes, validatorMap, metaToSave, metaLoaded );
  }
}
