package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import dynamodb.DynamoDBMapperWrapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieDAOTest {

    private static final String IMDBID = "tt0000036";
    private static final String GENRE = "Drama";
    private static final int MAX = 10;

    private DynamoDBMapperWrapper mapper;
    private MovieDAO dao;

    @Before
    public void setUp() {
        mapper = mock(DynamoDBMapperWrapper.class);

        dao = new MovieDAO(mapper);
    }

    @Test
    public void fetchMovieThatExists() {
        when(mapper.load(Movie.class, IMDBID)).thenReturn(Optional.of(new Movie()));

        final Optional<Movie> result = dao.fetch(IMDBID);

        assertTrue(result.isPresent());
    }

    @Test
    public void fetchMovieDoesntExist() {
        when(mapper.load(Movie.class, IMDBID)).thenReturn(Optional.empty());

        final Optional<Movie> result = dao.fetch(IMDBID);

        assertFalse(result.isPresent());
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchMovieDynamoException() {
        when(mapper.load(Movie.class, IMDBID)).thenThrow(new AmazonDynamoDBException("test"));

        dao.fetch(IMDBID);
    }

    @Test
    public void listMovies() {
        final QueryResultPage<Movie> movies = mock(QueryResultPage.class);
        when(mapper.queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class))).thenReturn(movies);
        when(movies.getResults()).thenReturn(Collections.singletonList(new Movie()));

        dao.listMovies(GENRE);

        verify(mapper).queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class));
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void listMoviesDynamoException() {
        when(mapper.queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.listMovies(GENRE);
    }

    @Test
    public void fetchTopRatedMovies() {
        final QueryResultPage<Movie> movies = mock(QueryResultPage.class);
        when(mapper.queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class))).thenReturn(movies);
        when(movies.getResults()).thenReturn(Collections.singletonList(new Movie()));

        dao.fetchTopRatedMoviesRandomGenres(MAX);

        verify(mapper, times(MAX)).queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class));
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void fetchTopRatedMoviesDynamoException() {
        when(mapper.queryPage(eq(Movie.class), any(DynamoDBQueryExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.fetchTopRatedMoviesRandomGenres(MAX);
    }
}
