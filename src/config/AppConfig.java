package config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import dynamodb.DynamoDBMapperWrapper;
import movie.MovieDAO;
import movie.MovieService;
import movie.TopMovieDAO;
import movie.TopMovieService;
import recommendation.RecommendationService;
import review.DeletedReviewDAO;
import review.DeletedReviewService;
import review.ReviewDAO;
import review.ReviewService;
import user.UserDAO;
import user.UserService;

/**
 * Wiring up and managing all dependencies.
 */
public class AppConfig {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final AmazonDynamoDB DYNAMO_CLIENT = AmazonDynamoDBClientBuilder.standard().build();
    private static final DynamoDBMapper DYNAMO_DB_MAPPER = new DynamoDBMapper(DYNAMO_CLIENT);
    private static final DynamoDBMapperWrapper DYNAMO_DB_MAPPER_WRAPPER = new DynamoDBMapperWrapper(DYNAMO_DB_MAPPER);

    private final UserDAO userDAO = new UserDAO(DYNAMO_DB_MAPPER_WRAPPER);
    private final MovieDAO movieDAO = new MovieDAO(DYNAMO_DB_MAPPER_WRAPPER);
    private final ReviewDAO reviewDAO = new ReviewDAO(DYNAMO_DB_MAPPER_WRAPPER);
    private final DeletedReviewDAO deletedReviewDAO = new DeletedReviewDAO(DYNAMO_DB_MAPPER_WRAPPER);
    private final TopMovieDAO topMovieDAO = new TopMovieDAO(DYNAMO_DB_MAPPER_WRAPPER);

    private final UserService userService = new UserService(userDAO);
    private final MovieService movieService = new MovieService(movieDAO);
    private final ReviewService reviewService = new ReviewService(userService, movieService, reviewDAO);
    private final DeletedReviewService deletedReviewService = new DeletedReviewService(deletedReviewDAO);
    private final TopMovieService topMovieService = new TopMovieService(movieService, topMovieDAO);
    private final RecommendationService recommendationService =
            new RecommendationService(movieService, reviewService, topMovieService);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public ReviewService getReviewService() {
        return reviewService;
    }

    public DeletedReviewService getDeletedReviewService() {
        return deletedReviewService;
    }

    public RecommendationService getRecommendationService() {
        return recommendationService;
    }

    public TopMovieService getTopMovieService() {
        return topMovieService;
    }
}
