package movie;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Manages a table of the top Amazon Videos movies.
 */
public class TopMovieService {

    public static final int MAX_MOVIES_TO_UPDATE = 10;

    private final MovieService movieService;
    private final TopMovieDAO topMovieDAO;

    public TopMovieService(MovieService movieService, TopMovieDAO topMovieDAO) {
        this.movieService = movieService;
        this.topMovieDAO = topMovieDAO;
    }

    /**
     * Fetches the current top movies and saves those to the Top Movie table,
     * while also deleting the now stale top movies from the Top Movie table.
     *
     * @param max the maximum number to update
     * @return a Pair, containing the old Top Movies as the`left`,
     *     and the new Top Movies as the `right`
     */
    public Pair<List<TopMovie>, List<TopMovie>> updateTopMovies(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Max cannot be less than 0.");
        }

        final List<TopMovie> oldTopMovies = topMovieDAO.listTopMovies(max);
        final List<TopMovie> newTopMovies = movieService.fetchTopRatedMoviesRandomGenres(max)
                .stream()
                .map(TopMovieService::valueOf)
                .collect(Collectors.toList());

        return topMovieDAO.deleteOldAndSaveNew(oldTopMovies, newTopMovies);
    }

    /**
     * Lists the top movies.
     *
     * @param max the maximum number to list. The naturally returned size is ~10.
     * @return a list of top movies
     */
    public List<TopMovie> listTopMovies(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Max cannot be less than 0.");
        }
        return topMovieDAO.listTopMovies(max);
    }

    /**
     * Returns a TopMovie representation of a {@code Movie} argument.
     *
     * @param movie a {@code Movie}
     * @return the TopMovie representation of the given Movie
     */
    public static TopMovie valueOf(Movie movie) {
        return new TopMovie(
                movie.getImdbId(),
                movie.getTitle(),
                movie.getImdbRating(),
                movie.getMostFrequentGenre(),
                movie.getMostFrequentKeyword(),
                movie.getIsAdult(),
                movie.getReleaseYear()
        );
    }
}
