package review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import exceptions.InvalidRatingException;
import exceptions.ResourceNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * Handles creating and updating an Amazon Videos user's review.
 */
public class PutReviewHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String MESSAGE = "{\"message\" : \"%s\"}";

    private final Logger log = Logger.getLogger(PutReviewHandler.class);
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

        final Review reviewRequest;
        try {
            if (input.getBody() == null) {
                throw new IOException("Request is missing body. Body should contain the rating of the review.");
            }
            reviewRequest = mapper.readValue(input.getBody(), Review.class);
            reviewRequest.setUserId(userId);
            reviewRequest.setImdbId(imdbId);
        }
        catch (IOException e) {
            log.warn("Unable to map request body to Review object.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST);
        }

        try {
            final Review reviewResponse = reviewService.put(reviewRequest);
            return new APIGatewayProxyResponseEvent()
                    .withBody(mapper.writeValueAsString(reviewResponse))
                    .withStatusCode(HttpStatus.SC_OK);
        }
        catch (NullPointerException | IllegalArgumentException | InvalidRatingException e) {
            log.warn("Invalid Review object: invalid rating.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        catch (ResourceNotFoundException e) {
            log.warn("Invalid Review object: resource could not be found.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Internal service error: Unable to add review.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, "Internal service error. Please try again."))
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
