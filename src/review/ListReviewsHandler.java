package review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import exceptions.UserNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * Handles listing an Amazon Videos user's reviews.
 */
public class ListReviewsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String MESSAGE = "{\"message\" : \"%s\"}";

    /*
    ATA: Copy-past mistake? Should be ListReviewsHandler.class
     */
    private final Logger log = Logger.getLogger(ListReviewsHandler.class);
    private final AppConfig appConfig = new AppConfig();
    private final ObjectMapper mapper = appConfig.getObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    private final ReviewService service = appConfig.getReviewService();

    /**
     * Handles a Lambda Function request.
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return handleRequest(input, service);
    }

    @VisibleForTesting
    APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, ReviewService reviewService) {
        final String userId = input.getPathParameters().get("userId");

        final Map<String, String> queryParameters = input.getQueryStringParameters();
        final String count = queryParameters == null ? null : queryParameters.get("count");
        final String paginationToken = queryParameters == null ? null : queryParameters.get("paginationToken");

        try {
            final ListReviewsResponse response =
                    reviewService.listReviews(ListReviewsRequest.of(userId, count, paginationToken));

            return new APIGatewayProxyResponseEvent()
                    .withBody(mapper.writeValueAsString(response))
                    .withStatusCode(HttpStatus.SC_OK);
        }
        catch (IllegalArgumentException e) {
            log.warn("Invalid query parameter/s.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        catch (UserNotFoundException e) {
            log.warn(String.format("The requested user {%s} could not be found.", userId), e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Internal service error: Unable to list reviews.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, "Internal service error.  Please try again."))
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
