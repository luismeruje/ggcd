package twitter.json;

import java.util.ArrayList;
import java.util.List;

public class ExtendedEntities {
    private List<Media> media = new ArrayList<>();

    public List<Media> getMedia() {
        return media;
    }

    public String toString(){
        System.out.println("Hello extended");
        return media.toString();
    }
}
