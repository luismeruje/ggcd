package twitter.json;

import java.io.Serializable;

public class Place implements Serializable {
    String full_name;
    String country_code;

    public String getFull_name() {
        return full_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String toString(){
        return full_name + ":" + country_code;
    }
}
