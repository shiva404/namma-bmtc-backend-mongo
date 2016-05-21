package com.intuit.mongo;

import com.intuit.types.BusRoute;
import com.intuit.types.Location;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.DeleteResult;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class MongoDao {
    private MongoCollection<BasicDBObject> locationsCollection;
    private MongoCollection<BasicDBObject> busRoutesCollection;
    private MongoCollection<BasicDBObject> thanksCollection;


    MongoDatabase mongoDatabase;

    public MongoDao(MongoClient mongoClient, String database, String locationDB, String busRoutesDB,String thanksDB){
        this.mongoDatabase = mongoClient.getDatabase(database);
        locationsCollection = mongoDatabase.getCollection(locationDB, BasicDBObject.class);
        busRoutesCollection = mongoDatabase.getCollection(busRoutesDB, BasicDBObject.class);
        thanksCollection = mongoDatabase.getCollection(thanksDB,BasicDBObject.class);
    }

    public String insertLocation(Location location) {
        String refToken = UUID.randomUUID().toString();
        BasicDBObject basicDBObject = MongoMapper.getLocationDocument(refToken, location);
        locationsCollection.insertOne(basicDBObject);
        return refToken;
    }

    public List<Location> getLocations(Double lat, Double longitude, Double rangeInKm) {
        List<Location> locations = new LinkedList<>();

        BasicDBObject geoFilter = new BasicDBObject("$geometry", new Point(new Position(lat, longitude)));
        geoFilter.append("$maxDistance", rangeInKm * 1000);

        BasicDBObject nearFilter = new BasicDBObject("$near", geoFilter);

        FindIterable<BasicDBObject> basicDBObjects = locationsCollection.find(new BasicDBObject("loc", nearFilter));
        for(BasicDBObject basicDBObject : basicDBObjects){
            BasicDBList basicDBList = (BasicDBList) basicDBObject.get("loc");
            Double tempLat = (Double) basicDBList.get(0);
//            System.out.println(tempLat);
            Double tempLong = (Double)basicDBList.get(1);
//            System.out.println(tempLong);
            String crowdLevel = basicDBObject.getString("crowdLevel");
            String refToken = basicDBObject.getString("refToken");
            String routeNumber = basicDBObject.getString("routeNumber");
            locations.add(new Location(refToken, tempLat, tempLong, routeNumber, crowdLevel));
        }
        return locations;
    }

    public String updateLocation(Location location) {
        locationsCollection.findOneAndReplace(new BasicDBObject("refToken", location.getRefToken()), MongoMapper.getLocationDocument(location.getRefToken(), location));
        return location.getRefToken();
    }

    public void deleteLocation(String refToken) {
        locationsCollection.findOneAndDelete(new BasicDBObject("refToken", new BasicDBObject("$eq", refToken)));
        DeleteResult refToken1 = locationsCollection.deleteOne(new BasicDBObject("refToken", refToken));
        System.out.println(refToken1.getDeletedCount());
    }

    public void persistBusRouts(BusRoute busRoutes){
        BasicDBObject basicDBObject = MongoMapper.getBusRouteObject(busRoutes);
        busRoutesCollection.insertOne(basicDBObject);
    }

    public BusRoute getBusRoute(String routeNumber) {

        BusRoute busRoute = new BusRoute();
        FindIterable<BasicDBObject> basicDBObjects = busRoutesCollection.find(new BasicDBObject("routeNumber", routeNumber));
        BasicDBObject basicDBObject = basicDBObjects.first();

        if(null != basicDBObject)
        {
            busRoute.setBusRoute(routeNumber);
            busRoute.setFrom(basicDBObject.get("from").toString());
            busRoute.setTo(basicDBObject.get("to").toString());
            busRoute.setDetails(basicDBObject.get("details").toString());

        }

        return busRoute;

    }

    public void sayThanks(String refToken)
    {

        FindIterable<BasicDBObject> basicDBObjects = thanksCollection.find(new BasicDBObject("refToken", refToken));
        BasicDBObject basicDBObject = basicDBObjects.first();

        if(null!= basicDBObject)
        {
            int count = basicDBObject.getInt("count");
            count++;
            thanksCollection.findOneAndReplace(new BasicDBObject("refToken", refToken),MongoMapper.getThanksCollectionObject(refToken,count));
        }
        else
        {
            BasicDBObject basicDBObjectInsert = MongoMapper.getThanksCollectionObject(refToken,1);
            thanksCollection.insertOne(basicDBObjectInsert);
        }
    }
}
