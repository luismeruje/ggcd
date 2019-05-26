package twitter.json;

import java.io.Serializable;

public class Hashtag implements Serializable {
    String text;

    public String getText() {
        return text;
    }

    public String toString(){
        return text;
    }
}
