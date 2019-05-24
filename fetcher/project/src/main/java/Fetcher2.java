
/* -------------------------------------------------------------------------- */

import twitter4j.FilterQuery;
import twitter4j.RawStreamListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Fetcher2 {
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

//        private Thread statsT;

        //Listener(long printInterval)
        //{

        //}

        @Override
        public void onMessage(String str) {
            counter++;


            System.out.println(str);
            //System.out.println(counter);
        }

        @Override
        public void onException(Exception e) {
            e.printStackTrace();
        }

    }

    /* ---------------------------------------------------------------------- */

    public Fetcher(Configuration config,
                   int nListerners) {
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

        } catch (Exception e) {

        }

    }

    /* ---------------------------------------------------------------------- */

    public void start() {
        FilterQuery query = new FilterQuery();

        double[] southwestcornerPT = {-9.52657060387, 36.838268541};
        double[] northeastcornerPT = {-6.3890876937, 42.280468655};

        double[] southwestcornerUS = {-171.791110603, 18.91619};
        double[] northeastcornerUS = {-66.96466, 71.3577635769};

        double[] southwestcornerES = {-9.39288367353, 35.946850084};
        double[] northeastcornerES = {3.03948408368, 43.7483377142};

        double[] southwestcornerUK = {-7.57216793459, 49.959999905};
        double[] northeastcornerUK = {1.68153079591, 58.6350001085};

        double[] southwestcornerFR = {-54.5247541978, 2.05338918702};
        double[] northeastcornerFR = {9.56001631027, 51.1485061713};

        query.locations(southwestcornerPT, northeastcornerPT);
        //query.locations(southwestcornerUS, northeastcornerUS);
        query.locations(southwestcornerES, northeastcornerES);
        query.locations(southwestcornerUK, northeastcornerUK);
        query.locations(southwestcornerFR, northeastcornerFR);
        query.language("en");
        this.twitter.filter(query);
    }

    /* ---------------------------------------------------------------------- */

}