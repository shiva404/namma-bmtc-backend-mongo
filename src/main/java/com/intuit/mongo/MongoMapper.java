package com.intuit.mongo;

import com.intuit.types.Location;
import com.mongodb.BasicDBObject;
import org.bson.Document;

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
}
