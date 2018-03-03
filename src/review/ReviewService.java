package review;

import exceptions.InvalidRatingException;
import exceptions.MovieNotFoundException;
import exceptions.ReviewNotFoundException;
import exceptions.UserNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import movie.MovieService;
import user.UserService;

/**
 * Manages reviews for Amazon Videos.
 */
public class ReviewService {

    public static final double MIN_RATING = 4.0;

    private final UserService userService;
    private final MovieService movieService;
    private final ReviewDAO reviewDAO;

    /**
     * Manages reviews for Amazon Videos.
     */
    public ReviewService(UserService userService, MovieService movieService, ReviewDAO reviewDAO) {
        this.userService = userService;
        this.movieService = movieService;
        this.reviewDAO = reviewDAO;
    }

    /**
     * Put a new review. Namely, creates a new review if one does not exist,
     * or updates one if it already does.
     *
     * @param review the review to create or update
     * @return the review in its current state
     * @throws InvalidRatingException if the given rating, i, is not (0 < i <= 5) && ((i % .5) == 0)
     * @throws UserNotFoundException if the given userId does not map to a user
     * @throws MovieNotFoundException if the given imdbId does not map to a movie
     * @throws NullPointerException if the given rating is null
     */
    public Review put(Review review)
            throws InvalidRatingException, UserNotFoundException, MovieNotFoundException {
        Objects.requireNonNull(review.getRating(),
                "Review cannot contain empty or null rating. {rating: " + review.getRating() + "}");

        final Optional<Review> toSave = reviewDAO.fetch(review);
        return toSave.isPresent()
                ? update(toSave.get(), validRating(review))
                : create(valid(review));
    }

    private Review validRating(Review review)
            throws InvalidRatingException {
        if (!isValid(review.getRating())) {
            throw new InvalidRatingException(
                    String.format("The provided rating: {%s} is not valid.", review.getRating()));
        }
        return review;
    }

    private Review valid(Review review)
            throws InvalidRatingException, UserNotFoundException, MovieNotFoundException {
        if (!isValid(review.getRating())) {
            throw new InvalidRatingException(
                    String.format("The provided rating: {%s} is not valid.", review.getRating()));
        }
        userService.getUser(review.getUserId());
        if (!movieService.movieExists(review.getImdbId())) {
            throw new MovieNotFoundException(
                    String.format("The specified movie: {%s} could not be found.", review.getImdbId()));
        }
        return review;
    }

    /**
     * Verifies that a given rating is valid.
     * Rating must be greater than 0 and less than or equal to 5.
     * It must also be divisible by .5.
     *
     * @param toCheck the value to validate
     * @return true if this is a valid rating
     */
    private boolean isValid(double toCheck) {
        final double epsilon = 0.0000001;
        final double maximumRating = 5.0;
        final double divisibility = .5;

        return toCheck > 0.0
                && (toCheck < maximumRating || (Math.abs(maximumRating - toCheck) < epsilon))
                && toCheck % divisibility < epsilon;
    }

    private Review update(Review older, Review newer) {
        older.setRating(newer.getRating());
        return reviewDAO.save(older);
    }

    private Review create(Review review) {
        return reviewDAO.save(review);
    }

    /**
     * Delete a review.
     *
     * @param userId the userId of the review
     * @param imdbId the imdbId of the review
     * @return the deleted review
     * @throws ReviewNotFoundException if the given userId and imdbId don't map to an existing review
     */
    public Review delete(String userId, String imdbId) throws ReviewNotFoundException {
        final Review toDelete = reviewDAO.fetch(userId, imdbId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Review with given userId {" + userId + "} and imdbId {" + imdbId + "} could not be found"));
        return reviewDAO.delete(toDelete);
    }

    /**
     * List the reviews of a user.
     *
     * @param request a ListReviewsRequest
     * @return a ListResponse encapsulating the list of reviews returned
     * @throws UserNotFoundException if the given userId does not map to a user
     */
    public ListReviewsResponse listReviews(ListReviewsRequest request)
            throws UserNotFoundException {
        userService.getUser(request.getUserId());
        return reviewDAO.createListReviewsResponse(
                request.getUserId(), request.getCount(), request.getLastEvaluatedKey());
    }

    /**
     * Fetch the latest review, with rating >= 4, of a user.
     *
     * @param userId the userId of the review
     * @return an Optional of the "latest favorite" review
     * @throws UserNotFoundException if the user could not be found
     */
    public Optional<Review> fetchLatestFavorite(String userId) throws UserNotFoundException {
        userService.getUser(userId);
        return reviewDAO.fetchLatestFavorite(userId, MIN_RATING);
    }

    /**
     * Returns a set of Strings containing the imdbIds of all the movies
     * the given user has reviewed.
     *
     * @param userId the userId of the reviews
     * @return a Set of Strings of imdbIds
     * @throws UserNotFoundException if the user could not be found
     */
    public Set<String> listReviewedMovies(String userId) throws UserNotFoundException {
        userService.getUser(userId);
        return reviewDAO.listReviewedMovies(userId);
    }
}
