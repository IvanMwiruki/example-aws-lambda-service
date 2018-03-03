package review;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.google.common.collect.ImmutableMap;
import dynamodb.DynamoDBMapperWrapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores, updates, retrieves, and deletes movie reviews from DynamoDB.
 */
public class ReviewDAO {

    private final DynamoDBMapperWrapper mapper;

    public ReviewDAO(DynamoDBMapperWrapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Fetch a review by its userId and imbdId (both contained in the given Review object).
     *
     * @param review the movie review to retrieve
     * @return an Optional of the Review specified
     */
    public Optional<Review> fetch(Review review) {
        return mapper.load(review);
    }

    /**
     * Fetch a review by its userId and imbdId.
     *
     * @param userId the userId of the review
     * @param imdbId the imdbId of the review
     * @return an Optional of the Review specified
     */
    public Optional<Review> fetch(String userId, String imdbId) {
        return mapper.load(Review.class, userId, imdbId);
    }

    /**
     * Create a new review.
     *
     * @param review the review to be created
     * @return the review that was created
     * @throws ConditionalCheckFailedException if Dynamo encountered a version conflict
     */
    public Review save(Review review) throws ConditionalCheckFailedException {
        return mapper.save(review);
    }

    /**
     * Delete a review.
     *
     * @param toDelete the review to delete
     * @return the deleted review
     */
    public Review delete(Review toDelete) {
        return mapper.delete(toDelete);
    }

    /**
     * Returns a ListReviewsResponse representing the given values.
     *
     * @param userId the userId of the reviews
     * @param count used to limit the number of results that will be returned
     * @param lastEvaluatedKey used to fetch the next page of results
     * @return a ListReviewsResponse
     */
    public ListReviewsResponse createListReviewsResponse(String userId,
                                                                int count,
                                                                Map<String, AttributeValue> lastEvaluatedKey) {
        final QueryResultPage queryResult = listReviews(userId, count, lastEvaluatedKey);
        final List<Review> reviews = queryResult.getResults();
        final Map<String, AttributeValue> newLastEvaluatedKey = queryResult.getLastEvaluatedKey();
        final String paginationToken = newLastEvaluatedKey == null
                                       ? "No more results."
                                       : createToken(newLastEvaluatedKey);

        return new ListReviewsResponse(reviews, paginationToken);
    }

    /* Converts a "last evaluated key" from DynamoDB to a String pagination token */
    private String createToken(Map<String, AttributeValue> lastEvaluatedKey) {
        final String imdbId = lastEvaluatedKey.get("imdbId").getS();
        final String createdAt = lastEvaluatedKey.get("createdAt").getS();
        return imdbId + "#" + createdAt;
    }

    private QueryResultPage<Review> listReviews(String userId,
                                               int count,
                                               Map<String, AttributeValue> lastEvaluatedKey) {
        final Map<String, AttributeValue> expressionAttributeValues = ImmutableMap.of(
                ":id", new AttributeValue(userId)
        );

        final DynamoDBQueryExpression<Review> queryExpression = new DynamoDBQueryExpression<Review>()
                .withIndexName(Review.USER_ID_TO_CREATED_AT_INDEX)
                .withKeyConditionExpression("userId = :id")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withLimit(count)
                .withExclusiveStartKey(lastEvaluatedKey)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return mapper.queryPage(Review.class, queryExpression);
    }

    /**
     * Fetch the latest review with the given minimum rating.
     *
     * @param userId the userId of the review
     * @return an Optional of the "latest favorite" review
     */
    public Optional<Review> fetchLatestFavorite(String userId, double minimumRating) {
        final Map<String, AttributeValue> expressionAttributeValues = ImmutableMap.of(
                ":id", new AttributeValue(userId),
                ":minimumRating", new AttributeValue().withN(Double.toString(minimumRating))
        );

        final DynamoDBQueryExpression<Review> queryExpression = new DynamoDBQueryExpression<Review>()
                .withIndexName(Review.USER_ID_TO_CREATED_AT_INDEX)
                .withKeyConditionExpression("userId = :id")
                .withFilterExpression("rating >= :minimumRating")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return mapper.queryPage(Review.class, queryExpression)
                .getResults()
                .stream()
                .findFirst();
    }

    /**
     * Returns a set of Strings containing the imdbIds of all the movies
     * the given user has reviewed.
     *
     * @param userId the userId of the reviews
     * @return a Set of Strings of imdbIds
     */
    public Set<String> listReviewedMovies(String userId) {
        final Map<String, AttributeValue> expressionAttributeValues = ImmutableMap.of(
                ":id", new AttributeValue(userId)
        );

        final DynamoDBQueryExpression<Review> queryExpression = new DynamoDBQueryExpression<Review>()
                .withKeyConditionExpression("userId = :id")
                .withExpressionAttributeValues(expressionAttributeValues);

        return mapper.query(Review.class, queryExpression)
                .stream()
                .map(Review::getImdbId)
                .collect(Collectors.toSet());
    }
}
