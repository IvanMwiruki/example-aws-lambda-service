package review;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.collect.ImmutableMap;
import exceptions.ReviewNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteReviewHandlerTest {
    private static final String USERID_PARAMETER_KEY = "userId";
    private static final String USERID_PARAMETER_VALUE = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String IMDBID_PARAMETER_KEY = "imdbId";
    private static final String IMDBID_PARAMETER_VALUE = "tt0000036";

    private ReviewService service;
    private Map<String, String> pathParameters;
    private APIGatewayProxyRequestEvent request;
    private Review review;
    private DeleteReviewHandler handler;

    @Before
    public void setUp() {
        service = mock(ReviewService.class);
        pathParameters = ImmutableMap.of(
                USERID_PARAMETER_KEY, USERID_PARAMETER_VALUE,
                IMDBID_PARAMETER_KEY, IMDBID_PARAMETER_VALUE);
        request = new APIGatewayProxyRequestEvent();
        request.setPathParameters(pathParameters);
        review = new Review();

        handler = new DeleteReviewHandler();
    }

    @Test
    public void deleteReview() throws ReviewNotFoundException {
        when(service.delete(USERID_PARAMETER_VALUE, IMDBID_PARAMETER_VALUE)).thenReturn(review);

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }

    @Test
    public void catchReviewNotFoundException() throws ReviewNotFoundException {
        when(service.delete(USERID_PARAMETER_VALUE, IMDBID_PARAMETER_VALUE))
                .thenThrow(new ReviewNotFoundException("Review not found."));

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode().intValue());
    }

    @Test
    public void catchException() throws Exception {
        when(service.delete(USERID_PARAMETER_VALUE, IMDBID_PARAMETER_VALUE))
                .thenThrow(new RuntimeException());

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode().intValue());
    }
}
