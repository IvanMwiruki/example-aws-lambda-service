package review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import exceptions.ReviewNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * Handles deleting an Amazon Videos review. Deleted reviews are written to a separate table.
 */
public class DeleteReviewHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String MESSAGE = "{\"message\" : \"%s\"}";

    private final Logger log = Logger.getLogger(DeleteReviewHandler.class);
    private final AppConfig appConfig = new AppConfig();
    private final ObjectMapper mapper = appConfig.getObjectMapper();
    private final ReviewService service = appConfig.getReviewService();

    /**
     * Handles a Lambda Function request.
     *
     * @param input The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return handleRequest(input, service);
    }

    @VisibleForTesting
    APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, ReviewService reviewService) {
        final Map<String, String> pathParameters = input.getPathParameters();
        final String userId = pathParameters.get("userId");
        final String imdbId = pathParameters.get("imdbId");

        try {
            final Review reviewResponse = reviewService.delete(userId, imdbId);
            return new APIGatewayProxyResponseEvent()
                    .withBody(mapper.writeValueAsString(reviewResponse))
                    .withStatusCode(HttpStatus.SC_OK);
        }
        catch (ReviewNotFoundException e) {
            log.warn("Review not found.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Internal service error: Unable to add review.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, "Internal service error.  Please try again."))
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
