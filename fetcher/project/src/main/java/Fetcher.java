



/* -------------------------------------------------------------------------- */

import twitter4j.FilterQuery;
import twitter4j.RawStreamListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Fetcher
{
    private TwitterStream twitter;

    private int nListerners;
    private List<Listener> listeners;

    private Socket socket;

    private Configuration config;
    private FilterQuery query;
    private String lang;

    /* ---------------------------------------------------------------------- */

    class Listener implements RawStreamListener {

        private long counter;

        private Thread

        @Override
        public void onMessage(String str)
        {
            counter++;


            //System.out.println(str);
            //System.out.println(counter);
        }

        @Override
        public void onException(Exception e)
        {
            e.printStackTrace();
        }

    }

    /* ---------------------------------------------------------------------- */

    public Fetcher(Configuration config,
                   int nListerners)
    {
        try {

            //this.socket = new Socket(address, port);

            this.config = config;
            this.twitter = new TwitterStreamFactory(config).getInstance();

            this.nListerners = nListerners;
            this.listeners = new ArrayList<Listener>();
            for (int i = 0; i < nListerners; ++i) {
                Listener l = new Listener();
                this.twitter.addListener(l);
                this.listeners.add(l);
            }

        }
        catch(Exception e)
        {

        }

    }

    /* ---------------------------------------------------------------------- */

    public void start()
    {
        FilterQuery query = new FilterQuery();

        double[] southwestcornerPT = {-9.52657060387, 36.838268541};
        double[] northeastcornerPT = {-6.3890876937, 42.280468655};

        double[] southwestcornerUS= {-171.791110603, 18.91619};
        double[] northeastcornerUS = {-66.96466, 71.3577635769};


        query.locations(southwestcornerUS, northeastcornerUS);
        //query.language("pt");
        this.twitter.filter(query);
    }

    /* ---------------------------------------------------------------------- */


}

/* -------------------------------------------------------------------------- */
