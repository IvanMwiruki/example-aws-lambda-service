package review;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import dynamodb.DynamoDBMapperWrapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeletedReviewDAOTest {

    private DynamoDBMapperWrapper mapper;
    private DeletedReview deletedReview;
    private DeletedReviewDAO dao;

    @Before
    public void setUp() {
        mapper = mock(DynamoDBMapperWrapper.class);
        deletedReview = new DeletedReview();

        dao = new DeletedReviewDAO(mapper);
    }

    @Test
    public void saveDeletedReview() {
        when(mapper.save(deletedReview)).thenReturn(deletedReview);

        final DeletedReview result = dao.save(deletedReview);

        assertEquals(deletedReview, result);
    }

    @Test(expected = AmazonDynamoDBException.class)
    public void saveDeletedReviewDynamoException() {
        when(mapper.save(deletedReview)).thenThrow(new AmazonDynamoDBException("test"));

        dao.save(deletedReview);
    }
}
