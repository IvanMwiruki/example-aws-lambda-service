package recommendation;

import exceptions.UserNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import movie.Movie;
import movie.MovieService;
import movie.TopMovie;
import movie.TopMovieService;
import review.Review;
import review.ReviewService;

/**
 * Manages listing movie recommendations for an Amazon Videos user.
 */
public class RecommendationService {

    public static final int MAX_RECOMMENDATIONS = 10;

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final TopMovieService topMovieService;

    /**
     * Manages reviews for Amazon Videos.
     */
    public RecommendationService(MovieService movieService,
                                 ReviewService reviewService,
                                 TopMovieService topMovieService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.topMovieService = topMovieService;
    }

    /**
     * Lists (up to 10) movie recommendations for an Amazon Videos user.
     *
     * @param userId the userId of the review
     * @return a list of recommendation movies
     * @throws UserNotFoundException if the user could not be found
     */
    public List<?> listRecommendations(String userId) throws UserNotFoundException {
        final Optional<Review> latestFavorite = reviewService.fetchLatestFavorite(userId);
        return latestFavorite.isPresent()
                ? generateRecommendations(latestFavorite.get())
                : listTopMovies(MAX_RECOMMENDATIONS);
    }

    private List<Movie> generateRecommendations(Review review) throws UserNotFoundException {
        final Movie fromReview = movieService.fetch(review.getImdbId()).get();
        final String mostFrequentGenre = fromReview.getMostFrequentGenre();
        final String mostFrequentKeyword = fromReview.getMostFrequentKeyword();
        final Set<String> alreadySeen = reviewedMovies(review.getUserId());
        final Comparator<Movie> releaseYearDescending =
                Comparator.comparingInt(Movie::getReleaseYear).reversed();

        return movieService.listMovies(mostFrequentGenre).stream()
                .filter(movie -> !alreadySeen.contains(movie.getImdbId()))
                .filter(movie -> movie.getMostFrequentKeyword().contains(mostFrequentKeyword))
                .limit(MAX_RECOMMENDATIONS)
                .sorted(releaseYearDescending)
                .collect(Collectors.toList());
    }

    private Set<String> reviewedMovies(String userId) throws UserNotFoundException {
        return reviewService.listReviewedMovies(userId);
    }

    private List<TopMovie> listTopMovies(int max) {
        return topMovieService.listTopMovies(max);
    }
}
