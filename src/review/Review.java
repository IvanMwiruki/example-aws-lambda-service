package review;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Calendar;
import java.util.Objects;

/**
 * A review within Amazon Videos.
 */
@DynamoDBTable(tableName = "...")
public class Review {

    public static final String USER_ID_TO_CREATED_AT_INDEX = "userId-createdAt-index";

    @DynamoDBHashKey
    private String userId;

    @DynamoDBRangeKey
    private String imdbId;

    private Double rating;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    private Calendar createdAt;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
    private Calendar updatedAt;

    @JsonIgnore
    @DynamoDBVersionAttribute
    private Long version;

    public Review() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Review
                && Objects.equals(userId, ((Review) other).userId)
                && Objects.equals(imdbId, ((Review) other).imdbId)
                && Objects.equals(rating, ((Review) other).rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, imdbId, rating);
    }
}
