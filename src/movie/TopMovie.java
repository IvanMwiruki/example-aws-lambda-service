package movie;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Objects;

/**
 * A top movie within Amazon Videos.
 */
@DynamoDBTable(tableName = "...")
public class TopMovie {

    @DynamoDBHashKey
    private String imdbId;

    private String title;

    @DynamoDBRangeKey
    private Double imdbRating;

    private String mostFrequentGenre;

    private String mostFrequentKeyword;

    private Boolean isAdult;

    private Integer releaseYear;

    public TopMovie() {}

    /**
     * Constructs a TopMovie.
     */
    public TopMovie(String imdbId,
                    String title,
                    Double imdbRating,
                    String mostFrequentGenre,
                    String mostFrequentKeyword,
                    Boolean isAdult,
                    Integer releaseYear) {
        this.imdbId = imdbId;
        this.title = title;
        this.imdbRating = imdbRating;
        this.mostFrequentGenre = mostFrequentGenre;
        this.mostFrequentKeyword = mostFrequentKeyword;
        this.isAdult = isAdult;
        this.releaseYear = releaseYear;
    }

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
        return other instanceof TopMovie
                && Objects.equals(imdbId, (((TopMovie) other).imdbId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId);
    }
}
