/*!
 *
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
 *
 * Copyright (c) 2002-2018 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.platform.api.repository;

import org.pentaho.platform.api.engine.IFileFilter;
import org.pentaho.platform.api.engine.ISolutionFile;

import java.util.List;
import java.util.Set;

public interface IRepositoryFile extends ISearchable {

  char SEPARATOR = '/';

  /**
   * This method's purpose is to allow Hibernate to initialize the ACLs from the data-store. Application clients
   * should likely use resetAccessControls.
   */
  @SuppressWarnings( "rawtypes" )
  void setAccessControls(List acls);

  @SuppressWarnings( "rawtypes" )
  void resetAccessControls(List acls);

  int getRevision();

  String getFileId();

  String getSolution();

  String getSolutionPath();

  String getFileName();

  String getFullPath();

  void setParent(IRepositoryFile parent);

  IRepositoryFile getParent();

  ISolutionFile retrieveParent();

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.repository.ISearchable#getSearchableColumns()
   */
  String[] getSearchableColumns();

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.repository.ISearchable#getSearchableTable()
   */
  String getSearchableTable();

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.repository.ISearchable#getPhraseSearchQueryName()
   */
  String getPhraseSearchQueryName();

  boolean isDirectory();

  /**
   * @return Returns the childrenResources.
   */
  @SuppressWarnings( "rawtypes" )
  Set getChildrenFiles();

  /**
   * @param childrenResources
   *          The childrenResources to set.
   */
  @SuppressWarnings( "rawtypes" )
  void setChildrenFiles(Set childrenFiles);

  void addChildFile(IRepositoryFile file);

  void removeChildFile(IRepositoryFile file);

  /**
   * @return Returns the data.
   */
  byte[] getData();

  /**
   * @param data
   *          The data to set.
   */
  void setData(byte[] data);

  ISolutionFile[] listFiles(IFileFilter filter);

  ISolutionFile[] listFiles();

  IRepositoryFile[] listRepositoryFiles();

  int compareTo(Object o);

  /**
   * @return Returns the modDate.
   */
  long getLastModified();

  /**
   * @param modDate
   *          The modDate to set.
   */
  void setLastModified(long modDate);

  boolean containsActions();

  boolean isRoot();

  /**
   * @return a boolean indicating if this file has an extension
   */
  boolean hasExtension();

  /**
   * @return the extension (including the . seperator) of this file
   */
  String getExtension();

  boolean exists();

}
