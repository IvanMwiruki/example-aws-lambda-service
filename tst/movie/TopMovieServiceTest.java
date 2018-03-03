package movie;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopMovieServiceTest {

    private static final int INVALID_MAX = -1;
    private static final String IMDBID = "tt0000036";

    private MovieService movieService;
    private TopMovieDAO topMovieDAO;
    private TopMovieService service;

    @Before
    public void setUp() {
        movieService = mock(MovieService.class);
        topMovieDAO = mock(TopMovieDAO.class);

        service = new TopMovieService(movieService, topMovieDAO);
    }

    @Test
    public void updateTopMovies() {
        final List<TopMovie> oldTopMovies = Collections.singletonList(new TopMovie());
        when(topMovieDAO.listTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE)).thenReturn(oldTopMovies);
        final Movie movie = new Movie();
        movie.setImdbId(IMDBID);
        final List<TopMovie> newTopMovies = Collections.singletonList(TopMovieService.valueOf(movie));
        when(movieService.fetchTopRatedMoviesRandomGenres(TopMovieService.MAX_MOVIES_TO_UPDATE))
                .thenReturn(Collections.singletonList(movie));
        final Pair<List<TopMovie>, List<TopMovie>> expectedPair = Pair.of(oldTopMovies, newTopMovies);
        when(topMovieDAO.deleteOldAndSaveNew(oldTopMovies, newTopMovies)).thenReturn(expectedPair);

        final Pair<List<TopMovie>, List<TopMovie>> result = service.updateTopMovies(
                TopMovieService.MAX_MOVIES_TO_UPDATE);

        assertEquals(expectedPair, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTopMoviesIllegalMax() {
        service.updateTopMovies(INVALID_MAX);
    }

    @Test
    public void listTopMovies() {
        final List<TopMovie> topMovies = Collections.singletonList(new TopMovie());
        when(topMovieDAO.listTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE)).thenReturn(topMovies);

        final List<TopMovie> result = service.listTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE);

        assertEquals(topMovies, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void listTopMoviesIllegalMax() {
        service.listTopMovies(INVALID_MAX);
    }
}
