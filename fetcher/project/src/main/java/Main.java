/* -------------------------------------------------------------------------- */

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;
import java.util.HashSet;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

/* -------------------------------------------------------------------------- */

/**
 * Initial entity that defines the flow of the program, that is, for the usage
 * of the twitter stream authentication is necessary.
 *
 * @author Ângela Amorim
 * @author Fábio Fontes
 * @author Luís Ferreira
 */
public class Main
{

    public static void main(String[] args)
            throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        Configuration config = Authentication.authentication();

        if (config != null)
        {
            Fetcher f = new Fetcher(config,
                             1,
                          60000,
                         "src/main/resources/output.txt");

            String[] lang = {};
            String[] countries = {"us"};
            f.start(new HashSet<>(Arrays.asList(countries)),
                    new HashSet<>(Arrays.asList(lang)));
        }
    }
}

/* -------------------------------------------------------------------------- */
