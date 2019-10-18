/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
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

package org.pentaho.di.repository;

import java.util.Calendar;
import java.util.List;

import org.pentaho.di.cluster.ClusterSchema;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.partition.PartitionSchema;
import org.pentaho.di.shared.SharedObjects;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.pentaho.platform.api.repository2.unified.IUnifiedRepository;

public interface Repository {

  /**
   * @return The name of the repository
   */
  String getName();

  /**
   * Get the repository version.
   *
   * @return The repository version as a string
   */
  String getVersion();

  /**
   * @return the metadata describing this repository.
   */
  RepositoryMeta getRepositoryMeta();

  /**
   * @return the currently logged on user. (also available through the repository security provider)
   */
  IUser getUserInfo();

  /**
   * @return The security provider for this repository.
   */
  RepositorySecurityProvider getSecurityProvider();

  /**
   * @return The security manager for this repository.
   */
  RepositorySecurityManager getSecurityManager();

  /**
   * @return the logging channel of this repository
   */
  LogChannelInterface getLog();

  /**
   * Connect to the repository. Make sure you don't connect more than once to the same repository with this repository
   * object.
   *
   * @param username
   *          the username of the user connecting to the repository.
   * @param password
   *          the password of the user connecting to the repository.
   * @throws KettleSecurityException
   *           in case the supplied user or password is incorrect.
   * @throws KettleException
   *           in case there is a general unexpected error OR if we're already connected to the repository.
   */
  void connect(String username, String password) throws KettleException, KettleSecurityException;

  /**
   * Disconnect from the repository.
   */
  void disconnect();

  boolean isConnected();

  /**
   * Initialize the repository with the repository metadata and user information.
   * */
  void init(RepositoryMeta repositoryMeta);

  // Common methods...

  /**
   * See if a repository object exists in the repository
   *
   * @param name
   *          the name of the object
   * @param repositoryDirectory
   *          the directory in which it should reside
   * @param objectType
   *          the type of repository object
   * @return true if the job exists
   * @throws KettleException
   *           in case something goes wrong.
   */
  boolean exists(String name, RepositoryDirectoryInterface repositoryDirectory,
                 RepositoryObjectType objectType) throws KettleException;

  ObjectId getTransformationID(String name, RepositoryDirectoryInterface repositoryDirectory) throws KettleException;

  ObjectId getJobId(String name, RepositoryDirectoryInterface repositoryDirectory) throws KettleException;

  void save(RepositoryElementInterface repositoryElement, String versionComment,
            ProgressMonitorListener monitor) throws KettleException;

  /**
   * Save an object to the repository optionally overwriting any associated objects.
   *
   * @param repositoryElement
   *          Object to save
   * @param versionComment
   *          Version comment for update
   * @param monitor
   *          Progress Monitor to report feedback to
   * @param overwrite
   *          Overwrite any existing objects involved in saving {@code repositoryElement}, e.g. repositoryElement,
   *          database connections, slave servers
   * @throws KettleException
   *           Error saving the object to the repository
   */
  void save(RepositoryElementInterface repositoryElement, String versionComment,
            ProgressMonitorListener monitor, boolean overwrite) throws KettleException;

  /**
   * Save the object to the repository with version comments as well as version dates. This form exists largely to
   * support the importing of revisions, preserving their revision date.
   *
   * @param repositoryElement
   * @param versionComment
   * @param versionDate
   * @param monitor
   * @param overwrite
   * @throws KettleException
   */
  void save(RepositoryElementInterface repositoryElement, String versionComment, Calendar versionDate,
            ProgressMonitorListener monitor, boolean overwrite) throws KettleException;

  RepositoryDirectoryInterface getDefaultSaveDirectory(RepositoryElementInterface repositoryElement) throws KettleException;

  RepositoryDirectoryInterface getUserHomeDirectory() throws KettleException;

  /**
   * Clear the shared object cache, if applicable.
   */
  void clearSharedObjectCache();

  // Transformations : Loading & saving objects...

  /**
   * Load a transformation with a name from a folder in the repository
   *
   * @param transname
   *          the name of the transformation to load
   * @param The
   *          folder to load it from
   * @param monitor
   *          the progress monitor to use (UI feedback)
   * @param setInternalVariables
   *          set to true if you want to automatically set the internal variables of the loaded transformation. (true is
   *          the default with very few exceptions!)
   * @param revision
   *          the revision to load. Specify null to load the last version.
   */
  TransMeta loadTransformation(String transname, RepositoryDirectoryInterface repdir,
                               ProgressMonitorListener monitor, boolean setInternalVariables, String revision) throws KettleException;

  /**
   * Load a transformation by id
   *
   * @param id_transformation
   *          the id of the transformation to load
   * @param versionLabel
   *          version to load. Specify null to load the last version.
   */
  TransMeta loadTransformation(ObjectId id_transformation, String versionLabel) throws KettleException;

  SharedObjects readTransSharedObjects(TransMeta transMeta) throws KettleException;

  /**
   * Move / rename a transformation
   *
   * @param id_transformation
   *          The ObjectId of the transformation to move
   * @param newDirectory
   *          The RepositoryDirectoryInterface that will be the new parent of the transformation (May be null if a move
   *          is not desired)
   * @param newName
   *          The new name of the transformation (May be null if a rename is not desired)
   * @return The ObjectId of the transformation that was moved
   * @throws KettleException
   */
  ObjectId renameTransformation(ObjectId id_transformation, RepositoryDirectoryInterface newDirectory,
                                String newName) throws KettleException;

  /**
   * Move / rename a transformation
   *
   * @param id_transformation
   *          The ObjectId of the transformation to move
   * @param versionComment
   *          Version comment for rename
   * @param newDirectory
   *          The RepositoryDirectoryInterface that will be the new parent of the transformation (May be null if a move
   *          is not desired)
   * @param newName
   *          The new name of the transformation (May be null if a rename is not desired)
   * @return The ObjectId of the transformation that was moved
   * @throws KettleException
   */
  ObjectId renameTransformation(ObjectId id_transformation, String versionComment,
                                RepositoryDirectoryInterface newDirectory, String newName) throws KettleException;

  /**
   * Delete everything related to a transformation from the repository. This does not included shared objects :
   * databases, slave servers, cluster and partition schema.
   *
   * @param id_transformation
   *          the transformation id to delete
   * @throws KettleException
   */
  void deleteTransformation(ObjectId id_transformation) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Jobs: Loading & saving objects...
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Load a job from the repository
   *
   * @param jobname
   *          the name
   * @param repdir
   *          the directory
   * @param monitor
   *          the progress monitor or null
   * @param revision
   *          the revision to load. Specify null to load the last version.
   */
  JobMeta loadJob(String jobname, RepositoryDirectoryInterface repdir, ProgressMonitorListener monitor,
                  String revision) throws KettleException;

  /**
   * Load a job from the repository by id
   *
   * @param id_job
   *          the id of the job
   * @param versionLabel
   *          version to load. Specify null to load the last version.
   */
  JobMeta loadJob(ObjectId id_job, String versionLabel) throws KettleException;

  SharedObjects readJobMetaSharedObjects(JobMeta jobMeta) throws KettleException;

  /**
   * Move / rename a job
   *
   * @param id_job
   *          The ObjectId of the job to move
   * @param versionComment
   *          Version comment for rename
   * @param newDirectory
   *          The RepositoryDirectoryInterface that will be the new parent of the job (May be null if a move is not
   *          desired)
   * @param newName
   *          The new name of the job (May be null if a rename is not desired)
   * @return The ObjectId of the job that was moved
   * @throws KettleException
   */
  ObjectId renameJob(ObjectId id_job, String versionComment, RepositoryDirectoryInterface newDirectory,
                     String newName) throws KettleException;

  /**
   * Move / rename a job
   *
   * @param id_job
   *          The ObjectId of the job to move
   * @param newDirectory
   *          The RepositoryDirectoryInterface that will be the new parent of the job (May be null if a move is not
   *          desired)
   * @param newName
   *          The new name of the job (May be null if a rename is not desired)
   * @return The ObjectId of the job that was moved
   * @throws KettleException
   */
  ObjectId renameJob(ObjectId id_job, RepositoryDirectoryInterface newDirectory, String newName) throws KettleException;

  void deleteJob(ObjectId id_job) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Databases : loading, saving, renaming, etc.
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Load the Database connection Metadata from the repository
   *
   * @param id_database
   *          the id of the database connection to load
   * @param revision
   *          the revision to load. Specify null to load the last version.
   * @throws KettleException
   *           in case something goes wrong with database, connection, etc.
   */
  DatabaseMeta loadDatabaseMeta(ObjectId id_database, String revision) throws KettleException;

  /**
   * Remove a database connection from the repository
   *
   * @param databaseName
   *          The name of the connection to remove
   * @throws KettleException
   *           In case something went wrong: database error, insufficient permissions, depending objects, etc.
   */
  void deleteDatabaseMeta(String databaseName) throws KettleException;

  ObjectId[] getDatabaseIDs(boolean includeDeleted) throws KettleException;

  String[] getDatabaseNames(boolean includeDeleted) throws KettleException;

  /**
   * Read all the databases defined in the repository
   *
   * @return a list of all the databases defined in the repository
   * @throws KettleException
   */
  List<DatabaseMeta> readDatabases() throws KettleException;

  ObjectId getDatabaseID(String name) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // ClusterSchema
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  ClusterSchema loadClusterSchema(ObjectId id_cluster_schema, List<SlaveServer> slaveServers,
                                  String versionLabel) throws KettleException;

  ObjectId[] getClusterIDs(boolean includeDeleted) throws KettleException;

  String[] getClusterNames(boolean includeDeleted) throws KettleException;

  ObjectId getClusterID(String name) throws KettleException;

  void deleteClusterSchema(ObjectId id_cluster) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // SlaveServer
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  SlaveServer loadSlaveServer(ObjectId id_slave_server, String versionLabel) throws KettleException;

  ObjectId[] getSlaveIDs(boolean includeDeleted) throws KettleException;

  String[] getSlaveNames(boolean includeDeleted) throws KettleException;

  /**
   * @return a list of all the slave servers in the repository.
   * @throws KettleException
   */
  List<SlaveServer> getSlaveServers() throws KettleException;

  ObjectId getSlaveID(String name) throws KettleException;

  void deleteSlave(ObjectId id_slave) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // PartitionSchema
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  PartitionSchema loadPartitionSchema(ObjectId id_partition_schema, String versionLabel) throws KettleException;

  ObjectId[] getPartitionSchemaIDs(boolean includeDeleted) throws KettleException;

  // public ObjectId[] getPartitionIDs(ObjectId id_partition_schema) throws KettleException;

  String[] getPartitionSchemaNames(boolean includeDeleted) throws KettleException;

  ObjectId getPartitionSchemaID(String name) throws KettleException;

  void deletePartitionSchema(ObjectId id_partition_schema) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Directory stuff
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  RepositoryDirectoryInterface loadRepositoryDirectoryTree() throws KettleException;

  RepositoryDirectoryInterface findDirectory(String directory) throws KettleException;

  RepositoryDirectoryInterface findDirectory(ObjectId directory) throws KettleException;

  void saveRepositoryDirectory(RepositoryDirectoryInterface dir) throws KettleException;

  void deleteRepositoryDirectory(RepositoryDirectoryInterface dir) throws KettleException;

  /**
   * Move / rename a repository directory
   *
   * @param id
   *          The ObjectId of the repository directory to move
   * @param newParentDir
   *          The RepositoryDirectoryInterface that will be the new parent of the repository directory (May be null if a
   *          move is not desired)
   * @param newName
   *          The new name of the repository directory (May be null if a rename is not desired)
   * @return The ObjectId of the repository directory that was moved
   * @throws KettleException
   */
  ObjectId renameRepositoryDirectory(ObjectId id, RepositoryDirectoryInterface newParentDir, String newName) throws KettleException;

  /**
   * Create a new directory, possibly by creating several sub-directies of / at the same time.
   *
   * @param parentDirectory
   *          the parent directory
   * @param directoryPath
   *          The path to the new Repository Directory, to be created.
   * @return The created sub-directory
   * @throws KettleException
   *           In case something goes wrong
   */
  RepositoryDirectoryInterface createRepositoryDirectory(RepositoryDirectoryInterface parentDirectory,
                                                         String directoryPath) throws KettleException;

  String[] getTransformationNames(ObjectId id_directory, boolean includeDeleted) throws KettleException;

  List<RepositoryElementMetaInterface> getJobObjects(ObjectId id_directory, boolean includeDeleted) throws KettleException;

  List<RepositoryElementMetaInterface> getTransformationObjects(ObjectId id_directory,
                                                                boolean includeDeleted) throws KettleException;

  /**
   * Gets all job and transformation objects in the given directory. (Combines {@link #getJobObjects(ObjectId, boolean)}
   * and {@link #getTransformationObjects(ObjectId, boolean)} into one operation.
   *
   * @param id_directory
   *          directory
   * @param includeDeleted
   *          true to return deleted objects
   * @return list of repository objects
   * @throws KettleException
   *           In case something goes wrong
   */
  List<RepositoryElementMetaInterface> getJobAndTransformationObjects(ObjectId id_directory,
                                                                      boolean includeDeleted) throws KettleException;

  String[] getJobNames(ObjectId id_directory, boolean includeDeleted) throws KettleException;

  /**
   * Returns the child directory names of a parent directory
   *
   * @param id_directory
   *          parent directory id
   * @return array of child names
   * @throws KettleException
   */
  String[] getDirectoryNames(ObjectId id_directory) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Logging...
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Insert an entry in the audit trail of the repository. This is an optional operation and depends on the capabilities
   * of the underlying repository.
   *
   * @param The
   *          description to be put in the audit trail of the repository.
   */
  ObjectId insertLogEntry(String description) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Relationships between objects !!!!!!!!!!!!!!!!!!!!!! <-----------------
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  void insertStepDatabase(ObjectId id_transformation, ObjectId id_step, ObjectId id_database) throws KettleException;

  void insertJobEntryDatabase(ObjectId id_job, ObjectId id_jobentry, ObjectId id_database) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Condition
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * This method saves the object ID of the condition object (if not null) in the step attributes
   *
   * @param id_step
   * @param code
   * @param condition
   */
  void saveConditionStepAttribute(ObjectId id_transformation, ObjectId id_step, String code,
                                  Condition condition) throws KettleException;

  /**
   * Load a condition from the repository with the Object ID stored in a step attribute.
   *
   * @param id_step
   * @param code
   * @return
   * @throws KettleException
   */
  Condition loadConditionFromStepAttribute(ObjectId id_step, String code) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Attributes for steps...
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  boolean getStepAttributeBoolean(ObjectId id_step, int nr, String code, boolean def) throws KettleException;

  boolean getStepAttributeBoolean(ObjectId id_step, int nr, String code) throws KettleException;

  boolean getStepAttributeBoolean(ObjectId id_step, String code) throws KettleException;

  long getStepAttributeInteger(ObjectId id_step, int nr, String code) throws KettleException;

  long getStepAttributeInteger(ObjectId id_step, String code) throws KettleException;

  String getStepAttributeString(ObjectId id_step, int nr, String code) throws KettleException;

  String getStepAttributeString(ObjectId id_step, String code) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, int nr, String code, String value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, String code, String value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, int nr, String code, boolean value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, String code, boolean value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, int nr, String code, long value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, String code, long value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, int nr, String code, double value) throws KettleException;

  void saveStepAttribute(ObjectId id_transformation, ObjectId id_step, String code, double value) throws KettleException;

  int countNrStepAttributes(ObjectId id_step, String code) throws KettleException;

  // ///////////////////////////////////////////////////////////////////////////////////////////////
  // Attributes for job entries...
  // ///////////////////////////////////////////////////////////////////////////////////////////////

  int countNrJobEntryAttributes(ObjectId id_jobentry, String code) throws KettleException;

  boolean getJobEntryAttributeBoolean(ObjectId id_jobentry, String code) throws KettleException;

  boolean getJobEntryAttributeBoolean(ObjectId id_jobentry, int nr, String code) throws KettleException;

  boolean getJobEntryAttributeBoolean(ObjectId id_jobentry, String code, boolean def) throws KettleException;

  long getJobEntryAttributeInteger(ObjectId id_jobentry, String code) throws KettleException;

  long getJobEntryAttributeInteger(ObjectId id_jobentry, int nr, String code) throws KettleException;

  String getJobEntryAttributeString(ObjectId id_jobentry, String code) throws KettleException;

  String getJobEntryAttributeString(ObjectId id_jobentry, int nr, String code) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, int nr, String code, String value) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, String code, String value) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, int nr, String code, boolean value) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, String code, boolean value) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, int nr, String code, long value) throws KettleException;

  void saveJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, String code, long value) throws KettleException;

  /**
   * This method is introduced to avoid having to go over an integer/string/whatever in the interface and the step code.
   *
   * @param id_step
   * @param code
   * @return
   * @throws KettleException
   */
  DatabaseMeta loadDatabaseMetaFromStepAttribute(ObjectId id_step, String code,
                                                 List<DatabaseMeta> databases) throws KettleException;

  /**
   * This method saves the object ID of the database object (if not null) in the step attributes
   *
   * @param id_transformation
   * @param id_step
   * @param code
   * @param database
   */
  void saveDatabaseMetaStepAttribute(ObjectId id_transformation, ObjectId id_step, String code,
                                     DatabaseMeta database) throws KettleException;

  /**
   * This method is introduced to avoid having to go over an integer/string/whatever in the interface and the job entry
   * code.
   *
   * @param id_step
   * @param code
   * @return
   * @throws KettleException
   */
  DatabaseMeta loadDatabaseMetaFromJobEntryAttribute(ObjectId id_jobentry, String nameCode, String idCode,
                                                     List<DatabaseMeta> databases) throws KettleException;

  /**
   * This method is introduced to avoid having to go over an integer/string/whatever in the interface and the job entry
   * code.
   *
   * @param id_entry
   * @param nameCode
   * @param nr
   * @param idcode
   * @param databases
   * @return
   * @throws KettleException
   */
  DatabaseMeta loadDatabaseMetaFromJobEntryAttribute(ObjectId id_jobentry, String nameCode, int nr,
                                                     String idCode, List<DatabaseMeta> databases) throws KettleException;

  /**
   * This method saves the object ID of the database object (if not null) in the job entry attributes
   *
   * @param id_job
   * @param id_jobentry
   * @param idCode
   * @param database
   */
  void saveDatabaseMetaJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, String nameCode,
                                         String idCode, DatabaseMeta database) throws KettleException;

  /**
   * This method saves the object ID of the database object (if not null) in the job entry attributes
   *
   * @param id_job
   * @param id_jobentry
   * @param nr
   * @param code
   * @param database
   */
  void saveDatabaseMetaJobEntryAttribute(ObjectId id_job, ObjectId id_jobentry, int nr, String nameCode,
                                         String idCode, DatabaseMeta database) throws KettleException;

  /**
   * Removes he deleted flag from a repository element in the repository. If it wasn't deleted, it remains untouched.
   *
   * @param element
   *          the repository element to restore
   * @throws KettleException
   *           get throws in case something goes horribly wrong.
   */
  void undeleteObject(RepositoryElementMetaInterface repositoryObject) throws KettleException;

  /**
   * Retrieves the current list of of IRepository Services.
   *
   * @return List of repository services
   * @throws KettleException
   *           in case something goes horribly wrong.
   */
  List<Class<? extends IRepositoryService>> getServiceInterfaces() throws KettleException;

  /**
   * Retrieves a given repository service
   *
   * @param service
   *          class name
   * @return repository service
   *
   * @throws KettleException
   *           in case something goes horribly wrong.
   */
  IRepositoryService getService(Class<? extends IRepositoryService> clazz) throws KettleException;

  /**
   * Checks whether a given repository service is available or not
   *
   * @param repository
   *          service class that needs to be checked for support
   * @throws KettleException
   *           in case something goes horribly wrong.
   */
  boolean hasService(Class<? extends IRepositoryService> clazz) throws KettleException;

  /**
   * Get more information about a certain object ID in the form of the RepositoryObject
   *
   * @param objectId
   *          The ID of the object to get more information about.
   * @param objectType
   *          The type of object to investigate.
   * @return The repository object or null if nothing could be found.
   * @throws KettleException
   *           In case there was a loading problem.
   */
  RepositoryObject getObjectInformation(ObjectId objectId, RepositoryObjectType objectType) throws KettleException;

  /**
   * This is an informational message that a repository can display on connecting within Spoon. If a null is returned,
   * no message is displayed to the end user.
   *
   * @return message
   */
  String getConnectMessage();

  /**
   * Get the repository version.
   *
   * @return The repository version as a string
   */
  String[] getJobsUsingDatabase(ObjectId id_database) throws KettleException;

  String[] getTransformationsUsingDatabase(ObjectId id_database) throws KettleException;

  /**
   * @return the importer that will handle imports into this repository
   */
  IRepositoryImporter getImporter();

  /**
   * @return the exporter that will handle exports from this repository
   */
  IRepositoryExporter getExporter() throws KettleException;

  /**
   * @return the Metastore that is implemented in this Repository. Return null if this repository doesn't implement a
   *         Metastore.
   */
  IMetaStore getMetaStore();

  /**
   * @return repository for connect to server
   */
  IUnifiedRepository getUnderlyingRepository();
}
