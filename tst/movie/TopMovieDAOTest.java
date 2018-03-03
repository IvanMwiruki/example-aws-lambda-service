package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import dynamodb.DynamoDBMapperWrapper;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TopMovieDAOTest {

    private static final String IMDBID = "tt0000036";

    private DynamoDBMapperWrapper mapper;
    private TopMovie movie;
    private TopMovieDAO dao;

    @Before
    public void setUp() {
        mapper = mock(DynamoDBMapperWrapper.class);
        movie = new TopMovie();
        movie.setImdbId(IMDBID);

        dao = new TopMovieDAO(mapper);
    }

    @Test
    public void deleteOldAndSaveNew() {
        final List<TopMovie> oldTopMovies = Collections.singletonList(movie);
        final List<TopMovie> newTopMovies = Collections.singletonList(movie);

        final Pair<List<TopMovie>, List<TopMovie>> result = dao.deleteOldAndSaveNew(oldTopMovies, newTopMovies);

        verify(mapper).batchWrite(newTopMovies, oldTopMovies);
        assertEquals(Pair.of(oldTopMovies, newTopMovies), result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void deleteOldAndSaveNewDynamoException() {
        when(mapper.batchWrite(null, null)).thenThrow(new AmazonDynamoDBException("test"));

        dao.deleteOldAndSaveNew(null, null);
    }

    @Test
    public void listTopMovies() {
        final ScanResultPage<TopMovie> scanPage = new ScanResultPage<>();
        scanPage.setResults(Collections.singletonList(movie));
        when(mapper.scanPage(eq(TopMovie.class), any(DynamoDBScanExpression.class))).thenReturn(scanPage);

        final List<TopMovie> result = dao.listTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE);

        assertEquals(scanPage.getResults(), result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void listTopMoviesDynamoException() {
        when(mapper.scanPage(eq(TopMovie.class), any(DynamoDBScanExpression.class)))
                .thenThrow(new AmazonDynamoDBException("test"));

        dao.listTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE);
    }
}
