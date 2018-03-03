package movie;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * Manages movies for Amazon Videos.
 */
public class MovieService {

    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    /**
     * Fetch a movie by its imdbId.
     *
     * @param imdbId the imdbId of the movie to retrieve
     * @return an Optional of the Movie with the specified imdbId
     */
    public Optional<Movie> fetch(String imdbId) {
        if (StringUtils.isBlank(imdbId)) {
            final String message = String.format("Cannot look up movie by invalid imdbId. {imdbId: %s}", imdbId);
            throw new IllegalArgumentException(message);
        }
        return movieDAO.fetch(imdbId);
    }

    /**
     * Verifies that a movie exists.
     *
     * @param imdbId the imdbId of the movie to verify
     * @return true if the movie exists, false otherwise
     */
    public boolean movieExists(String imdbId) {
        return fetch(imdbId).isPresent();
    }

    /**
     * List movies with the given mostFrequentGenre. Movies are sorted by rating descending.
     *
     * @param mostFrequentGenre the most frequent genre
     * @return a list of movies
     * @throws NullPointerException if the String mostFrequentGenre is null
     */
    public List<Movie> listMovies(String mostFrequentGenre) {
        Objects.requireNonNull(mostFrequentGenre, "mostFrequentGenre cannot be null.");
        return movieDAO.listMovies(mostFrequentGenre);
    }

    /**
     * Fetch the top rated movies, all with random genres.
     *
     * @param max the maximum number to fetch, result may contain less
     *     than this amount
     * @return a list of movies
     */
    public List<Movie> fetchTopRatedMoviesRandomGenres(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Max cannot be less than 0.");
        }
        return movieDAO.fetchTopRatedMoviesRandomGenres(max);
    }
}
