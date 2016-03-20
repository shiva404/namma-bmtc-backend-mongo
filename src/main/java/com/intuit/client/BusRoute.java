package com.intuit.client;

import com.intuit.types.Location;

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


    @Override
    public void run() {
        try {
            clientDaoImpl.updatePosition(location);
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
