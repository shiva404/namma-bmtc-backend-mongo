package com.intuit.resource;

import com.intuit.client.ClientDao;
import com.intuit.client.ClientDaoImpl;
import com.intuit.types.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Path("test")
@Consumes("application/json")
@Produces("application/json")
public class TestResource {

    public TestResource() {
        this.clientDao = new ClientDaoImpl();
    }

    private ClientDao clientDao;

    Logger logger = LoggerFactory.getLogger(TestResource.class);

    @GET
    public void asyncGetWithTimeout(@Suspended final AsyncResponse asyncResponse) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation time out.").build()));
        asyncResponse.register((CompletionCallback) throwable -> {
            if (throwable == null) {
                // Trigger something right after the req completion like putting to the messaging
            } else {
                // some error occured
                logger.error("Error occurred :", throwable);
            }
        });
        asyncResponse.setTimeout(5, TimeUnit.SECONDS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Data result = callSomeClient();
                asyncResponse.resume(Response.ok().entity(result).build());
            }

            private Data callSomeClient() {
                try {
                    return clientDao.getSomeData();
                } catch (Exception e) {
                    throw new RuntimeException("Error", e);
                }
            }
        }).start();
    }

    @GET
    @Path("/sample")
    public void getSample(@Suspended final AsyncResponse asyncResponse) {
        new Thread(() -> {
            asyncResponse.resume(Response.ok().entity(new Data("id", "name")).build());
        }).start();
    }
}
