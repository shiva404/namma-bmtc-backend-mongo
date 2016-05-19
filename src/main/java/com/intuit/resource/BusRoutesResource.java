package com.intuit.resource;

import com.intuit.mongo.MongoDao;
import com.intuit.types.BusRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/***
 *
 */
@Consumes("application/json")
@Produces("application/json")
@Path("/location")
@Singleton
@Component
public class BusRoutesResource {

    @Autowired
    private MongoDao mongoDao;

    @GET
    @Path("/{routeNumber}")
    private Response getBusRoute(@PathParam("routeNumber") String routeNumber){
        return Response.ok().entity(new BusRoute(routeNumber, "Something", "TOSomething", "Blah blah")).build();
    }
}
