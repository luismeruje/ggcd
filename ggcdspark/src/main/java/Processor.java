
import bigquery.ResultRegistryManagerBigQueryImp;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.bigquery.*;
import org.apache.spark.*;

import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.storage.StorageLevel;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import org.apache.spark.streaming.twitter.TwitterUtils;
import twitter.fetcher.Authentication;
import twitter.fetcher.Fetcher;
import twitter.json.Hashtag;
import twitter.json.Media;
import twitter.json.Tweet;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.auth.OAuthAuthorization;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Filter;


public class Processor {
    public static void main(String args[]) throws Exception{
        
        String jsonTest = "{\"created_at\":\"Mon Oct 01 06:29:03 +0000 2018\",\"id\":1046648167884681216,\"id_str\":\"1046648167884681216\",\"extended_entities\": {\"media\": [{\"id\": 861627472244162561,\"id_str\": \"861627472244162561\",\"indices\": [68,91],\"media_url\": \"http://pbs.twimg.com/media/C_UdnvPUwAE3Dnn.jpg\",\"media_url_https\": \"https://pbs.twimg.com/media/C_UdnvPUwAE3Dnn.jpg\",\"url\": \"https://t.co/9r69akA484\",\"display_url\": \"pic.twitter.com/9r69akA484\",\"expanded_url\": \"https://twitter.com/FloodSocial/status/861627479294746624/photo/1\",\"type\": \"photo\",\n" +
                "            \"sizes\": {\n" +
                "              \"medium\": {\n" +
                "                \"w\": 1200,\n" +
                "                \"h\": 900,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"small\": {\n" +
                "                \"w\": 680,\n" +
                "                \"h\": 510,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"thumb\": {\n" +
                "                \"w\": 150,\n" +
                "                \"h\": 150,\n" +
                "                \"resize\": \"crop\"\n" +
                "              },\n" +
                "              \"large\": {\n" +
                "                \"w\": 2048,\n" +
                "                \"h\": 1536,\n" +
                "                \"resize\": \"fit\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 861627472244203520,\n" +
                "            \"id_str\": \"861627472244203520\",\n" +
                "            \"indices\": [\n" +
                "              68,\n" +
                "              91\n" +
                "            ],\n" +
                "            \"media_url\": \"http://pbs.twimg.com/media/C_UdnvPVYAAZbEs.jpg\",\n" +
                "            \"media_url_https\": \"https://pbs.twimg.com/media/C_UdnvPVYAAZbEs.jpg\",\n" +
                "            \"url\": \"https://t.co/9r69akA484\",\n" +
                "            \"display_url\": \"pic.twitter.com/9r69akA484\",\n" +
                "            \"expanded_url\": \"https://twitter.com/FloodSocial/status/861627479294746624/photo/1\",\n" +
                "            \"type\": \"photo\",\n" +
                "            \"sizes\": {\n" +
                "              \"small\": {\n" +
                "                \"w\": 680,\n" +
                "                \"h\": 680,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"thumb\": {\n" +
                "                \"w\": 150,\n" +
                "                \"h\": 150,\n" +
                "                \"resize\": \"crop\"\n" +
                "              },\n" +
                "              \"medium\": {\n" +
                "                \"w\": 1200,\n" +
                "                \"h\": 1200,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"large\": {\n" +
                "                \"w\": 2048,\n" +
                "                \"h\": 2048,\n" +
                "                \"resize\": \"fit\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 861627474144149504,\n" +
                "            \"id_str\": \"861627474144149504\",\n" +
                "            \"indices\": [\n" +
                "              68,\n" +
                "              91\n" +
                "            ],\n" +
                "            \"media_url\": \"http://pbs.twimg.com/media/C_Udn2UUQAADZIS.jpg\",\n" +
                "            \"media_url_https\": \"https://pbs.twimg.com/media/C_Udn2UUQAADZIS.jpg\",\n" +
                "            \"url\": \"https://t.co/9r69akA484\",\n" +
                "            \"display_url\": \"pic.twitter.com/9r69akA484\",\n" +
                "            \"expanded_url\": \"https://twitter.com/FloodSocial/status/861627479294746624/photo/1\",\n" +
                "            \"type\": \"photo\",\n" +
                "            \"sizes\": {\n" +
                "              \"medium\": {\n" +
                "                \"w\": 1200,\n" +
                "                \"h\": 900,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"small\": {\n" +
                "                \"w\": 680,\n" +
                "                \"h\": 510,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"thumb\": {\n" +
                "                \"w\": 150,\n" +
                "                \"h\": 150,\n" +
                "                \"resize\": \"crop\"\n" +
                "              },\n" +
                "              \"large\": {\n" +
                "                \"w\": 2048,\n" +
                "                \"h\": 1536,\n" +
                "                \"resize\": \"fit\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 861627474760708096,\n" +
                "            \"id_str\": \"861627474760708096\",\n" +
                "            \"indices\": [\n" +
                "              68,\n" +
                "              91\n" +
                "            ],\n" +
                "            \"media_url\": \"http://pbs.twimg.com/media/C_Udn4nUMAAgcIa.jpg\",\n" +
                "            \"media_url_https\": \"https://pbs.twimg.com/media/C_Udn4nUMAAgcIa.jpg\",\n" +
                "            \"url\": \"https://t.co/9r69akA484\",\n" +
                "            \"display_url\": \"pic.twitter.com/9r69akA484\",\n" +
                "            \"expanded_url\": \"https://twitter.com/FloodSocial/status/861627479294746624/photo/1\",\n" +
                "            \"type\": \"photo\",\n" +
                "            \"sizes\": {\n" +
                "              \"small\": {\n" +
                "                \"w\": 680,\n" +
                "                \"h\": 680,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"thumb\": {\n" +
                "                \"w\": 150,\n" +
                "                \"h\": 150,\n" +
                "                \"resize\": \"crop\"\n" +
                "              },\n" +
                "              \"medium\": {\n" +
                "                \"w\": 1200,\n" +
                "                \"h\": 1200,\n" +
                "                \"resize\": \"fit\"\n" +
                "              },\n" +
                "              \"large\": {\n" +
                "                \"w\": 2048,\n" +
                "                \"h\": 2048,\n" +
                "                \"resize\": \"fit\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\"text\":\"RT @Ivan166374: Mfs be buying a 2003 Mercedes-Benz with over 100,000 miles and they swear they making money moves.\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1697668855,\"id_str\":\"1697668855\",\"name\":\"Omar\",\"screen_name\":\"Omarmartinez_13\",\"location\":\"San Tan Valley, AZ\",\"url\":null,\"description\":\"++snapchat: omar_martinez\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":513,\"friends_count\":388,\"listed_count\":1,\"favourites_count\":3535,\"statuses_count\":4199,\"created_at\":\"Sat Aug 24 23:15:04 +0000 2013\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1046505567835971584\\/V1JuZBc__normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1046505567835971584\\/V1JuZBc__normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1697668855\\/1531896285\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\": {\"type\": \"Point\", \"coordinates\": [-73.9998279,40.74118764]},\"place\": {\"id\": \"07d9db48bc083000\",\"url\": \"https://api.twitter.com/1.1/geo/id/07d9db48bc083000.json\",\"place_type\": \"poi\",\"name\": \"McIntosh Lake\",\"full_name\": \"McIntosh Lake\",\"country_code\": \"US\",\"country\": \"United States\",\"bounding_box\": {\"type\": \"Polygon\",\"coordinates\": [[[-105.14544,40.192138],[-105.14544,40.192138],[-105.14544,40.192138],[-105.14544,40.192138]]]},\"attributes\": {}},\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Sun Sep 30 17:53:59 +0000 2018\",\"id\":1046458147139809280,\"id_str\":\"1046458147139809280\",\"text\":\"Mfs be buying a 2003 Mercedes-Benz with over 100,000 miles and they swear they making money moves.\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1698294474,\"id_str\":\"1698294474\",\"name\":\"Ivan Retana Santos\",\"screen_name\":\"Ivan166374\",\"location\":\"AZ\",\"url\":null,\"description\":\"Always Strive and Prosper\\ud83e\\udd1f\\ud83c\\udffc\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":570,\"friends_count\":286,\"listed_count\":0,\"favourites_count\":18064,\"statuses_count\":20752,\"created_at\":\"Sun Aug 25 06:23:17 +0000 2013\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"000000\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme14\\/bg.gif\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme14\\/bg.gif\",\"profile_background_tile\":false,\"profile_link_color\":\"89C9FA\",\"profile_sidebar_border_color\":\"000000\",\"profile_sidebar_fill_color\":\"000000\",\"profile_text_color\":\"000000\",\"profile_use_background_image\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1024718057061736449\\/aU3Wzem__normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1024718057061736449\\/aU3Wzem__normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1698294474\\/1533187398\",\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":{\"id\":\"44d207663001f00b\",\"url\":\"https:\\/\\/api.twitter.com\\/1.1\\/geo\\/id\\/44d207663001f00b.json\",\"place_type\":\"city\",\"name\":\"Mesa\",\"full_name\":\"Mesa, AZ\",\"country_code\":\"US\",\"country\":\"United States\",\"bounding_box\":{\"type\":\"Polygon\",\"coordinates\":[[[-111.894548,33.306275],[-111.894548,33.505234],[-111.580583,33.505234],[-111.580583,33.306275]]]},\"attributes\":{}},\"contributors\":null,\"is_quote_status\":false,\"quote_count\":0,\"reply_count\":1,\"retweet_count\":4,\"favorite_count\":20,\"entities\":{\"hashtags\":[{\"text\":\"MaineAkHypeBandOnEB\",\"indices\":[45,65]}],\"urls\":[],\"user_mentions\":[],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"en\"},\"is_quote_status\":false,\"quote_count\":0,\"reply_count\":0,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[{\"text\":\"MaineAkHypeBandOnEB\",\"indices\":[45,65]}],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"Ivan166374\",\"name\":\"Ivan Retana Santos\",\"id\":1698294474,\"id_str\":\"1698294474\",\"indices\":[3,14]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1538375343663\"}";
        //Tweet tweet = mapper.readValue(jsonTest,Tweet.class);
        //JSONObject json = new JSONObject(jsonTest);
        //System.out.println(json.toString(4));
        //System.out.println(tweet.toString());
        //resultRegistryManager.registerTweet(tweet);
        /*Map<String,List<String>> imageLabelsMap = new HashMap<>();
        for(Media media: tweet.getExtended_entities().getMedia()){
            if(media.getType().equals("photo") && media.getMedia_url().endsWith(".jpg")){
                List<String> labels = imageClassifierStub.classifyImage(media.getMedia_url());
                imageLabelsMap.put(media.getMedia_url(),labels);
                System.out.println(labels);
            }
        }
        resultRegistryManager.registerImageLabel(tweet,imageLabelsMap);
        //imageClassifierStub.classifyImage()

        */

        List<String> tweetList = new ArrayList<>();
        tweetList.add(jsonTest);

        ImageClassifierStub imageClassifierStub = new ImageClassifierStub();

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SparkConf conf = new SparkConf().setMaster("spark://" + args[0] + ":7077").setAppName("TwitterAnalysis");

        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        Queue<JavaRDD<String>> rddQueue =
                new LinkedList<>();

        rddQueue.add(jssc.sparkContext().parallelize(tweetList));

        JavaDStream<String> dStream =
                jssc.queueStream(rddQueue);

        JavaDStream<Tweet> tweets = dStream.map(string -> {
            return mapper.readValue(string,Tweet.class);
        });

        JavaDStream<Tweet> tweets2 = tweets.map(tweet -> {
            ResultRegistryManagerBigQueryImp.registerTweet(tweet);
            return tweet;
/*


        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        OAuthAuthorization auth = Authentication.authentication();

        String[] countries = {"us"};
        String[] languages = {};
        FilterQuery query = Fetcher.getQuery(
                new HashSet<>(Arrays.asList(countries),
                new HashSet<>(Arrays.asList(languages))));

        JavaReceiverInputDStream<Status> stream =
                TwitterUtils.createFilteredStream(jssc, auth, query, StorageLevel.MEMORY_ONLY_SER_2());

        Fetcher f = new Fetcher(auth,
                     60000,
                       "src/main/resources/output.txt");
        f.start(query);

        JavaDStream<Tweet> tweets = tweets.map(rdd -> {
            return mapper.readValue(rdd,Tweet.class);
*/
        });


        tweets2.print();

        //URL: twitter.com/statuses/<ID>
        //OR: https://twitter.com/{twitter-user-id}/status/{tweet-status-id}

        jssc.start();// Start the computation
        jssc.awaitTermination();

    }


}
