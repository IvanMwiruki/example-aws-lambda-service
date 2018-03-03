package review;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListReviewsRequestTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String VALID_COUNT = "100";
    private static final String PAGINATION_TOKEN = "tt0000414#2018-01-05T00:24:49.969Z";

    @Test
    public void createListReviewsRequest() {
        final ListReviewsRequest result = ListReviewsRequest.of(USERID, VALID_COUNT, PAGINATION_TOKEN);

        assertEquals(USERID, result.getUserId());
        assertEquals(VALID_COUNT, result.getCount().toString());
        assertTrue(result.getLastEvaluatedKey().containsKey("userId"));
        assertTrue(result.getLastEvaluatedKey().containsKey("imdbId"));
        assertTrue(result.getLastEvaluatedKey().containsKey("createdAt"));
    }

    @Test
    public void createListReviewsRequestNullCount() {
        final ListReviewsRequest result = ListReviewsRequest.of(USERID, null, PAGINATION_TOKEN);

        assertEquals(USERID, result.getUserId());
        assertEquals(ListReviewsRequest.MAXIMUM_COUNT, result.getCount().intValue());
        assertTrue(result.getLastEvaluatedKey().containsKey("userId"));
        assertTrue(result.getLastEvaluatedKey().containsKey("imdbId"));
        assertTrue(result.getLastEvaluatedKey().containsKey("createdAt"));
    }

    @Test
    public void createListReviewsRequestNullPaginationToken() {
        final ListReviewsRequest result = ListReviewsRequest.of(USERID, VALID_COUNT, null);

        assertEquals(USERID, result.getUserId());
        assertEquals(VALID_COUNT, result.getCount().toString());
        assertTrue(result.getLastEvaluatedKey() == null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createListReviewsRequestIllegalCountLessThan0() {
        ListReviewsRequest.of(USERID, "0", PAGINATION_TOKEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createListReviewsRequestIllegalGreaterThanMax() {
        ListReviewsRequest.of(USERID, "1000", PAGINATION_TOKEN);
    }

}
