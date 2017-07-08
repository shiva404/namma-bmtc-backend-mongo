package com.intuit.client;

import com.intuit.types.Location;

import java.util.Random;

import static com.intuit.client.ClientDaoImpl.CO_ORDINATES;

/**
 *
 */
public class BusRoute implements Runnable {
    private ClientDaoImpl clientDaoImpl;
    private long delay ;
    private Location location;
    public BusRoute(ClientDaoImpl clientDaoImpl, int delay, Location location) {
        this.clientDaoImpl = clientDaoImpl;
        this.delay = delay;
        this.location = location;
    }

    static Random random = new Random();

    @Override
    public void run() {
        try {
            int i = random.nextInt(CO_ORDINATES.length - 1);
            while (true) {
                int curIteration = i % CO_ORDINATES.length;
                System.out.println("Current value:" + curIteration);
                double latitude = CO_ORDINATES[curIteration][0];
                double longitude = CO_ORDINATES[curIteration][1];

                location.setLongitude(longitude);
                location.setLatitude(latitude);

                System.out.println( "Updating " + location.getRefToken() + " -->" + location.getRouteNumber() + " -- " + location.getLatitude() + " - " + location.getLongitude());

                clientDaoImpl.updatePosition(location);
                i++;
                Thread.sleep(delay);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
