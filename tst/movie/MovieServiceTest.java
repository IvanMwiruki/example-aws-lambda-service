package movie;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MovieServiceTest {

    private static final String IMDBID = "tt0000076";
    private static final String MOST_FREQUENT_GENRE = "Drama";
    private static final int MAX = 10;

    private MovieDAO movieDAO;
    private MovieService service;

    @Before
    public void setUp() {
        movieDAO = mock(MovieDAO.class);

        service = new MovieService(movieDAO);
    }

    @Test
    public void fetchMovie() {
        service.fetch(IMDBID);

        verify(movieDAO).fetch(IMDBID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchMovieEmptyImdbId() {
        service.fetch("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchMovieNullImdbId() {
        service.fetch(null);
    }

    @Test
    public void listMovies() {
        service.listMovies(MOST_FREQUENT_GENRE);

        verify(movieDAO).listMovies(MOST_FREQUENT_GENRE);
    }

    @Test(expected = NullPointerException.class)
    public void listMoviesNullGenre() {
        service.listMovies(null);
    }

    @Test
    public void fetchTopRatedMoviesRandomGenres() {
        service.fetchTopRatedMoviesRandomGenres(MAX);

        verify(movieDAO).fetchTopRatedMoviesRandomGenres(MAX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchTopRatedMoviesRandomGenresLessThan0Max() {
        service.fetchTopRatedMoviesRandomGenres(-1);
    }
}
