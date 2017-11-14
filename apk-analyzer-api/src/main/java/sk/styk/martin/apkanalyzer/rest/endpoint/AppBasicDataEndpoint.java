package sk.styk.martin.apkanalyzer.rest.endpoint;

import sk.styk.martin.apkanalyzer.entity.AppBasicData;
import sk.styk.martin.apkanalyzer.service.AppBasicDataService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/appdata/basic")
public class AppBasicDataEndpoint {

    @Inject
    private AppBasicDataService appDataService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("device") String deviceId) {
        List<AppBasicData> data;
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
        AppBasicData data = appDataService.findById(id);
        return Response.ok(data).build();
    }

}