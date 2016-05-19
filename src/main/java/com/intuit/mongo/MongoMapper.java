package com.intuit.mongo;

import com.intuit.types.BusRoute;
import com.intuit.types.Location;
import com.mongodb.BasicDBObject;

/**
 *
 */
public class MongoMapper {
    public static BasicDBObject getLocationDocument(String refToken, Location location) {
        final BasicDBObject locationDBObject = new BasicDBObject();
        locationDBObject.put("refToken", refToken);
        locationDBObject.put("routeNumber", location.getRouteNumber());
        locationDBObject.put("crowdLevel", location.getCrowdLevel());
        locationDBObject.put("loc", new double[]{location.getLatitude(), location.getLongitude()});
        return locationDBObject;
    }

    public static BasicDBObject getBusRouteObject(BusRoute busRoute){
        final BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("routeNumber", busRoute.getBusRoute());
        basicDBObject.put("from",busRoute.getFrom());
        basicDBObject.put("to", busRoute.getTo());
        basicDBObject.put("details", busRoute.getDetails());
        return basicDBObject;
    }
}
