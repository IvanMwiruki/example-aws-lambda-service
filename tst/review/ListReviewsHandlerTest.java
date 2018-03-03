package review;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.collect.ImmutableMap;
import exceptions.UserNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListReviewsHandlerTest {

    private static final String USERID_PARAMETER_KEY = "userId";
    private static final String USERID_PARAMETER_VALUE = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String COUNT_QUERY_PARAMETER_KEY = "count";
    private static final String COUNT_QUERY_PARAMETER_VALUE = "10";
    private static final String PAGINATION_TOKEN_QUERY_PARAMETER_KEY = "10";
    private static final String PAGINATION_TOKEN_QUERY_PARAMETER_VALUE = "tt0000300#2018-01-05T00:34:18.467Z";

    private ReviewService service;
    private Map<String, String> pathParameters;
    private Map<String, String> queryParameters;
    private APIGatewayProxyRequestEvent request;
    private ListReviewsHandler handler;

    @Before
    public void setUp() {
        service = mock(ReviewService.class);
        queryParameters = ImmutableMap.of(
                COUNT_QUERY_PARAMETER_KEY, COUNT_QUERY_PARAMETER_VALUE,
                PAGINATION_TOKEN_QUERY_PARAMETER_KEY, PAGINATION_TOKEN_QUERY_PARAMETER_VALUE);
        pathParameters = ImmutableMap.of(
                USERID_PARAMETER_KEY, USERID_PARAMETER_VALUE);
        request = new APIGatewayProxyRequestEvent();
        request.setPathParameters(pathParameters);
        request.setQueryStringParameters(queryParameters);

        handler = new ListReviewsHandler();
    }

    @Test
    public void listReviewsWithUserIDAndCountAndPaginationToken() {
        final APIGatewayProxyResponseEvent result = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_OK, result.getStatusCode().intValue());
    }

    @Test
    public void catchIllegalArgumentException() throws Exception {
        when(service.listReviews(any(ListReviewsRequest.class))).thenThrow(new IllegalArgumentException());

        final APIGatewayProxyResponseEvent result = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_BAD_REQUEST, result.getStatusCode().intValue());
    }

    @Test
    public void catchUserNotFoundException() throws Exception {
        when(service.listReviews(any(ListReviewsRequest.class)))
                .thenThrow(new UserNotFoundException("User not found."));

        final APIGatewayProxyResponseEvent result = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_NOT_FOUND, result.getStatusCode().intValue());
    }

    @Test
    public void catchException() throws Exception {
        when(service.listReviews(any(ListReviewsRequest.class))).thenThrow(new RuntimeException());

        final APIGatewayProxyResponseEvent result = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getStatusCode().intValue());
    }
}
