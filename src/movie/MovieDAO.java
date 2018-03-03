package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import dynamodb.DynamoDBMapperWrapper;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Retrieves movies from DynamoDB.
 */
public class MovieDAO {

    private static final int MAX_MOVIES_RETRIEVED = 500;

    private final DynamoDBMapperWrapper mapper;

    public MovieDAO(DynamoDBMapperWrapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Fetch a movie by its imdbId.
     *
     * @param imdbId the imdbId of the movie to retrieve
     * @return an Optional of the Movie with the specified imdbId
     */
    public Optional<Movie> fetch(String imdbId) {
        return mapper.load(Movie.class, imdbId);
    }

    /**
     * List movies with the given mostFrequentGenre. Movies are sorted by rating descending.
     * Up to 500 results are retrieved.
     *
     * @param mostFrequentGenre the most frequent genre, used to search for movies
     * @return a list of movies
     */
    public List<Movie> listMovies(String mostFrequentGenre) {
        final Map<String, AttributeValue> expressionAttributeValues = ImmutableMap.of(
                ":mostFrequentGenre", new AttributeValue(mostFrequentGenre),
                ":false", new AttributeValue().withBOOL(false)
        );

        final DynamoDBQueryExpression<Movie> queryExpression = new DynamoDBQueryExpression<Movie>()
                .withIndexName(Movie.MOST_FREQUENT_GENRE_TO_IMDB_RATING_INDEX)
                .withKeyConditionExpression("mostFrequentGenre = :mostFrequentGenre")
                .withFilterExpression("isAdult = :false")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withLimit(MAX_MOVIES_RETRIEVED);

        return mapper.queryPage(Movie.class, queryExpression).getResults();
    }

    /**
     * Fetch the top rated movies, all with random genres.
     *
     * @param max the maximum number to fetch, result may contain less
     *     than this amount
     * @return a list of movies
     */
    public List<Movie> fetchTopRatedMoviesRandomGenres(int max) {
        final List<String> genres = Genre.listGenres().stream()
                .map(Genre::toString)
                .collect(Collectors.toList());
        Collections.shuffle(genres);

        final List<Movie> fetched = new LinkedList<>();

        // For demonstration only. This is very expensive.
        for (int i = 0; i < max; i++) {
            final Map<String, AttributeValue> expressionAttributeValues = ImmutableMap.of(
                    ":mostFrequentGenre", new AttributeValue(genres.get(i))
            );

            final DynamoDBQueryExpression<Movie> queryExpression = new DynamoDBQueryExpression<Movie>()
                    .withIndexName(Movie.MOST_FREQUENT_GENRE_TO_IMDB_RATING_INDEX)
                    .withKeyConditionExpression("mostFrequentGenre = :mostFrequentGenre")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withLimit(1)
                    .withConsistentRead(false)
                    .withScanIndexForward(false);

            fetched.addAll(mapper.queryPage(Movie.class, queryExpression)
                    .getResults());
        }
        return fetched;
    }
}
