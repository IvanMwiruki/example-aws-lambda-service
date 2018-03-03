package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import dynamodb.DynamoDBMapperWrapper;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Stores, retrieves, and deletes top movies from DynamoDB.
 */
public class TopMovieDAO {

    private static final int MAX_MOVIES_RETRIEVED = 10;

    private final DynamoDBMapperWrapper mapper;

    public TopMovieDAO(DynamoDBMapperWrapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Deletes a list of old top movies and saves a list of new top movies.
     *
     * @param oldTopMovies a list of old top movies
     * @param newTopMovies a list of new top movies
     * @return a Pair, containing the oldTopMovies passed in as the `left`,
     *     and the newTopMovies passed in as the `right`
     */
    public Pair<List<TopMovie>, List<TopMovie>> deleteOldAndSaveNew(
            List<TopMovie> oldTopMovies, List<TopMovie> newTopMovies) {
        mapper.batchWrite(newTopMovies, oldTopMovies);
        return Pair.of(oldTopMovies, newTopMovies);
    }

    /**
     * Lists the top movies.
     *
     * @param max the maximum number to list, up to 10.
     * @return a list of top movies
     */
    public List<TopMovie> listTopMovies(int max) {
        if (max > MAX_MOVIES_RETRIEVED) {
            throw new IllegalArgumentException("Max cannot be greater than 10.");
        }
        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(max);

        return mapper.scanPage(TopMovie.class, scanExpression).getResults();
    }
}
