package review;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import dynamodb.DynamoDBMapperWrapper;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewDAOTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String IMDBID = "tt0000036";
    private static final double VALID_RATING = 4.5;

    private DynamoDBMapperWrapper mapper;
    private Review review;
    private ReviewDAO dao;

    @Before
    public void setUp() {
        mapper = mock(DynamoDBMapperWrapper.class);
        review = new Review();
        review.setUserId(USERID);
        review.setImdbId(IMDBID);
        review.setRating(VALID_RATING);

        dao = new ReviewDAO(mapper);
    }

    @Test
    public void fetchReviewWithReviewObjectThatExists() {
        when(mapper.load(review)).thenReturn(Optional.of(review));

        final Optional<Review> result = dao.fetch(review);

        assertEquals(review, result.get());
    }

    @Test
    public void fetchReviewWithReviewObjectDoesntExist() {
        when(mapper.load(review)).thenReturn(Optional.empty());

        final Optional<Review> result = dao.fetch(review);

        assertFalse(result.isPresent());
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchReviewWithReviewObjectDynamoException() {
        when(mapper.load(review)).thenThrow(new AmazonDynamoDBException("test"));

        dao.fetch(review);
    }

    @Test
    public void fetchReviewWithUserIdImdbIdThatExists() {
        when(mapper.load(Review.class, USERID, IMDBID)).thenReturn(Optional.of(review));

        final Optional<Review> result = dao.fetch(USERID, IMDBID);

        assertEquals(review, result.get());
    }

    @Test
    public void fetchReviewWithUserIdImdbIdDoesntExist() {
        when(mapper.load(Review.class, USERID, IMDBID)).thenReturn(Optional.empty());

        final Optional<Review> result = dao.fetch(USERID, IMDBID);

        assertFalse(result.isPresent());
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchReviewWithUserIdImdbIdDynamoException() {
        when(mapper.load(Review.class, USERID, IMDBID)).thenThrow(new AmazonDynamoDBException("test"));

        dao.fetch(USERID, IMDBID);
    }

    @Test
    public void saveReview() {
        when(mapper.save(review)).thenReturn(review);

        final Review result = dao.save(review);

        assertEquals(review, result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void saveReviewDynamoException() {
        when(mapper.save(review)).thenThrow(new AmazonDynamoDBException("test"));

        dao.save(review);
    }

    @Test
    public void deleteReview() {
        when(mapper.delete(review)).thenReturn(review);

        final Review result = dao.delete(review);

        assertEquals(review, result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void deleteReviewDynamoException() {
        when(mapper.delete(review)).thenThrow(new AmazonDynamoDBException("test"));

        dao.delete(review);
    }

    @Test
    public void createListReviewsResponse() {
        when(mapper.queryPage(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(new QueryResultPage<>());

        dao.createListReviewsResponse(USERID, ListReviewsRequest.MAXIMUM_COUNT, null);

        verify(mapper).queryPage(eq(Review.class), any(DynamoDBQueryExpression.class));
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void createListReviewsResponseDynamoException() {
        when(mapper.queryPage(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.createListReviewsResponse(USERID, ListReviewsRequest.MAXIMUM_COUNT, null);
    }

    @Test
    public void fetchLatestFavorite() {
        final QueryResultPage<Review> reviewPage = mock(QueryResultPage.class);
        when(mapper.queryPage(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(reviewPage);
        when(reviewPage.getResults()).thenReturn(Collections.singletonList(review));

        final Optional<Review> result = dao.fetchLatestFavorite(USERID, VALID_RATING);

        assertEquals(Optional.of(review), result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchLatestFavoriteDynamoException() {
        when(mapper.queryPage(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.fetchLatestFavorite(USERID, VALID_RATING);
    }

    @Test
    public void listReviewedMovies() {
        final PaginatedQueryList<Review> reviews = mock(PaginatedQueryList.class);
        when(mapper.query(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(reviews);
        when(reviews.stream()).thenReturn(Collections.singletonList(review).stream());

        final Set<String> result = dao.listReviewedMovies(USERID);

        assertTrue(result.contains(review.getImdbId()));
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void listReviewedMoviesDynamoException() {
        when(mapper.query(eq(Review.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.listReviewedMovies(USERID);
    }
}
