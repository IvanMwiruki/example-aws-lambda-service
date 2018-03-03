package recommendation;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.collect.ImmutableMap;
import exceptions.UserNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListRecommendationsHandlerTest {

    private static final String USERID_PARAMETER_KEY = "userId";
    private static final String USERID_PARAMETER_VALUE = "d2fadc3b-b791-4054-b51e-49be4beb24c7";

    private RecommendationService service;
    private Map<String, String> pathParameters;
    private APIGatewayProxyRequestEvent request;
    private ListRecommendationsHandler handler;

    @Before
    public void setUp() {
        service = mock(RecommendationService.class);
        pathParameters = ImmutableMap.of(
                USERID_PARAMETER_KEY, USERID_PARAMETER_VALUE
        );
        request = new APIGatewayProxyRequestEvent();
        request.setPathParameters(pathParameters);

        handler = new ListRecommendationsHandler();
    }

    @Test
    public void listRecommendations() {
        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }

    @Test
    public void catchUserNotFoundException() throws UserNotFoundException {
        when(service.listRecommendations(USERID_PARAMETER_VALUE))
                .thenThrow(new UserNotFoundException("User not found."));

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode().intValue());
    }

    @Test
    public void catchException() throws UserNotFoundException {
        when(service.listRecommendations(USERID_PARAMETER_VALUE))
                .thenThrow(new RuntimeException());

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode().intValue());
    }
}
