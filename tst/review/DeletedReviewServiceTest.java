package review;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeletedReviewServiceTest {

    private DeletedReviewDAO deletedReviewDAO;
    private DynamodbStreamRecord record;
    private DeletedReviewService service;

    @Before
    public void setUp() {
        deletedReviewDAO = mock(DeletedReviewDAO.class);
        record = mock(DynamodbStreamRecord.class);
        service = new DeletedReviewService(deletedReviewDAO);
    }

    @Test
    public void save() {
        final DeletedReview deletedReview = new DeletedReview();

        service.save(deletedReview);

        verify(deletedReviewDAO).save(deletedReview);
    }

    @Test
    public void valueOf() {
        final StreamRecord streamRecord = new StreamRecord().withOldImage(createValues());
        when(record.getDynamodb()).thenReturn(streamRecord);
        final DeletedReview expected = new DeletedReview(null, null, 0.0, null, null);

        final DeletedReview result = service.valueOf(record);

        assertEquals(expected, result);
    }

    private Map<String, AttributeValue> createValues() {
        return ImmutableMap.of(
                DeletedReviewService.USERID, new AttributeValue(),
                DeletedReviewService.IMDBID, new AttributeValue(),
                DeletedReviewService.RATING, new AttributeValue().withN("0.0"),
                DeletedReviewService.CREATEDAT, new AttributeValue(),
                DeletedReviewService.UPDATEDAT, new AttributeValue()
        );
    }
}
