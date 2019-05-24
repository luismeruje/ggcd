package twitter.json;

import java.util.ArrayList;
import java.util.List;

public class Entities {
    private List<Hashtag> hashtags = new ArrayList<>();

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("hashtags: ");
        for(Hashtag hashtag: hashtags){
            sb.append(hashtag.toString() + "; ");
        }
        return sb.toString();
    }
}
