



/* -------------------------------------------------------------------------- */

import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Fetcher
{
    private TwitterStream twitter;

    private int nListerners;
    private List<RawListener> listeners;


    /* ---------------------------------------------------------------------- */

    class RawListener implements RawStreamListener {

        private Integer counter;

        private Thread statsT;


        RawListener(final long printInterval, final String filename)
        {
            this.counter = 0;

            this.statsT = new Thread(
                    () -> printStats(printInterval, filename)
            );
            this.statsT.start();
        }

        public void printStats(final long printInterval, final String filename)
        {
            PrintWriter writer = null;
            try {

                int c = 0;
                long printIntervalMin =
                        TimeUnit.MILLISECONDS.toMinutes(printInterval);


                if (filename != null)
                    writer = new PrintWriter(new FileOutputStream(filename, true), true);
                else
                    writer = new PrintWriter(System.out, true);

                writer.println("Begin");
                writer.println(printIntervalMin);

                while(true)
                {
                    synchronized (counter)
                    {
                        c = counter;
                    }

                    float result = ((float) c) / printIntervalMin;
                    writer.println(result);

                    Thread.currentThread().sleep(printInterval);
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                if (writer != null)
                {
                    writer.println("End");
                    writer.close();
                }
            }


        }

        @Override
        public void onMessage(String str)
        {
            counter++;

            // TODO
            System.out.println(str);
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
                   int nListerners,
                   long printInterval,
                   String filename)
    {
        try {

            //this.socket = new Socket(address, port);
            this.twitter = new TwitterStreamFactory(config).getInstance();

            this.nListerners = nListerners;
            this.listeners = new ArrayList<RawListener>();
            for (int i = 0; i < nListerners; ++i) {
                RawListener l = new RawListener(printInterval, filename);
                this.twitter.addListener(l);
                this.listeners.add(l);
            }

        }
        catch(Exception e)
        {

        }

    }

    /* ---------------------------------------------------------------------- */

    public void start(String[] countries, String[] lang)
    {
        FilterQuery query = new FilterQuery();

        double[] southwestcornerPT = {-9.52657060387, 36.838268541};
        double[] northeastcornerPT = {-6.3890876937, 42.280468655};

        double[] southwestcornerUS= {-171.791110603, 18.91619};
        double[] northeastcornerUS = {-66.96466, 71.3577635769};

        double[] southwestcornerES= {-9.39288367353, 35.946850084};
        double[] northeastcornerES = {3.03948408368, 43.7483377142};

        double[] southwestcornerUK = {-7.57216793459, 49.959999905};
        double[] northeastcornerUK = {1.68153079591, 58.6350001085};

        double[] southwestcornerFR = {-54.5247541978, 2.05338918702};
        double[] northeastcornerFR = {9.56001631027, 51.1485061713};

        for(country : countries) {
            switch (country) {
                //query.locations(southwestcornerPT, northeastcornerPT);
                query.locations(southwestcornerUS, northeastcornerUS);
                //query.locations(southwestcornerES, northeastcornerES);
                //query.locations(southwestcornerUK, northeastcornerUK);
                //query.locations(southwestcornerFR, northeastcornerFR);
            }
        }
        //query.language("en");
        this.twitter.filter(query);
    }

    /* ---------------------------------------------------------------------- */


}

/* -------------------------------------------------------------------------- */
