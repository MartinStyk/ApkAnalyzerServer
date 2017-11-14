package sk.styk.martin.apkanalyzer.rest.endpoint;

import sk.styk.martin.apkanalyzer.entity.AppData;
import sk.styk.martin.apkanalyzer.service.AppDataService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/apps")
public class AppDataEndpoint {

    @Inject
    private AppDataService appDataService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("device") String deviceId) {
        List<AppData> data;
        if (deviceId == null) {
            data = appDataService.findAll();
        } else {
            data = appDataService.findByDevice(deviceId);
        }
        return Response.ok(data).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        AppData data = appDataService.findById(id);
        return Response.ok(data).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(AppData data) {
        Response.ResponseBuilder builder;
        try {
            AppData created = appDataService.createWithExistenceCheck(data);
            if (created != null) {
                builder = Response.ok(created);
            } else {
                builder = Response.status(Response.Status.CONFLICT);
            }

        } catch (Exception e) {
            builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }
        return builder.build();
    }

}