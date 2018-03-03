package review;

import exceptions.InvalidRatingException;
import exceptions.MovieNotFoundException;
import exceptions.ReviewNotFoundException;
import exceptions.UserNotFoundException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import movie.MovieService;
import org.junit.Before;
import org.junit.Test;
import user.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String IMDBID = "tt0000036";
    private static final double VALID_RATING = 4.5;
    private static final double INVALID_RATING = 0.0;

    private UserService userService;
    private MovieService movieService;
    private ReviewDAO reviewDAO;
    private Review review;
    private ReviewService service;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        movieService = mock(MovieService.class);
        reviewDAO = mock(ReviewDAO.class);
        review = new Review();
        review.setUserId(USERID);
        review.setImdbId(IMDBID);
        review.setRating(VALID_RATING);

        service = new ReviewService(userService, movieService, reviewDAO);
    }

    @Test
    public void putNewReview() throws Exception {
        when(reviewDAO.fetch(review)).thenReturn(Optional.empty());
        when(movieService.movieExists(anyString())).thenReturn(true);
        when(reviewDAO.save(review)).thenReturn(review);

        final Review result = service.put(review);

        verify(reviewDAO).fetch(review);
        verify(reviewDAO).save(review);
        assertEquals(review, result);
    }

    @Test
    public void putUpdateReview() throws Exception {
        final double oldRating = 3.5;
        final Review oldReview = new Review();
        review.setRating(oldRating);
        when(reviewDAO.fetch(review)).thenReturn(Optional.of(oldReview));
        when(movieService.movieExists(anyString())).thenReturn(true);
        when(reviewDAO.save(oldReview)).thenReturn(review);

        final Review result = service.put(review);

        verify(reviewDAO).fetch(review);
        verify(reviewDAO).save(oldReview);
        assertEquals(review, result);
        assertEquals(oldReview.getRating(), result.getRating());
    }

    @Test(expected = NullPointerException.class)
    public void putNullRating() throws Exception {
        service.put(null);
    }

    @Test(expected = InvalidRatingException.class)
    public void putInvalidRating() throws Exception {
        review.setRating(INVALID_RATING);
        when(reviewDAO.fetch(review)).thenReturn(Optional.empty());

        service.put(review);
    }

    @Test(expected = MovieNotFoundException.class)
    public void putMovieNotFound() throws Exception {
        when(reviewDAO.fetch(review)).thenReturn(Optional.empty());
        when(movieService.movieExists(IMDBID)).thenReturn(false);

        service.put(review);
    }

    @Test(expected = UserNotFoundException.class)
    public void putUserNotFound() throws Exception {
        when(reviewDAO.fetch(review)).thenReturn(Optional.empty());
        when(userService.getUser(USERID)).thenThrow(new UserNotFoundException("User not found."));

        service.put(review);
    }

    @Test
    public void deleteReview() throws Exception {
        when(reviewDAO.fetch(USERID, IMDBID)).thenReturn(Optional.of(review));
        when(reviewDAO.delete(review)).thenReturn(review);

        final Review result = service.delete(USERID, IMDBID);

        verify(reviewDAO).fetch(USERID, IMDBID);
        verify(reviewDAO).delete(result);
        assertEquals(review, result);

    }

    @Test(expected = ReviewNotFoundException.class)
    public void deleteReviewNotFound() throws Exception {
        when(reviewDAO.fetch(USERID, IMDBID)).thenReturn(Optional.empty());

        service.delete(USERID, IMDBID);
    }

    @Test
    public void listReviews() throws Exception {
        final ListReviewsRequest request = ListReviewsRequest.of(USERID, null, null);
        when(reviewDAO.createListReviewsResponse(USERID, ListReviewsRequest.MAXIMUM_COUNT, null))
                .thenReturn(new ListReviewsResponse(null, null));

        service.listReviews(request);

        verify(userService).getUser(USERID);
        verify(reviewDAO).createListReviewsResponse(USERID, ListReviewsRequest.MAXIMUM_COUNT, null);

    }

    @Test(expected = UserNotFoundException.class)
    public void listReviewsUserNotFound() throws Exception {
        final ListReviewsRequest request = ListReviewsRequest.of(USERID, null, null);
        when(userService.getUser(USERID)).thenThrow(new UserNotFoundException("User not found."));

        service.listReviews(request);
    }

    @Test
    public void fetchLatestFavorite() throws Exception {
        when(reviewDAO.fetchLatestFavorite(USERID, ReviewService.MIN_RATING)).thenReturn(Optional.of(review));

        final Optional<Review> result = service.fetchLatestFavorite(USERID);

        verify(userService).getUser(USERID);
        assertEquals(review, result.get());
    }

    @Test(expected = UserNotFoundException.class)
    public void fetchLatestFavoriteUserNotFound() throws Exception {
        when(userService.getUser(USERID)).thenThrow(new UserNotFoundException("User not found."));

        service.fetchLatestFavorite(USERID);
    }

    @Test
    public void listReviewedMovies() throws Exception {
        final Set<String> movies = Collections.singleton(IMDBID);
        when(reviewDAO.listReviewedMovies(USERID)).thenReturn(movies);

        final Set<String> result = service.listReviewedMovies(USERID);

        verify(userService).getUser(USERID);
        verify(reviewDAO).listReviewedMovies(USERID);
        assertEquals(movies, result);
    }

    @Test(expected = UserNotFoundException.class)
    public void listReviewedMoviesUserNotFound() throws Exception {
        when(userService.getUser(USERID)).thenThrow(new UserNotFoundException("User not found."));

        service.listReviewedMovies(USERID);
    }
}
