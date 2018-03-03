package review;

import java.util.List;

/**
 * Represents an immutable response.
 * Built after a request to list an Amazon Videos user's reviews has been processed.
 */
public class ListReviewsResponse {

    private final List<Review> results;
    private final String paginationToken;

    public ListReviewsResponse(List<Review> results, String paginationToken) {
        this.results = results;
        this.paginationToken = paginationToken;
    }
}
