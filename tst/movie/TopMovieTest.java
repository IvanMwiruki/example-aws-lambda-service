package movie;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TopMovieTest {

    private static final String IMDBID = "tt0000036";
    private static final String IMDBID_OTHER = "tt0000100";

    @Test
    public void equal() {
        final TopMovie movie1 = new TopMovie();
        movie1.setImdbId(IMDBID);
        final TopMovie movie2 = new TopMovie();
        movie2.setImdbId(IMDBID);

        assertTrue(movie1.equals(movie2));
        assertEquals(movie1.hashCode(), movie2.hashCode());
    }

    @Test
    public void notEqual() {
        final TopMovie movie1 = new TopMovie();
        movie1.setImdbId(IMDBID);
        final TopMovie movie2 = new TopMovie();
        movie2.setImdbId(IMDBID_OTHER);

        assertFalse(movie1.equals(movie2));
        assertNotEquals(movie1.hashCode(), movie2.hashCode());
    }

    @Test
    public void equalSameObject() {
        final TopMovie movie1 = new TopMovie();
        final TopMovie movie2 = movie1;

        assertTrue(movie1.equals(movie2));
    }

    @Test
    public void notEqualDifferentInstanceOf() {
        final TopMovie movie1 = new TopMovie();
        final Movie movie2 = new Movie();

        assertFalse(movie1.equals(movie2));
    }
}
