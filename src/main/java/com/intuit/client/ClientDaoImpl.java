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

    public static final String[] routes = { "a3b0db52-a10f-48de-b360-d824f1246a91",  "9ec94be4-e542-48c2-bd39-8ea181a3cf42",
           "9b8a65a4-0b00-41d0-b778-17a187cc7e32",  "8ee284f4-71ce-4e2f-a2a5-c23c35dd8a55",  "86adad04-1b73-403e-94c3-bc63ee7fb09e",
           "b88db6e2-a601-48ae-8584-96f30847b9b5",  "0597d270-87e9-4b67-9c5b-66967edbac83",  "1eebc47a-ea5f-4939-a7e5-174cfbea4614",
           "71a63807-3c89-400e-b764-1586796932cc",  "bc4087b1-4e1b-4b72-a0d7-b568a9cb5336",  "5a726197-b4b2-4031-8614-0cde3405e10c",
           "9517924b-be3e-4ec0-8fb3-5fba40206cd6",  "2e4711be-5bee-40af-8c27-4ca251ca1264",  "698fdac7-409c-4bcf-9bf4-ad7197c6d9c9",
           "0b2febf2-9706-41af-a578-a5af5de91ef3",  "80449238-b215-430a-982b-cb9b4bf0fe68",  "f27c5c2d-538c-4c00-a341-bef1b99f45a2",
           "155ef3e7-0cbe-46cc-bed3-05f3038a124d",  "5830aaa0-6f5d-4633-9955-b1fcc6f9a0d9",  "16ef92d5-d030-4130-8285-b52f83ceab5a",
           "4a3d4119-924a-4e52-ab3f-9509b24da627",  "bee06e95-a79f-4a05-960c-99683bb65fb4",  "f4e5ec17-d58e-4fac-9e1f-44ce4f07f366",
           "bd3d0b4d-dd63-40a2-ac0a-489580e85547",  "5ee67fde-1dee-4aad-93c0-fe61bb1a98c9",  "9585b474-b430-402e-b7af-5c3559d09196",
           "b018c100-48b8-4239-bcf1-a19277b659b4",  "e329e463-313a-4746-93ef-10aabcfffae1",  "9eb22784-e5a7-4bb5-82fa-ae103b26d19f",
           "268e2c71-d9aa-4596-9837-f90951eb0758",  "023bc36b-bb91-4121-aa27-de69cf26a710",  "a7388548-82ce-46a0-a676-15df216db2a5",
           "ad2c3073-679c-4206-87a6-b263f248805d",  "bd6cbe22-e852-4198-b3e5-3b5275424cd0",  "be5ae1f9-8239-4663-9b61-7009116f9737",
           "220dce80-7725-470e-8ea6-a3c235252b3e" };

    public static final String[] routes_no = { "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501",
            "95",  "60A",  "216A",  "23D",  "34F",  "99",  "101",  "201B",  "201R", "501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",
            "610",  "56",  "34",  "501","501A",  "410",  "335E",  "43A",  "43C",  "36A",  "34B",  "610",  "56",  "34",  "501", };
    public static final double[][] values = {
            {12.956496, 77.701229},
            {12.954101, 77.700231},
            {12.950084, 77.699696},
            {12.942244, 77.697119},
            {12.939568, 77.695603},
            {12.935384, 77.691244},
            {12.931803, 77.687311},
            {12.929780, 77.684879},
            {12.927756, 77.680706},
            {12.925611, 77.676072},
            {12.924650, 77.674164},
            {12.921666, 77.667895},
            {12.920501, 77.665085},
            {12.921125, 77.662660},
            {12.921585, 77.661094},
            {12.922505, 77.658025},
            {12.924199, 77.650730},
            {12.923049, 77.646481},
            {12.919598, 77.643112},
            {12.917151, 77.640859},
            {12.915875, 77.637876},
            {12.915854, 77.637919},
            {12.917507, 77.638134},
            {12.912403, 77.638048},
            {12.912069, 77.644378},
            {12.911933, 77.648820},
            {12.908712, 77.649300},
            {12.908158, 77.647594}};

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
        Future<Response> post = client.target("http://lb-1816851115.ap-southeast-1.elb.amazonaws.com/test/v1/location")
                .request().async().post(Entity.json(location));
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
            int route_val = threadLocalRandom.nextInt(0, 31);
            int currValue = i %20;
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
