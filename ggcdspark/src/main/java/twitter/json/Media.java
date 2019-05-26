package twitter.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Media implements Serializable {
    private String media_url;
    private String type;

    public String getMedia_url() {
        return media_url;
    }

    public String getType() {
        return type;
    }

    public String toString(){
        System.out.println("Hello Media");
        return "\t\tMedia_url: " + media_url + "\t\tType:" + type + "\n";
    }
}
