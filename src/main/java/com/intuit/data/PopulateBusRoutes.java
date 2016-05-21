package com.intuit.data;

import com.intuit.mongo.MongoDao;
import com.intuit.types.BusRoute;
import com.mongodb.MongoClient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 */
public class PopulateBusRoutes {
    private static final String ROUTE_NO = "route_no";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String ROUTE = "route";

    private static final String [] FILE_HEADER_MAPPING = {"route_no","from","to","route"};


    public static void main(String[] args) {
        PopulateBusRoutes populateBusRoutes = new PopulateBusRoutes();
        List<BusRoute> busRoutes = populateBusRoutes.readCsvFile("C:\\Personal\\Namma BMTC\\namma-bmtc-backend-mongo\\src\\main\\resources\\bus_routes.csv");

        MongoClient mongoClient = new MongoClient("127.0.0.1");
        MongoDao mongoDao = new MongoDao(mongoClient, "namma-bmtc", "loc","bus_routes","thanks");

        for(BusRoute busRoute : busRoutes){
            mongoDao.persistBusRouts(busRoute);
        }
    }

    public List<BusRoute> readCsvFile(String fileName) {

        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {

            //Create a new list of student to be filled by CSV file data
            List<BusRoute> routes = new ArrayList<>();

            //initialize FileReader object
            fileReader = new FileReader(fileName);


            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                //Create a new route object and fill his data
                BusRoute route = new BusRoute(record.get(ROUTE_NO), record.get(FROM), record.get(TO), record.get(ROUTE));
                routes.add(route);
            }
           return routes;
        }

        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(fileReader != null)
                fileReader.close();
                if(csvFileParser != null)
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
    }
}
