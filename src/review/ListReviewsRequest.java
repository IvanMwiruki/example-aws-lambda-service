package review;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a request to list an Amazon Videos user's reviews.
 *
 * <p>
 * Invariants:
 * userId is a String and must not be null.
 * count is an int and can be passed in as a String that is null or (1 <= count <= 25). A null count defaults to 25.
 * lastEvaluatedKey is a Map of String to AttributeValue and is created from the pagination token
 * that is passed in, which must be a non empty String or null.
 * </p>
 */
public class ListReviewsRequest {

    public static final int MAXIMUM_COUNT = 500;

    private final String userId;
    private final Integer count;
    private final Map<String, AttributeValue> lastEvaluatedKey;

    private ListReviewsRequest(@NotNull String userId,
                               int count,
                               @Nullable Map<String, AttributeValue> lastEvaluatedKey) {
        this.userId = userId;
        this.count = count;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    /**
     * Returns a ListReviewsRequest representing the given values.
     *
     * @param userId          userId of the reviews to list
     * @param count           used to limit the number of results that will be returned
     * @param paginationToken used to construct the key to get the next page or results
     * @return a ListReviewsRequest
     * @throws IllegalArgumentException if the given count is not null and cannot be parsed into an integer,
     *                                  or the parsed integer is less than 1 or greater than 100, as well
     *                                  as if the pagination token is invalid
     */
    public static ListReviewsRequest of(@NotNull String userId,
                                        @Nullable String count,
                                        @Nullable String paginationToken)
            throws IllegalArgumentException {
        final int countInt = count == null ? MAXIMUM_COUNT : Integer.parseInt(count);
        if (countInt < 1 || countInt > MAXIMUM_COUNT) {
            throw new IllegalArgumentException(
                    "The count given {" + countInt + "} is less than 1 or greater than 100.");
        }

        final Map<String, AttributeValue> lastEvaluatedKey = parseToken(userId, paginationToken);

        return new ListReviewsRequest(userId, countInt, lastEvaluatedKey);
    }

    /* Creates a "last evaluated key" that DynamoDB can use when getting pages. */
    private static Map<String, AttributeValue> parseToken(@NotNull String userId, @Nullable String paginationToken) {
        if (StringUtils.isBlank(paginationToken)) {
            return null;
        }

        final String[] attributes = paginationToken.split("#");
        if (attributes.length != 2) {
            throw new IllegalArgumentException("Invalid pagination token {" + paginationToken + "}.");
        }

        final int indexOfImdbId = 0;
        final int indexOfCreatedAt = 1;

        return ImmutableMap.of("userId", new AttributeValue(userId),
                "imdbId", new AttributeValue(attributes[indexOfImdbId]),
                "createdAt", new AttributeValue(attributes[indexOfCreatedAt]));
    }

    public String getUserId() {
        return userId;
    }

    public Integer getCount() {
        return count;
    }

    public Map<String, AttributeValue> getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }
}
