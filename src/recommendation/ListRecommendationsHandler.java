package recommendation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import exceptions.UserNotFoundException;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * Handles listing movie recommendations for an Amazon Videos user.
 */
public class ListRecommendationsHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String MESSAGE = "{\"message\" : \"%s\"}";

    private final Logger log = Logger.getLogger(ListRecommendationsHandler.class);
    private final AppConfig appConfig = new AppConfig();
    private final ObjectMapper mapper = appConfig.getObjectMapper();
    private final RecommendationService service = appConfig.getRecommendationService();

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
    APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input,
                                               RecommendationService recommendationService) {
        final String userId = input.getPathParameters().get("userId");

        try {
            final List<?> response =
                    recommendationService.listRecommendations(userId);

            return new APIGatewayProxyResponseEvent()
                    .withBody(mapper.writeValueAsString(response))
                    .withStatusCode(HttpStatus.SC_OK);
        }
        catch (UserNotFoundException e) {
            log.warn(String.format("The requested user {%s} could not be found.", userId), e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, e.getMessage()))
                    .withStatusCode(HttpStatus.SC_NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Internal service error: Unable to list recommendations.", e);
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format(MESSAGE, "Internal service error.  Please try again."))
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
