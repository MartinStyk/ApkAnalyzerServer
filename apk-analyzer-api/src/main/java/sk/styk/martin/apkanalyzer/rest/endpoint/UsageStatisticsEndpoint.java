package sk.styk.martin.apkanalyzer.rest.endpoint;

import sk.styk.martin.apkanalyzer.service.UsageStatisticsService;
import sk.styk.martin.apkanalyzer.transfer.object.UsageStatistics;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mstyk
 * @date 11/13/17
 */
@Path("/usage")
public class UsageStatisticsEndpoint {

    @Inject
    private UsageStatisticsService usageStatisticsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        UsageStatistics data = usageStatisticsService.getAll();
        return Response.ok(data).build();
    }
}
