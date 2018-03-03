package recommendation;

import exceptions.UserNotFoundException;
import java.util.Optional;
import movie.Movie;
import movie.MovieService;
import movie.TopMovieService;
import org.junit.Before;
import org.junit.Test;
import review.Review;
import review.ReviewService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {

    private static final String USERID = "larry";
    private static final String IMDBID = "tt0000076";

    private MovieService movieService;
    private ReviewService reviewService;
    private TopMovieService topMovieService;
    private RecommendationService service;

    @Before
    public void setUp() {
        movieService = mock(MovieService.class);
        reviewService = mock(ReviewService.class);
        topMovieService = mock(TopMovieService.class);

        service = new RecommendationService(movieService, reviewService, topMovieService);
    }

    @Test
    public void listRecommendationsWithLatestFavorite() throws UserNotFoundException {
        final Review review = new Review();
        review.setUserId(USERID);
        review.setImdbId(IMDBID);
        final Optional<Review> reviewOptional = Optional.of(review);
        when(reviewService.fetchLatestFavorite(USERID))
                .thenReturn(reviewOptional);
        when(movieService.fetch(IMDBID)).thenReturn(Optional.of(new Movie()));

        service.listRecommendations(USERID);

        verify(reviewService).fetchLatestFavorite(USERID);
        verify(topMovieService, never()).listTopMovies(RecommendationService.MAX_RECOMMENDATIONS);
        verify(movieService).fetch(IMDBID);
        verify(reviewService).listReviewedMovies(USERID);
        verify(movieService).listMovies(anyString());
    }

    @Test
    public void listRecommendationsWithoutLatestFavorite() throws UserNotFoundException {
        when(reviewService.fetchLatestFavorite(USERID))
                .thenReturn(Optional.empty());

        service.listRecommendations(USERID);


        verify(reviewService).fetchLatestFavorite(USERID);
        verify(topMovieService).listTopMovies(RecommendationService.MAX_RECOMMENDATIONS);
        verify(movieService, never()).fetch(IMDBID);
        verify(reviewService, never()).listReviewedMovies(USERID);
        verify(movieService, never()).listMovies(anyString());
    }
}
