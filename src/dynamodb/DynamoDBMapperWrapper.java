package dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import java.util.List;
import java.util.Optional;

/**
 * A simple wrapper around DynamoDBMapper.
 *
 * @see <a href="https://github.com/IvanMwiruki/dynamodbmapper-wrapper">dynamodbmapper-wrapper</a>
 */
public class DynamoDBMapperWrapper {

    private final DynamoDBMapper mapper;

    public DynamoDBMapperWrapper(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Saves an item in DynamoDB. Additionally, it returns the item that was
     * saved. Under the hood, the DynamoDBMapper being delegated to uses either
     * {@link AmazonDynamoDB#putItem(PutItemRequest)}
     * or {@link AmazonDynamoDB#updateItem(UpdateItemRequest)}.
     *
     * @param pojo the item to save
     * @return the item that was saved, which now has any auto generated,
     *     or otherwise DynamoDB created, fields
     * @see IDynamoDBMapper#save(Object, DynamoDBSaveExpression, DynamoDBMapperConfig)
     */
    public <T> T save(T pojo) {
        mapper.save(pojo);
        return pojo;
    }

    /**
     * Saves an item in DynamoDB. Additionally, it returns the item that was
     * saved. Under the hood, the DynamoDBMapper being delegated to uses either
     * {@link AmazonDynamoDB#putItem(PutItemRequest)}
     * or {@link AmazonDynamoDB#updateItem(UpdateItemRequest)}.
     *
     * @param pojo   the item to save
     * @param config the configuration to use, which overrides the default provided
     *               at object creation
     * @see IDynamoDBMapper#save(Object, DynamoDBSaveExpression, DynamoDBMapperConfig)
     */
    public <T> T save(T pojo, DynamoDBMapperConfig config) {
        mapper.save(pojo, config);
        return pojo;
    }

    /**
     * Saves an item in DynamoDB. Additionally, it returns the item that was
     * saved. Under the hood, the DynamoDBMapper being delegated to uses either
     * {@link AmazonDynamoDB#putItem(PutItemRequest)}
     * or {@link AmazonDynamoDB#updateItem(UpdateItemRequest)}.
     *
     * @param pojo           the item to save
     * @param saveExpression provides a conditional save
     * @return the item that was saved, which now has any auto generated, or otherwise DynamoDB created, fields
     * @see IDynamoDBMapper#save(Object, DynamoDBSaveExpression, DynamoDBMapperConfig)
     */
    public <T> T save(T pojo, DynamoDBSaveExpression saveExpression) {
        mapper.save(pojo, saveExpression);
        return pojo;
    }

    /**
     * Saves an item in DynamoDB. Additionally, it returns the item that was
     * saved. Under the hood, the DynamoDBMapper being delegated to uses either
     * {@link AmazonDynamoDB#putItem(PutItemRequest)}
     * or {@link AmazonDynamoDB#updateItem(UpdateItemRequest)}.
     *
     * @param pojo           the item to save
     * @param saveExpression provides a conditional save
     * @param config         is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT}
     *                       in other save methods but can be specified here.
     * @return the item that was saved, which now has any auto generated, or otherwise DynamoDB created, fields
     * @see IDynamoDBMapper#save(Object, DynamoDBSaveExpression, DynamoDBMapperConfig)
     */
    public <T> T save(T pojo,
                      DynamoDBSaveExpression saveExpression,
                      DynamoDBMapperConfig config) {
        mapper.save(pojo, saveExpression, config);
        return pojo;
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param keyObject an object of the class to load with the key values to match
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(T keyObject) {
        return Optional.ofNullable(mapper.load(keyObject));
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param keyObject an object of the class to load with the key values to match
     * @param config    is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} in other load methods
     *                  but can be specified here.
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(T keyObject, DynamoDBMapperConfig config) {
        return Optional.ofNullable(mapper.load(keyObject, config));
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param pojo    the class to load, corresponding to a DynamoDB table
     * @param hashKey the key of the item
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(Class<T> pojo, Object hashKey) {
        return Optional.ofNullable(mapper.load(pojo, hashKey));
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param pojo     the class to load, corresponding to a DynamoDB table
     * @param hashKey  the key of the item
     * @param rangeKey the range key of the item
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(Class<T> pojo, Object hashKey, Object rangeKey) {
        return Optional.ofNullable(mapper.load(pojo, hashKey, rangeKey));
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param pojo    the class to load, corresponding to a DynamoDB table
     * @param hashKey the key of the item
     * @param config  is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} in other load methods
     *                but can be specified here.
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(Class<T> pojo, Object hashKey, DynamoDBMapperConfig config) {
        return Optional.ofNullable(mapper.load(pojo, hashKey, config));
    }

    /**
     * Returns an Optional of an item from DynamoDB, or an empty Optional if a matching item wasn't found.
     * Under the hood, the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#getItem(GetItemRequest)}.
     *
     * @param pojo     the class to load, corresponding to a DynamoDB table
     * @param hashKey  the key of the item
     * @param rangeKey the range key of the item
     * @param config   is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} in other load methods
     *                 but can be specified here.
     * @return an item, if found, or null if not
     * @see IDynamoDBMapper#load(Class, Object, Object, DynamoDBMapperConfig)
     */
    public <T> Optional<T> load(Class<T> pojo, Object hashKey, Object rangeKey, DynamoDBMapperConfig config) {
        return Optional.ofNullable(mapper.load(pojo, hashKey, rangeKey, config));
    }

    /**
     * Queries a DynamoDB table and returns the matching results in an unmodifiable list.
     * Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#query(QueryRequest)}.
     *
     * @param pojo            the class to query, corresponding to a DynamoDB table
     * @param queryExpression details on how to run the query and filter results
     * @param <T>             the type of the objects being returned
     * @return an unmodifiable list, which is lazily loaded by default (items are loaded into it
     *     as that object in the list is actually accessed)
     * @see IDynamoDBMapper#query(Class, DynamoDBQueryExpression, DynamoDBMapperConfig)
     */
    public <T> PaginatedQueryList<T> query(Class<T> pojo, DynamoDBQueryExpression<T> queryExpression) {
        return mapper.query(pojo, queryExpression);
    }

    /**
     * Queries a DynamoDB table and returns the matching results in an unmodifiable list.
     * Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#query(QueryRequest)}.
     *
     * @param pojo            the class to query, corresponding to a DynamoDB table
     * @param queryExpression details on how to run the query and filter results
     * @param config          is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *                        but can be specified here.
     * @param <T>             the type of the objects being returned
     * @return an unmodifiable list, which is lazily loaded by default (items are loaded into it
     *     as that object in the list is actually accessed)
     * @see IDynamoDBMapper#query(Class, DynamoDBQueryExpression, DynamoDBMapperConfig)
     */
    public <T> PaginatedQueryList<T> query(Class<T> pojo,
                                           DynamoDBQueryExpression<T> queryExpression,
                                           DynamoDBMapperConfig config) {
        return mapper.query(pojo, queryExpression, config);
    }

    /**
     * Queries a DynamoDB table and returns a single page of matching results. The page returned contains
     * a lastEvaluatedKey, allowing the caller to request the next page of results. Under the hood,
     * the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#query(QueryRequest)}.
     *
     * @param pojo            the class to query, corresponding to a DynamoDB table
     * @param queryExpression details on how to run the query and filter results
     * @param <T>             the type of the objects being returned
     * @return a page of results
     * @see IDynamoDBMapper#queryPage(Class, DynamoDBQueryExpression, DynamoDBMapperConfig)
     */
    public <T> QueryResultPage<T> queryPage(Class<T> pojo, DynamoDBQueryExpression<T> queryExpression) {
        return mapper.queryPage(pojo, queryExpression);
    }

    /**
     * Queries a DynamoDB table and returns a single page of matching results. The page returned contains
     * a lastEvaluatedKey, allowing the caller to request the next page of results. Under the hood,
     * the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#query(QueryRequest)}.
     *
     * @param pojo            the class to query, corresponding to a DynamoDB table
     * @param queryExpression details on how to run the query and filter results
     * @param config          is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *                        but can be specified here.
     * @param <T>             the type of the objects being returned
     * @return a page of results
     * @see IDynamoDBMapper#queryPage(Class, DynamoDBQueryExpression, DynamoDBMapperConfig)
     */
    public <T> QueryResultPage<T> queryPage(Class<T> pojo,
                                            DynamoDBQueryExpression<T> queryExpression,
                                            DynamoDBMapperConfig config) {
        return mapper.queryPage(pojo, queryExpression, config);
    }

    /**
     * Scans a DynamoDB table and returns the matching results in an unmodifiable list.
     * Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#scan(ScanRequest)}.
     *
     * @param pojo           the class to scan, corresponding to a DynamoDB table
     * @param scanExpression details on how to run the scan and filter results
     * @param <T>            the type of the objects being returned
     * @return an unmodifiable list, which is lazily loaded by default (items are loaded into it
     *     as that object in the list is actually accessed)
     * @see IDynamoDBMapper#scan(Class, DynamoDBScanExpression, DynamoDBMapperConfig)
     */
    public <T> PaginatedScanList<T> scan(Class<T> pojo, DynamoDBScanExpression scanExpression) {
        return mapper.scan(pojo, scanExpression);
    }

    /**
     * Scans a DynamoDB table and returns the matching results in an unmodifiable list.
     * Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#scan(ScanRequest)}.
     *
     * @param pojo           the class to scan, corresponding to a DynamoDB table
     * @param scanExpression details on how to run the scan and filter results
     * @param config         is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *                       but can be specified here.
     * @param <T>            the type of the objects being returned
     * @return an unmodifiable list, which is lazily loaded by default (items are loaded into it
     *     as that object in the list is actually accessed)
     * @see IDynamoDBMapper#scan(Class, DynamoDBScanExpression, DynamoDBMapperConfig)
     */
    public <T> PaginatedScanList<T> scan(Class<T> pojo,
                                         DynamoDBScanExpression scanExpression,
                                         DynamoDBMapperConfig config) {
        return mapper.scan(pojo, scanExpression, config);
    }

    /**
     * Scans a DynamoDB table and returns a single page of results. Under the hood,
     * the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#scan(ScanRequest)}.
     *
     * @param pojo           the class to scan, corresponding to a DynamoDB table
     * @param scanExpression details on how to run the scan and filter results
     * @param <T>            the type of the objects being returned
     * @return a page of results
     * @see IDynamoDBMapper#scanPage(Class, DynamoDBScanExpression, DynamoDBMapperConfig)
     */
    public <T> ScanResultPage<T> scanPage(Class<T> pojo, DynamoDBScanExpression scanExpression) {
        return mapper.scanPage(pojo, scanExpression);
    }

    /**
     * Scans a DynamoDB table and returns a single page of results. Under the hood,
     * the DynamoDBMapper being delegated to uses {@link AmazonDynamoDB#scan(ScanRequest)}.
     *
     * @param pojo           the class to scan, corresponding to a DynamoDB table
     * @param scanExpression details on how to run the scan and filter results
     * @param config         is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *                       but can be specified here.
     * @param <T>            the type of the objects being returned
     * @return a page of results
     * @see IDynamoDBMapper#scanPage(Class, DynamoDBScanExpression, DynamoDBMapperConfig)
     */
    public <T> ScanResultPage<T> scanPage(Class<T> pojo,
                                          DynamoDBScanExpression scanExpression,
                                          DynamoDBMapperConfig config) {
        return mapper.scanPage(pojo, scanExpression, config);
    }

    /**
     * Deletes a given item from DynamoDB. Additionally, it returns the item that was
     * deleted. Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#deleteItem(DeleteItemRequest)}.
     *
     * @param pojo an object of a class corresponding to a DynamoDB table. To be deleted.
     * @param <T>  the type of the objects being returned
     * @return the item that was deleted
     * @see IDynamoDBMapper#delete(Object, DynamoDBDeleteExpression, DynamoDBMapperConfig)
     */
    public <T> T delete(T pojo) {
        mapper.delete(pojo);
        return pojo;
    }

    /**
     * Deletes a given item from DynamoDB. Additionally, it returns the item that was
     * deleted. Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#deleteItem(DeleteItemRequest)}.
     *
     * @param pojo             an object of a class corresponding to a DynamoDB table. To be deleted.
     * @param deleteExpression details on how to run the delete operation
     * @param <T>              the type of the objects being returned
     * @return the item that was deleted
     * @see IDynamoDBMapper#delete(Object, DynamoDBDeleteExpression, DynamoDBMapperConfig)
     */
    public <T> T delete(T pojo, DynamoDBDeleteExpression deleteExpression) {
        mapper.delete(pojo, deleteExpression);
        return pojo;
    }

    /**
     * Deletes a given item from DynamoDB. Additionally, it returns the item that was
     * deleted. Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#deleteItem(DeleteItemRequest)}.
     *
     * @param pojo   an object of a class corresponding to a DynamoDB table. To be deleted.
     * @param config is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *               but can be specified here.
     * @param <T>    the type of the objects being returned
     * @return the item that was deleted
     * @see IDynamoDBMapper#delete(Object, DynamoDBDeleteExpression, DynamoDBMapperConfig)
     */
    public <T> T delete(T pojo, DynamoDBMapperConfig config) {
        mapper.delete(pojo, config);
        return pojo;
    }

    /**
     * Deletes a given item from DynamoDB. Additionally, it returns the item that was
     * deleted. Under the hood, the DynamoDBMapper being delegated to uses
     * {@link AmazonDynamoDB#deleteItem(DeleteItemRequest)}.
     *
     * @param pojo             an object of a class corresponding to a DynamoDB table. To be deleted.
     * @param deleteExpression details on how to run the delete operation
     * @param config           is set to DEFAULT: {@link DynamoDBMapperConfig#DEFAULT} normally
     *                         but can be specified here.
     * @param <T>              the type of the objects being returned
     * @return the item that was deleted
     * @see IDynamoDBMapper#delete(Object, DynamoDBDeleteExpression, DynamoDBMapperConfig)
     */
    public <T> T delete(T pojo,
                        DynamoDBDeleteExpression deleteExpression,
                        DynamoDBMapperConfig config) {
        mapper.delete(pojo, deleteExpression, config);
        return pojo;
    }

    /**
     * Saves and deletes the objects given using one or more calls to the
     * {@link AmazonDynamoDB#batchWriteItem(BatchWriteItemRequest)} API.
     * Cannot have more than 25 requests in the batch.
     *
     * @param objectsToWrite  a list of objects to save to DynamoDB.
     * @param objectsToDelete a list of objects to delete from DynamoDB.
     * @return a list of failed batches which includes the unprocessed items and the exceptions
     *     causing the failure.
     * @see AmazonDynamoDB#batchWriteItem(BatchWriteItemRequest)
     */
    public List<DynamoDBMapper.FailedBatch> batchWrite(Iterable<?> objectsToWrite,
                                                       Iterable<?> objectsToDelete) {
        return mapper.batchWrite(objectsToWrite, objectsToDelete);
    }
}
