package com.intuit.client;

import com.intuit.provider.JacksonJsonProvider;
import com.intuit.types.Data;
import com.intuit.types.Location;
import org.glassfish.jersey.client.ClientConfig;
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
            "dd4c4db7-1149-4599-b143-3ad21fc039b2",

            "7b32e17c-004a-4744-a236-1b5f41761f07",

            "5943384d-7e50-47b2-a6eb-1e4dd09404a9",

            "732fd697-bce9-4013-aef7-958eed2dabce",

            "80d8d4d7-84b0-4018-ab76-0b380d77e051",

            "f620e4bf-14ba-46ff-873c-063c55e7657e",

            "8f30e08e-be0c-411f-bb00-46953df6a5c7"
    };

    
    public static final String[] routes_no = { "144K",  "410",  "111",  "43B",  "43E",  "87",  "61G",  "414",  "356M",  "234B",  "137",
            "95",  "60A",  "131F",  "100",  "31E",  "79"};
    public static final double[][] CO_ORDINATES = {
            {12.956438, 77.701072},
            {12.955305, 77.700734},
            {12.954318, 77.700509},
            {12.952892, 77.700172},
            {12.951028, 77.699759},
            {12.949296, 77.699489},
            {12.949164, 77.699459},
            {12.948574, 77.699341},
            {12.947527, 77.699044},
            {12.946769, 77.698748},
            {12.945723, 77.698341},
            {12.944857, 77.697970},
            {12.942451, 77.697262},
            {12.941656, 77.696618},
            {12.940485, 77.695974},
            {12.940212, 77.695738},
            {12.938681, 77.694585},
            {12.937303, 77.693067},
            {12.936435, 77.692334},
            {12.934496, 77.690239},
            {12.930003, 77.684737},
            {12.929170, 77.683241},
            {12.928644, 77.681933},
            {12.927753, 77.680111},
            {12.927062, 77.678776}
    };

    public static String[] crowd = {"STAND", "SEAT", "CROWDED"};

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClientDaoImpl.class);

    Client client;
    static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public ClientDaoImpl() {
        Configuration configuration = new ClientConfig(JacksonJsonProvider.class);
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
        Future<Response> post = client.target("http://13.126.238.76:8080/test/v1/location")
                .request().async().post(Entity.json(location));
        System.out.println(post.get().getStatus());
    }

    public static void main(String[] args) {

        ClientDaoImpl clientDao = new ClientDaoImpl();
        int  corePoolSize  =    10;
        int  maxPoolSize   =   15;
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

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

        for(int count =0 ; count< USERS.length; count++) {
            int curIteration = i % CO_ORDINATES.length;
            System.out.println("Current value:" + curIteration);
            double latitude = CO_ORDINATES[curIteration][0];
            double longitude = CO_ORDINATES[curIteration][1];

            scheduledExecutorService.schedule(new BusRoute(clientDao, 4000, new Location(USERS[count], latitude, longitude, routes_no[count], crowd[threadLocalRandom.nextInt(0,3)])),
                    0, TimeUnit.SECONDS);

        }


//        while (true) {
//            int route_val = threadLocalRandom.nextInt(0, USERS.length);
//            int curIteration = i % CO_ORDINATES.length;
//            System.out.println("Current value:" + curIteration);
//            double latitude = CO_ORDINATES[curIteration][0];
//            double longitude = CO_ORDINATES[curIteration][1];
//            System.out.println("Latitude:" + latitude + " longitude:" + longitude);
//            BusRoute busRoute = new BusRoute(clientDao, 0, new Location(USERS[route_val], latitude, longitude, routes_no[route_val], crowd[threadLocalRandom.nextInt(0,3)]));
//            threadPoolExecutor.execute(busRoute);
//            i++;
//            try {
//                Thread.sleep( 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
