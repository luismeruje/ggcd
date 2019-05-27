package bigquery;




import com.google.cloud.bigquery.*;
import twitter.json.Hashtag;
import twitter.json.Media;
import twitter.json.Tweet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class ResultRegistryManagerBigQueryImp{ // implements ResultRegistryManager {

    /*public static void registerTweet(Tweet tweet){
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "ggcd";
        insertIntoTweetTable(tweet,bigquery,datasetName);
        insertIntoHashtagTable(tweet,bigquery,datasetName);

    }*/

    /*public static void registerTweet(Status status){
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "ggcd";
        System.out.println("Going to insert into tweet table");
        insertIntoTweetTable(status,datasetName);
        System.out.println("Inserted into tweet table");
        insertIntoHashtagTable(status,datasetName);
        System.out.println("Inserted into hashtag table");
    }*/

    public static void registerImageLabel(Tweet tweet, Map<String, List<String>> labelsMap){
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "ggcd";
        labelsMap.forEach((imageReference,labels)-> insertIntoImageTable(imageReference,labels,tweet.getId_str(),datasetName));
    }

    private static Map<String,Object> insertIntoTweetTable(Tweet tweet, String datasetName){
        TableId tweetTableId = TableId.of(datasetName,"tweet");
        Map<String,Object> row = new HashMap<>();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tweet.getTimestamp_ms())), ZoneId.systemDefault());
        String time = localDateTime.toString().replace("T"," ");
        //String format or something? Search for a more efficient way
        //System.out.println(Integer.valueOf(tweet.getTimestamp_ms()));
        String coordinates = "{ \"type\": \"Point\", \"coordinates\": " + tweet.getCoordinates().getCoordinates().toString() + " }";

        row.put("reference",tweet.getId_str());
        row.put("place", tweet.getPlace().toString());
        row.put("timestamp", time);
        row.put("coordinates", coordinates);
        System.out.println("Executing insert");
        return row;
        //executeInsert(row,bigquery,tweetTableId);
    }

    public static Map<String,Object> insertIntoTweetTable(Status status, String datasetName){
        TableId tweetTableId = TableId.of(datasetName,"tweet");
        Map<String,Object> row = new HashMap<>();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(status.getCreatedAt().toInstant(), ZoneId.systemDefault());
        String time = localDateTime.toString().replace("T"," ");
        //String format or something? Search for a more efficient way
        //System.out.println(Integer.valueOf(tweet.getTimestamp_ms()));
        String coordinates = null;
        if(status.getGeoLocation() != null) {
            coordinates = "{ \"type\": \"Point\", \"coordinates\": [" + status.getGeoLocation().getLatitude() + "," + status.getGeoLocation().getLongitude() + "] }";
        }


        row.put("reference",String.valueOf(status.getId()));
        if(status.getPlace() != null)
            row.put("place", status.getPlace().getFullName());
        row.put("timestamp", time);
        if(coordinates != null)
            row.put("coordinates", coordinates);

        //executeInsert(row,bigquery,tweetTableId);
        return row;
    }

    //TODO: only returning one row
    private static Map<String,Object> insertIntoHashtagTable(Tweet tweet,String datasetName){
        Map<String,Object> row = new HashMap<>();
        TableId hashtagTableId = TableId.of(datasetName,"hashtag");
        row.put("tweet_id",tweet.getId_str());
        for(Hashtag hashtag: tweet.getEntities().getHashtags()) {
            row.put("text", hashtag.getText());
            //executeInsert(row,bigquery,hashtagTableId);
        }
        return row;

    }


    public static List<Map<String,Object>> insertIntoHashtagTable(Status status,String datasetName){
        List<Map<String,Object>> rows = new ArrayList<>();
        TableId hashtagTableId = TableId.of(datasetName,"hashtag");
        for(HashtagEntity hashtag: status.getHashtagEntities()) {
            Map<String,Object> row = new HashMap<>();
            row.put("tweet_id",status.getId());
            row.put("text", hashtag.getText());
            rows.add(row);
            //executeInsert(row,bigquery,hashtagTableId);
        }
        return rows;

    }

    public static Map<String,Object> insertIntoImageTable(String imageReference, List<String> labels, String tweet_id,String datasetName){
        Map<String,Object> row = new HashMap<>();
        TableId imageTableId = TableId.of(datasetName,"image");
        row.put("reference",imageReference);
        row.put("tweet_id",tweet_id);
        for(int i = 0; i < labels.size() && i < 5; i++) {
            row.put("label_" + (i+1), labels.get(i));
        }
        //executeInsert(row,bigquery,imageTableId);
        return row;
    }


    public static int executeInsert(List<Map<String,Object>> rows, TableId tableId){
        if(rows != null && rows.size() > 0) {
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
            InsertAllRequest.Builder request = InsertAllRequest.newBuilder(tableId);
            for (Map<String, Object> row : rows) {
                request.addRow(row);
            }
            InsertAllResponse insertResponse = bigquery.insertAll(request.build());
            if (insertResponse.hasErrors()) {
                System.out.println("Errors occurred while inserting rows");
                insertResponse.getInsertErrors().forEach((k, error) -> System.out.println(error.toString()));
            } else {
                System.out.println("Insert successful");
            }
            return 0;
        }
        else
            return -1;
    }


}
