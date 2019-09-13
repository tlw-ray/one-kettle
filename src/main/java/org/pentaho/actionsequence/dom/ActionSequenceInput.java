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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.pentaho.actionsequence.dom.actions.IActionParameterMgr;

/**
 * A wrapper class for an action definition input or output element.
 * 
 * @author Angelo Rodriguez
 * 
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ActionSequenceInput extends AbstractIOElement implements IActionSequenceInput {

  public static final int REQUEST_INPUT_SOURCE_ID = 1;
  public static final int SESSION_INPUT_SOURCE_ID = 2;
  public static final int RUNTIME_INPUT_SOURCE_ID = 3;
  public static final int GLOBAL_INPUT_SOURCE_ID = 4;

  public ActionSequenceInput( Element inputElement, IActionParameterMgr actionInputProvider ) {
    super( inputElement, actionInputProvider );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.designstudio.dom.ActionSequenceIO#setType(java.lang.String)
   */
  public void setType( String ioType ) {
    if ( ( ioType != null ) && !ioType.equals( getType() ) ) {
      ioElement.addAttribute( TYPE_NAME, ioType );
      Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
      if ( defValElement != null ) {
        defValElement.clearContent();
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  public void setDefaultValue( String defValue ) {
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    ioElement.elements( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS ).clear();
    if ( defValue == null ) {
      if ( defValElement != null ) {
        defValElement.detach();
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      if ( defValElement == null ) {
        defValElement = ioElement.addElement( ActionSequenceDocument.DEFAULT_VAL_NAME );
      } else {
        defValElement.clearContent();
      }
      defValElement.addAttribute( TYPE_NAME, null );
      if ( defValue.length() > 0 ) {
        defValElement.addCDATA( defValue );
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  public void setDefaultValue( String[] defValue ) {
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    ioElement.elements( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS ).clear();
    if ( defValue == null ) {
      if ( defValElement != null ) {
        defValElement.detach();
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      if ( defValElement == null ) {
        defValElement = ioElement.addElement( ActionSequenceDocument.DEFAULT_VAL_NAME );
      } else {
        defValElement.clearContent();
      }
      if ( defValue.length > 0 ) {
        defValElement.addAttribute( TYPE_NAME, ActionSequenceDocument.STRING_LIST_TYPE );
        for ( int i = 0; i < defValue.length; i++ ) {
          defValElement.addElement( ActionSequenceDocument.DEFAULT_STRING_LIST_ITEM ).setText( defValue[i] );
        }
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  /**
   * Sets the input default value.
   * 
   * @param paramMap
   *          the default value
   */
  public void setDefaultValue( HashMap paramMap ) {
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    ioElement.elements( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS ).clear();
    if ( paramMap == null ) {
      if ( defValElement != null ) {
        defValElement.detach();
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      if ( defValElement == null ) {
        defValElement = ioElement.addElement( ActionSequenceDocument.DEFAULT_VAL_NAME );
      } else {
        defValElement.clearContent();
      }
      if ( paramMap.size() > 0 ) {
        defValElement.addAttribute( TYPE_NAME, ActionSequenceDocument.PROPERTY_MAP_TYPE );

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        for ( Iterator keyIter = paramMap.keySet().iterator(); keyIter.hasNext(); ) {
          defaultTableModel.addColumn( keyIter.next().toString() );
        }
        defaultTableModel.addRow( (String[]) paramMap.values().toArray( new String[0] ) );
        initPropertyMap( defValElement, defaultTableModel );
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   */
  public void setDefaultValue( TableModel defValue ) {
    setDefaultValue( defValue, false );
  }

  private void initPropertyMap( Element defValElement, TableModel defValue ) {
    for ( int rowIdx = 0; rowIdx < defValue.getRowCount(); rowIdx++ ) {
      Element propertyMapElement = defValElement.addElement( ActionSequenceDocument.PROPERTY_MAP_TYPE );
      for ( int colIdx = 0; colIdx < defValue.getColumnCount(); colIdx++ ) {
        Element entryElement = propertyMapElement.addElement( ActionSequenceDocument.PROPERTY_MAP_ENTRY );
        entryElement.addAttribute( ActionSequenceDocument.PROPERTY_MAP_ENTRY_KEY, defValue.getColumnName( colIdx ) );
        Object value = defValue.getValueAt( rowIdx, colIdx );
        entryElement.setText( value == null ? "" : value.toString() ); //$NON-NLS-1$
      }
    }
  }

  private void setPropMapListDefVal( TableModel defValue ) {
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    ioElement.elements( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS ).clear();
    if ( defValue == null ) {
      if ( defValElement != null ) {
        defValElement.detach();
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      if ( defValElement == null ) {
        defValElement = ioElement.addElement( ActionSequenceDocument.DEFAULT_VAL_NAME );
      } else {
        defValElement.clearContent();
      }
      if ( defValue.getColumnCount() > 0 ) {
        defValElement.addAttribute( TYPE_NAME, ActionSequenceDocument.PROPERTY_MAP_LIST_TYPE );
        initPropertyMap( defValElement, defValue );
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  private void setResultSetDefVal( TableModel defValue ) {
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    ioElement.elements( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS ).clear();
    if ( defValue == null ) {
      if ( defValElement != null ) {
        defValElement.detach();
        ActionSequenceDocument.fireIoChanged( this );
      }
    } else {
      if ( defValElement == null ) {
        defValElement = ioElement.addElement( ActionSequenceDocument.DEFAULT_VAL_NAME );
      } else {
        defValElement.clearContent();
      }
      if ( defValue.getColumnCount() > 0 ) {
        defValElement.addAttribute( TYPE_NAME, ActionSequenceDocument.RESULTSET_TYPE );
        Element columnsElement = ioElement.addElement( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS );
        for ( int colIdx = 0; colIdx < defValue.getColumnCount(); colIdx++ ) {
          columnsElement.addElement( defValue.getColumnName( colIdx ) ).addAttribute( ActionSequenceResource.TYPE_NAME,
              ActionSequenceDocument.STRING_TYPE );
        }
        for ( int rowIdx = 0; rowIdx < defValue.getRowCount(); rowIdx++ ) {
          Element rowElement = defValElement.addElement( ActionSequenceDocument.RESULTSET_ROW );
          for ( int colIdx = 0; colIdx < defValue.getColumnCount(); colIdx++ ) {
            Object value = defValue.getValueAt( rowIdx, colIdx );
            Element cellElement = rowElement.addElement( defValue.getColumnName( colIdx ) );
            cellElement.setText( value == null ? "" : value.toString() ); //$NON-NLS-1$
          }
        }
      }
      ActionSequenceDocument.fireIoChanged( this );
    }
  }

  /**
   * Sets the input default value.
   * 
   * @param defValue
   *          the default value
   * @param usePropertyMapList
   *          indicates whether the property map list element or result set element should be used to save the default
   *          value.
   */
  public void setDefaultValue( TableModel defValue, boolean usePropertyMapList ) {
    if ( usePropertyMapList ) {
      setPropMapListDefVal( defValue );
    } else {
      setResultSetDefVal( defValue );
    }
  }

  private String[] getDefaultStringList() {
    String[] defaultStringList = null;
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    if ( defValElement != null ) {
      List listItems = defValElement.elements( ActionSequenceDocument.DEFAULT_STRING_LIST_ITEM );
      defaultStringList = new String[listItems.size()];
      int index = 0;
      for ( Iterator iter = listItems.iterator(); iter.hasNext(); ) {
        Element listItem = (Element) iter.next();
        defaultStringList[index++] = listItem.getText();
      }
    }
    return defaultStringList;
  }

  private String getDefaultString() {
    String defaultString = null;
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    if ( defValElement != null ) {
      defaultString = defValElement.getText();
    }
    return defaultString;
  }

  private LinkedHashMap getDefaultPropertyMap() {
    LinkedHashMap linkedHashMap = null;
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    if ( defValElement != null ) {
      linkedHashMap = new LinkedHashMap();
      Element propertyMapElement = defValElement.element( ActionSequenceDocument.PROPERTY_MAP_TYPE );
      if ( propertyMapElement != null ) {
        List entries = propertyMapElement.elements( ActionSequenceDocument.PROPERTY_MAP_ENTRY );
        for ( Iterator iter = entries.iterator(); iter.hasNext(); ) {
          Element entry = (Element) iter.next();
          linkedHashMap.put( entry.attributeValue( ActionSequenceDocument.PROPERTY_MAP_ENTRY_KEY ), entry.getText() );
        }
      }
    }
    return linkedHashMap;
  }

  private TableModel getDefaultPropertyMapList() {
    DefaultTableModel defaultTableModel = null;
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    if ( defValElement != null ) {
      defaultTableModel = new DefaultTableModel();
      List propertyMaps = defValElement.elements( ActionSequenceDocument.PROPERTY_MAP_TYPE );
      HashSet columnSet = new HashSet();
      for ( Iterator iter = propertyMaps.iterator(); iter.hasNext(); ) {
        Element propertyMap = (Element) iter.next();
        List propertyMapEntries = propertyMap.elements( ActionSequenceDocument.PROPERTY_MAP_ENTRY );
        for ( Iterator entryIter = propertyMapEntries.iterator(); entryIter.hasNext(); ) {
          Element entry = (Element) entryIter.next();
          columnSet.add( entry.attributeValue( ActionSequenceDocument.PROPERTY_MAP_ENTRY_KEY ) );
        }
      }

      defaultTableModel.setColumnIdentifiers( columnSet.toArray() );

      for ( Iterator iter = propertyMaps.iterator(); iter.hasNext(); ) {
        Element propertyMap = (Element) iter.next();
        ArrayList row = new ArrayList();
        for ( Iterator columnIter = columnSet.iterator(); columnIter.hasNext(); ) {
          String columnName = ( (String) columnIter.next() );
          String cellValue = ""; //$NON-NLS-1$
          List propertyMapEntries = propertyMap.elements( ActionSequenceDocument.PROPERTY_MAP_ENTRY );
          for ( Iterator entryIter = propertyMapEntries.iterator(); entryIter.hasNext(); ) {
            Element entry = (Element) entryIter.next();
            if ( columnName.equals( entry.attributeValue( ActionSequenceDocument.PROPERTY_MAP_ENTRY_KEY ) ) ) {
              cellValue = entry.getText();
              break;
            }
          }
          row.add( cellValue );
        }
        defaultTableModel.addRow( row.toArray() );
      }
    }
    return defaultTableModel;
  }

  private TableModel getDefaultResultSet() {
    DefaultTableModel defaultTableModel = null;
    Element defValElement = ioElement.element( ActionSequenceDocument.DEFAULT_VAL_NAME );
    if ( defValElement != null ) {
      defaultTableModel = new DefaultTableModel();
      ArrayList columnList = new ArrayList();
      Element columnsElement = ioElement.element( ActionSequenceDocument.RESULTSET_DEFAULT_COLUMNS );
      if ( columnsElement != null ) {
        List columns = columnsElement.elements();
        for ( Iterator iter = columns.iterator(); iter.hasNext(); ) {
          Element columnElement = (Element) iter.next();
          columnList.add( columnElement.getName() );
        }
      }

      defaultTableModel.setColumnIdentifiers( columnList.toArray() );

      List rows = defValElement.elements( ActionSequenceDocument.RESULTSET_ROW );
      for ( Iterator rowIterator = rows.iterator(); rowIterator.hasNext(); ) {
        Element rowElement = (Element) rowIterator.next();
        ArrayList rowValues = new ArrayList();
        for ( Iterator columnIter = columnList.iterator(); columnIter.hasNext(); ) {
          String columnName = ( (String) columnIter.next() );
          String cellValue = ""; //$NON-NLS-1$
          Element cellElement = rowElement.element( columnName );
          if ( cellElement != null ) {
            cellValue = cellElement.getText();
          }
          rowValues.add( cellValue );
        }
        defaultTableModel.addRow( rowValues.toArray() );
      }
    }
    return defaultTableModel;
  }

  /**
   * @return the default value or null if none exists.
   */
  public Object getDefaultValue() {
    Object defVal = null;
    String type = getType();
    if ( ActionSequenceDocument.STRING_LIST_TYPE.equals( type ) ) {
      defVal = getDefaultStringList();
    } else if ( ActionSequenceDocument.LIST_TYPE.equals( type ) ) {
      defVal = getDefaultStringList();
    } else if ( ActionSequenceDocument.RESULTSET_TYPE.equals( type ) ) {
      defVal = getDefaultResultSet();
    } else if ( ActionSequenceDocument.PROPERTY_MAP_TYPE.equals( type ) ) {
      defVal = getDefaultPropertyMap();
    } else if ( ActionSequenceDocument.PROPERTY_MAP_LIST_TYPE.equals( type ) ) {
      defVal = getDefaultPropertyMapList();
    } else if ( ActionSequenceDocument.STRING_TYPE.equals( type ) ) {
      defVal = getDefaultString();
    } else if ( ActionSequenceDocument.LONG_TYPE.equals( type ) ) {
      defVal = getDefaultString();
    } else if ( ActionSequenceDocument.INTEGER_TYPE.equals( type ) ) {
      defVal = getDefaultString();
    } else if ( ActionSequenceDocument.BIGDECIMAL_TYPE.equals( type ) ) {
      defVal = getDefaultString();
    }
    return defVal;
  }

  public IActionSequenceInputSource[] getSources() {
    ArrayList inputSources = new ArrayList();
    List sourceElements = ioElement.selectNodes( ActionSequenceDocument.INPUT_SOURCES_NAME + "/*" ); //$NON-NLS-1$
    for ( Iterator iter = sourceElements.iterator(); iter.hasNext(); ) {
      inputSources.add( new ActionSequenceInputSource( (Element) iter.next(), actionInputProvider ) );
    }
    return (IActionSequenceInputSource[]) inputSources.toArray( new ActionSequenceInputSource[0] );
  }

  public IActionSequenceInputSource addSource( String origin, String name ) {
    Element sourceParent = DocumentHelper.makeElement( ioElement, ActionSequenceDocument.INPUT_SOURCES_NAME );
    Element newSourceElement = sourceParent.addElement( origin );
    newSourceElement.setText( name );
    IActionSequenceInputSource actionSequenceInputSource =
        new ActionSequenceInputSource( newSourceElement, actionInputProvider );
    ActionSequenceDocument.fireIoChanged( this );
    return actionSequenceInputSource;
  }

  public IActionSequenceInputSource addSource( int index, String origin, String name ) {
    if ( index >= getSources().length ) {
      throw new ArrayIndexOutOfBoundsException();
    }
    Element sourceParent = ioElement.element( ActionSequenceDocument.INPUT_SOURCES_NAME );
    Element newSourceElement = new DefaultElement( origin );
    List sources = sourceParent.elements();
    sources.add( index, newSourceElement );
    return new ActionSequenceInputSource( newSourceElement, actionInputProvider );
  }

  public String getVariableName() {
    return getName();
  }

}
