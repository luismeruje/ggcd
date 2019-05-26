import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;


public class Main
{

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException
    {

        Configuration config = Authentication.authentication();

        if (config != null)
        {
            Fetcher f = new Fetcher(config, 1, 60000, "src/main/resources/output.txt");
            f.start();
        }


    }
}
