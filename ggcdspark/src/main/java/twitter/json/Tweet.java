package twitter.json;

public class Tweet {
    private String id_str;
    private String timestamp_ms;
    private String text;
    private Entities entities;
    private Coordinates coordinates;
    private Place place;
    private ExtendedEntities extended_entities;


    public String getId_str() {
        return id_str;
    }

    public String getTimestamp_ms() {
        return timestamp_ms;
    }

    public String getText() {
        return text;
    }

    public Entities getEntities() {
        return entities;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Place getPlace() {
        return place;
    }

    public ExtendedEntities getExtended_entities() {
        return extended_entities;
    }

    public String toString(){
        return "Tweet - " + "\n\t Id_str: " + id_str + "\n\t timestamp: " + timestamp_ms + "\n\t text: \"" + text + "\"\n\t entities: \n\t\t" + entities  +
                "\n\t coordinates: " + coordinates + "\n\t place: " + place + "\n\t extended_entities: \n\t\t" + extended_entities;
    }


}
