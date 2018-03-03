package movie;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateTopMovieTableHandlerTest {

    private TopMovieService service;
    private UpdateTopMovieTableHandler handler;

    @Before
    public void setUp() {
        service = mock(TopMovieService.class);

        handler = new UpdateTopMovieTableHandler();
    }

    @Test
    public void updateTopMovies() {
        handler.handleRequest(service);

        verify(service).updateTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE);
    }

    @Test
    public void catchException() {
        when(service.updateTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE))
                .thenThrow(new RuntimeException());

        final String result = handler.handleRequest(service);

        assertTrue(result.contains("Failed"));
    }
}
