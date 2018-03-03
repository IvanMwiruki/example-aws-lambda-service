package review;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import dynamodb.DynamoDBMapperWrapper;

/**
 * Stores deleted Amazon Videos reviews to DynamoDB.
 */
public class DeletedReviewDAO {

    private final DynamoDBMapperWrapper mapper;

    public DeletedReviewDAO(DynamoDBMapperWrapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Saves a deleted review.
     *
     * @param review the review to be saved
     * @return the saved review
     * @throws ConditionalCheckFailedException if Dynamo encountered a version conflict
     */
    public DeletedReview save(DeletedReview review) {
        return mapper.save(review);
    }
}
