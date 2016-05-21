package com.intuit.client;

import com.intuit.provider.JacksonJsonProvider;
import com.intuit.types.Data;
import com.intuit.types.Location;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Response;
import java.util.concurrent.*;

/**
 *
 */
public class ClientDaoImpl implements ClientDao {

    public static final String[] routes = {
            "61a37be3-8a9a-4586-b563-0fdca37c285b",
            "d6bafec3-6056-4232-b7bc-1a6862654d33",
            "07592444-38ea-4d81-adeb-f2d6ff21837d",
            "66e1d55d-56b8-4a87-b5a2-fce15736c7d3",
            "6e402dbf-d37b-4ff8-85b4-fbbd2f29bfd7",
            "8cdd7cff-ccf3-469a-bb8b-481fd50d7609",
            "30e64caf-b861-486b-a3f5-204cb814d405",
            "5cd2945a-4901-485e-8c63-9e94884ff6bb",
            "5b120de2-5c70-4a2a-8320-0dea7179de58",
            "143d43ab-1470-45b7-a5f7-cfbf6f4d7c99",
            "edcd1cfb-b6be-4e2b-a8f2-337645e38652",
            "9aa8600e-d36b-4514-9991-ad303a94a5fd",
            "25094b77-05f7-47a8-a8f9-b0e7d70dc95a",
            "b6e61b85-db22-4801-8e6e-bb79bd90625b",
            "f30d07bc-5342-497f-91c6-e1bc8a35acec",
    };

    public static final String[] routes_no = { "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501",
            "95",  "60A",  "216A",  "23D",  "34F",  "99",  "101",  "201B",  "201R", "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",
            "610",  "56",  "34",  "501","501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501", };
    public static final double[][] values = {
            {12.902193, 77.601108},
            {12.899434, 77.600734},
            {12.897468, 77.600090},
            {12.895836, 77.599575},
            {12.894037, 77.599360},
            {12.892490, 77.598845},
            {12.890774, 77.598287},
            {12.889478, 77.597601},
            {12.887930, 77.597172},
            {12.886089, 77.596700},
            {12.884439, 77.596481},
            {12.884303, 77.597007},
            {12.884167, 77.597468},
            {12.884177, 77.598004},
            {12.884104, 77.598605},
            {12.884064, 77.599509},
            {12.884606, 77.602179},
            {12.884522, 77.603553},
            {12.883853, 77.605184},
            {12.883560, 77.606771},
            {12.883350, 77.608617},
            {12.882932, 77.610033},
            {12.879749, 77.609995},
            {12.877828, 77.610284},
            {12.876416, 77.611733},
            {12.876221, 77.614120},
            {12.876242, 77.615278},
            {12.876190, 77.615826},
            {12.876195, 77.616105},
            {12.876237, 77.617650},
            {12.877576, 77.61906},
            {12.877994, 77.621083},
            {12.877325, 77.622671},
            {12.877241, 77.624645}};

    public static String[] crowd = {"STAND", "SEAT", "CROWDED"};

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClientDaoImpl.class);

    Client client;
    static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public ClientDaoImpl() {
        Configuration configuration = new ClientConfig(JacksonJsonProvider.class, LoggingFilter.class);
        client = ClientBuilder.newClient(configuration);

    }

    @Override
    public Data getSomeData() throws Exception {
        final Future<Data> entityFuture = client.target("http://localhost:8389/test/v1/test/sample")
                .request().async().get(new InvocationCallback<Data>() {
                    @Override
                    public void completed(Data data) {
                        LOGGER.info("Response entity '" + data + "' received.");
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        LOGGER.error("Failed : ", throwable);
                    }
                });
        try {
            return entityFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("Failed to execute");
        }
    }

    public void updatePosition(Location location) throws Exception {
        Future<Response> post = client.target("http://localhost:8389/test/v1/location")
                .request().async().post(Entity.json(location));
        System.out.println(post.get().getStatus());
    }

    public static void main(String[] args) {

        ClientDaoImpl clientDao = new ClientDaoImpl();
        int  corePoolSize  =    20;
        int  maxPoolSize   =   30;
        long keepAliveTime = 50000;

        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(5000)
                );

        int i = 0;
        while (true) {
            int route_val = threadLocalRandom.nextInt(0, 15);
            int currValue = i %15;
            System.out.println("Current value:" + currValue);
            double latitude = values[currValue][0];
            double longitude = values[currValue][1];
            System.out.println("Latitude:" + latitude + " longitude:" + longitude);
            BusRoute busRoute = new BusRoute(clientDao, 0, new Location(routes[route_val], latitude, longitude, routes_no[route_val], crowd[threadLocalRandom.nextInt(0,3)]));
            threadPoolExecutor.execute(busRoute);
            i++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
