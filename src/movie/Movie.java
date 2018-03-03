package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Objects;

/**
 * A movie within Amazon Videos.
 */
@DynamoDBTable(tableName = "...")
public class Movie {

    public static final String MOST_FREQUENT_GENRE_TO_IMDB_RATING_INDEX = "mostFrequentGenre-imdbRating-index";

    @DynamoDBHashKey
    private String imdbId;

    private String title;

    private Double imdbRating;

    private String mostFrequentGenre;

    private String mostFrequentKeyword;

    private Boolean isAdult;

    private Integer releaseYear;

    public Movie() {}

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getMostFrequentGenre() {
        return mostFrequentGenre;
    }

    public void setMostFrequentGenre(String mostFrequentGenre) {
        this.mostFrequentGenre = mostFrequentGenre;
    }

    public String getMostFrequentKeyword() {
        return mostFrequentKeyword;
    }

    public void setMostFrequentKeyword(String mostFrequentKeyword) {
        this.mostFrequentKeyword = mostFrequentKeyword;
    }

    public Boolean getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(Boolean isAdult) {
        this.isAdult = isAdult;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Movie
                && Objects.equals(imdbId, (((Movie) other).imdbId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId);
    }
}
