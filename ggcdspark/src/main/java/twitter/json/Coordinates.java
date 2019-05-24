package twitter.json;

import java.util.ArrayList;
import java.util.List;

public class Coordinates {
    List<Float> coordinates = new ArrayList<>();

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public String toString(){
        return coordinates.toString();
    }
}
