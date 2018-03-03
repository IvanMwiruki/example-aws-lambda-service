package movie;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.google.common.annotations.VisibleForTesting;
import config.AppConfig;
import org.apache.log4j.Logger;

/**
 * Handles scheduled events triggered by AWS Lambda by updating the Top Movie table.
 */
public class UpdateTopMovieTableHandler implements RequestHandler<ScheduledEvent, String> {

    private final Logger log = Logger.getLogger(UpdateTopMovieTableHandler.class);
    private final AppConfig appConfig = new AppConfig();
    private final TopMovieService service = appConfig.getTopMovieService();

    /**
     * Handles a Lambda Function request.
     *
     * @param input The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public String handleRequest(ScheduledEvent input, Context context) {
        return handleRequest(service);
    }

    @VisibleForTesting
    String handleRequest(TopMovieService topMovieService) {
        try {
            return topMovieService.updateTopMovies(TopMovieService.MAX_MOVIES_TO_UPDATE)
                    .toString("Successful! Old top movies deleted: [ %s ], "
                            + "New top movies saved [ %s ]");
        }
        catch (Exception e) {
            log.error("Failed to update the Top Movie table.", e);
            return "Failed to update the Top Movie table.";
        }
    }
}
