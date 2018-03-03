package review;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import java.util.Map;

/**
 * Manages deleted Amazon Videos reviews.
 */
public class DeletedReviewService {

    public static final String USERID = "userId";
    public static final String IMDBID = "imdbId";
    public static final String RATING = "rating";
    public static final String CREATEDAT = "createdAt";
    public static final String UPDATEDAT = "updatedAt";

    private final DeletedReviewDAO deletedReviewDAO;

    public DeletedReviewService(DeletedReviewDAO deletedReviewDAO) {
        this.deletedReviewDAO = deletedReviewDAO;
    }

    /**
     * Saves a deleted review.
     *
     * @param review the review to be saved
     * @return the saved review
     */
    public DeletedReview save(DeletedReview review) {
        return deletedReviewDAO.save(review);
    }

    /**
     * Returns the DeletedReview representation of a {@code DynamodbStreamRecord} argument.
     *
     * @param record a {@code DynamodbStreamRecord}
     * @return the DeletedReview representation of the given DynamodbStreamRecord
     */
    public DeletedReview valueOf(DynamodbEvent.DynamodbStreamRecord record) {
        final Map<String, AttributeValue> values = record.getDynamodb().getOldImage();

        return new DeletedReview(
                values.get(USERID).getS(),
                values.get(IMDBID).getS(),
                Double.parseDouble(values.get(RATING).getN()),
                values.get(CREATEDAT).getS(),
                values.get(UPDATEDAT).getS()
                );
    }
}
