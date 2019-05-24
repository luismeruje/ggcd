import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;


public class Main
{

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException
    {

        //Configuration config = Authentication.authentication();


        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("1956ypcvBslnaq5FPuFYTc84t")
          .setOAuthConsumerSecret("0ZPrs8M6gSxBTuvhDk1PUsWlYW3Rvc5M0ylpm6rKGMpfzzBSi0")
          .setOAuthAccessToken("1120736790275678208-QJaNE5hGsELxlz5yLQqWSqhnEdk8Ow")
          .setOAuthAccessTokenSecret("zGNFBAJh5ojoidpcK6cUtxk6oWnVPSanb79wX0mRE9pot");



        Fetcher f = new Fetcher((cb.build()), 1);
        f.start();

        //try {
          //  Thread.sleep(TimeUnit.SECONDS.toMillis(20));
        //}
        //catch(InterruptedException e)
        ////  e.printStackTrace();
        //}
        //twitterStream.shutdown();

    }
}
