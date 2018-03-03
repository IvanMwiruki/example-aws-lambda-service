package review;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;

import static com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeletedReviewHandlerTest {

    private DynamodbEvent event;
    private DeletedReviewService service;
    private DeletedReviewHandler handler;

    @Before
    public void setUp() {
        event = mock(DynamodbEvent.class);
        service = mock(DeletedReviewService.class);

        handler = new DeletedReviewHandler();
    }

    @Test
    public void processDeletedReviews() {
        final int numberOfRecords = 25;
        final List<DynamodbStreamRecord> list =
                Stream.generate(() -> (DynamodbStreamRecord) new DynamodbStreamRecord()
                        .withEventName(DeletedReviewHandler.DELETED_EVENT_NAME))
                        .limit(numberOfRecords)
                        .collect(Collectors.toList());
        when(event.getRecords()).thenReturn(list);

        final String result = handler.handleRequest(event, service);

        assertTrue(result.contains(String.valueOf(numberOfRecords)));
    }

    @Test
    public void catchException() {
        when(event.getRecords()).thenThrow(new RuntimeException());

        handler.handleRequest(event, service);
    }
}
