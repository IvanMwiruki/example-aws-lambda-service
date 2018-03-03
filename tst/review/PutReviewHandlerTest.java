package review;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.common.collect.ImmutableMap;
import exceptions.InvalidRatingException;
import exceptions.UserNotFoundException;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PutReviewHandlerTest {

    private static final String VALID_BODY = "{\"rating\": 4.5}";
    private static final String INVALID_BODY_CANNOT_BE_DESERIALIZED = "{\"rating\": haha}";
    private static final String USERID_PARAMETER_KEY = "userId";
    private static final String USERID_PARAMETER_VALUE = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String IMDBID_PARAMETER_KEY = "imdbId";
    private static final String IMDBID_PARAMETER_VALUE = "tt0000036";

    private ReviewService service;
    private Map<String, String> pathParameters;
    private APIGatewayProxyRequestEvent request;
    private Review review;
    private PutReviewHandler handler;

    @Before
    public void setUp() {
        service = mock(ReviewService.class);
        pathParameters = ImmutableMap.of(
                USERID_PARAMETER_KEY, USERID_PARAMETER_VALUE,
                IMDBID_PARAMETER_KEY, IMDBID_PARAMETER_VALUE
        );
        request = new APIGatewayProxyRequestEvent();
        request.setPathParameters(pathParameters);
        review = new Review();

        handler = new PutReviewHandler();
    }

    @Test
    public void putReview() throws Exception {
        request.setBody(VALID_BODY);
        when(service.put(any(Review.class))).thenReturn(review);

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_OK, response.getStatusCode().intValue());
    }

    @Test
    public void missingBody() {
        request.setBody(null);

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void invalidRatingInBodyCannotBeDeserialized() {
        request.setBody(INVALID_BODY_CANNOT_BE_DESERIALIZED);

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void catchIllegalArgumentException() throws Exception {
        request.setBody(VALID_BODY);
        when(service.put(any(Review.class))).thenThrow(new IllegalArgumentException());

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void catchInvalidRatingException() throws Exception {
        request.setBody(VALID_BODY);
        when(service.put(any(Review.class))).thenThrow(new InvalidRatingException("Invalid rating."));

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().intValue());
    }

    @Test
    public void catchResourceNotFoundException() throws Exception {
        request.setBody(VALID_BODY);
        when(service.put(any(Review.class))).thenThrow(new UserNotFoundException("User not found."));

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode().intValue());
    }

    @Test
    public void catchException() throws Exception {
        request.setBody(VALID_BODY);
        when(service.put(any(Review.class))).thenThrow(new RuntimeException());

        final APIGatewayProxyResponseEvent response = handler.handleRequest(request, service);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode().intValue());
    }
}
