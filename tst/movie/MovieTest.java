package movie;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MovieTest {

    private static final String IMDBID = "tt0000036";

    @Test
    public void equal() {
        final Movie movie1 = new Movie();
        movie1.setImdbId(IMDBID);
        final Movie movie2 = new Movie();
        movie2.setImdbId(IMDBID);

        assertTrue(movie1.equals(movie2));
        assertEquals(movie1.hashCode(), movie2.hashCode());
    }

    @Test
    public void notEqual() {
        final Movie movie1 = new Movie();
        movie1.setImdbId(IMDBID);
        final Movie movie2 = new Movie();

        assertFalse(movie1.equals(movie2));
        assertNotEquals(movie1.hashCode(), movie2.hashCode());
    }
}
