import twitter.json.Tweet;

import java.util.List;

public interface ResultRegistryManager {
    public void registerTweet(Tweet tweet);
    public void registerTweetImageLabels(List<String> labels, Tweet tweet);
}
