package review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

/**
 * Handles writing deleted reviews to the database.
 */
public class DeletedReviewHandler implements RequestHandler<DynamodbEvent, String> {

    public static final String DELETED_EVENT_NAME = "REMOVE";

    private final Logger log = Logger.getLogger(DeletedReviewHandler.class);
    private final AppConfig appConfig = new AppConfig();
    private final DeletedReviewService service = appConfig.getDeletedReviewService();

    /**
     * Handles a Lambda Function request.
     *
     * @param input The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public String handleRequest(DynamodbEvent input, Context context) {
        return handleRequest(input, service);
    }

    @VisibleForTesting
    String handleRequest(DynamodbEvent input, DeletedReviewService deletedReviewService) {
        try {
            final List<DeletedReview> deletedReviews = listDeletedReviews(input, deletedReviewService);
            deletedReviews.forEach(deletedReviewService::save);
            return String.format("%d records were saved.", deletedReviews.size());
        }
        catch (Exception e) {
            log.error("Failed to write deleted review/s to database.", e);
            return "Failed to write deleted review/s to database.";
        }
    }

    private List<DeletedReview> listDeletedReviews(DynamodbEvent input, DeletedReviewService deletedReviewService) {
        return input.getRecords().stream()
                .filter(record -> record.getEventName().equals(DELETED_EVENT_NAME))
                .map(deletedReviewService::valueOf)
                .collect(Collectors.toList());
    }
}
