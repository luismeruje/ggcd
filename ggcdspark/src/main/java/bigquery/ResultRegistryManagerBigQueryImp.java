package bigquery;




import com.google.cloud.bigquery.*;
import twitter.json.Hashtag;
import twitter.json.Media;
import twitter.json.Tweet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class ResultRegistryManagerBigQueryImp{ // implements ResultRegistryManager {
    private static BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

    public static void registerTweet(Tweet tweet){
        String datasetName = "ggcd";
        insertIntoTweetTable(tweet,bigquery,datasetName);
        insertIntoHashtagTable(tweet,bigquery,datasetName);
    }

    public static void registerTweet(Status status){
        String datasetName = "ggcd";
        insertIntoTweetTable(status,bigquery,datasetName);
        insertIntoHashtagTable(status,bigquery,datasetName);
    }

    public static void registerImageLabel(Tweet tweet, Map<String, List<String>> labelsMap){
        String datasetName = "ggcd";
        labelsMap.forEach((imageReference,labels)-> insertIntoImageTable(imageReference,labels,tweet.getId_str(),bigquery,datasetName));
    }

    private static void insertIntoTweetTable(Tweet tweet, BigQuery bigquery, String datasetName){
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

        executeInsert(row,bigquery,tweetTableId);
    }

    private static void insertIntoTweetTable(Status status, BigQuery bigquery, String datasetName){
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

        executeInsert(row,bigquery,tweetTableId);
    }

    private static void insertIntoHashtagTable(Tweet tweet, BigQuery bigquery,String datasetName){
        Map<String,Object> row = new HashMap<>();
        TableId hashtagTableId = TableId.of(datasetName,"hashtag");
        row.put("tweet_id",tweet.getId_str());
        for(Hashtag hashtag: tweet.getEntities().getHashtags()) {
            row.put("text", hashtag.getText());
            executeInsert(row,bigquery,hashtagTableId);
        }

    }

    private static void insertIntoHashtagTable(Status status, BigQuery bigquery,String datasetName){
        Map<String,Object> row = new HashMap<>();
        TableId hashtagTableId = TableId.of(datasetName,"hashtag");
        row.put("tweet_id",status.getId());
        for(HashtagEntity hashtag: status.getHashtagEntities()) {
            row.put("text", hashtag.getText());
            executeInsert(row,bigquery,hashtagTableId);
        }

    }

    private static  void insertIntoImageTable(String imageReference, List<String> labels, String tweet_id, BigQuery bigquery,String datasetName){
        Map<String,Object> row = new HashMap<>();
        TableId imageTableId = TableId.of(datasetName,"image");
        row.put("reference",imageReference);
        row.put("tweet_id",tweet_id);
        for(int i = 0; i < labels.size() && i < 5; i++) {
            row.put("label_" + (i+1), labels.get(i));
        }
        executeInsert(row,bigquery,imageTableId);

    }

    //TODO: group inserts
    private static void executeInsert(Map<String,Object> row, BigQuery bigquery, TableId tableId){

        InsertAllResponse insertResponse = bigquery.insertAll(InsertAllRequest.newBuilder(tableId).addRow(row).build());
        if (insertResponse.hasErrors()) {
            System.out.println("Errors occurred while inserting rows");
            insertResponse.getInsertErrors().forEach((k,error)->System.out.println(error.toString()));
        } else{
            System.out.println("Insert successful");
        }
    }


}
