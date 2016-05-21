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

    public static final String[] USERS = {
            "351a50d7-34db-40ec-8cba-6d851b0d23cb",
            "2f4f02ce-deb1-49da-8819-e0a394cc477f",
            "d1d162de-70c4-4e09-8509-0e1fc7d3799f",
            "c453133b-6832-4836-96f5-d5148fada920",
            "63d87e17-f0d4-4fb6-95f3-129f107d553d",
            "667b27d2-c1cc-4344-ab77-a56b0b236c23",
            "1abb91ee-24ca-4544-9cc8-d00d156db696",
            "defaecd0-8bc9-48e5-a3c3-dc263dbf1e38",
            "d98d8895-f498-4881-84b3-75665f33165e",
            "fe69d7dc-4ca4-4158-87c0-7e698bf7320d",
            "4f7b5476-b8d5-401b-8412-f8bae56b4b58",
            "b29b8658-e978-4727-ad9a-ae67f1e013e5",
            "d6a958d0-a3d1-418c-9ea0-e884c52220cb",
            "40aa3392-2ce5-4036-ba13-02d5a7d09d17",
            "4ceebb4f-f366-4939-af84-7bf7c90912ad"
    };

    public static final String[] routes_no = { "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501",
            "95",  "60A",  "216A",  "23D",  "34F",  "99",  "101",  "201B",  "201R", "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",
            "610",  "56",  "34",  "501","501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501"};
    public static final double[][] CO_ORDINATES = {
            {13.003460, 77.635807},
            {13.002331, 77.633919},
            {13.001954, 77.631602},
            {13.001411, 77.628984},
            {13.000867, 77.626666},
            {12.998902, 77.624435},
            {12.997982, 77.622203},
            {12.997354, 77.620197},
            {12.997019, 77.619553},
            {12.996695, 77.618941},
            {12.996455, 77.618416},
            {12.996183, 77.617740},
            {12.995870, 77.617021},
            {12.995713, 77.616667},
            {12.995263, 77.616324},
            {12.994845, 77.616077},
            {12.994490, 77.615852},
            {12.994009, 77.615583},
            {12.993737, 77.615433},
            {12.993235, 77.615219},
            {12.992765, 77.614972},
            {12.992252, 77.614746},
            {12.991803, 77.614543},
            {12.991343, 77.614307},
            {12.990925, 77.614071},
            {12.990485, 77.613813},
            {12.990036, 77.613609},
            {12.989534, 77.613395},
            {12.989105, 77.613212}};

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
        Future<Response> post = client.target("http://54.179.147.112:8080/test/v1/location")
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
            int route_val = threadLocalRandom.nextInt(0, USERS.length);
            int curIteration = i % CO_ORDINATES.length;
            System.out.println("Current value:" + curIteration);
            double latitude = CO_ORDINATES[curIteration][0];
            double longitude = CO_ORDINATES[curIteration][1];
            System.out.println("Latitude:" + latitude + " longitude:" + longitude);
            BusRoute busRoute = new BusRoute(clientDao, 0, new Location(USERS[route_val], latitude, longitude, routes_no[route_val], crowd[threadLocalRandom.nextInt(0,3)]));
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
