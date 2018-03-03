package review;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ReviewTest {

    private static final String USERID = "d2fadc3b-b791-4054-b51e-49be4beb24c7";
    private static final String IMDBID = "tt0000036";
    private static final double RATING = 4.5;

    @Test
    public void equal() {
        final Review review1 = new Review();
        review1.setUserId(USERID);
        review1.setImdbId(IMDBID);
        review1.setRating(RATING);
        final Review review2 = new Review();
        review2.setUserId(USERID);
        review2.setImdbId(IMDBID);
        review2.setRating(RATING);

        assertTrue(review1.equals(review2));
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    public void notEqual() {
        final Review review1 = new Review();
        review1.setUserId(USERID);
        review1.setImdbId(IMDBID);
        review1.setRating(RATING);
        final Review review2 = new Review();

        assertFalse(review1.equals(review2));
        assertNotEquals(review1.hashCode(), review2.hashCode());
    }

}
