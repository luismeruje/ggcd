/* -------------------------------------------------------------------------- */

package twitter.fetcher;

/* -------------------------------------------------------------------------- */

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import twitter4j.*;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/* -------------------------------------------------------------------------- */

/**
 * The Fetcher class is responsible for the consumption of the twitter stream
 * based on a combination of countries an languages.
 *
 * @author Ângela Amorim
 * @author Fábio Fontes
 * @author Luís Ferreira
 */
public class Fetcher
{
    private TwitterStream twitter;
    private RawListener listener;

    /* ---------------------------------------------------------------------- */

    class RawListener implements RawStreamListener {

        private Integer counter;

        private Thread statsT;

        /* ------------------------------------------------------------------ */

        RawListener(final long printInterval, final String filename)
        {
            this.counter = 0;

            this.statsT = new Thread(
                    () -> printStats(printInterval, filename)
            );
            this.statsT.start();
        }

        /* ------------------------------------------------------------------ */

        @Override
        public void onMessage(String str)
        {
            synchronized (counter)
            {
                counter++;
            }
        }

        @Override
        public void onException(Exception e)
        {
            e.printStackTrace();
        }

        /* ------------------------------------------------------------------ */

        public void printStats(final long printInterval, final String filename)
        {
            PrintWriter writer = null;
            try {

                int c = 0, tmp = 0;
                long printIntervalMin =
                        TimeUnit.MILLISECONDS.toMinutes(printInterval);


                if (filename != null)
                    writer = new PrintWriter(new FileOutputStream(filename,
                            true),
                            true);
                else
                    writer = new PrintWriter(System.out, true);

                writer.println("Begin");
                writer.println(printIntervalMin);

                while(true)
                {
                    Thread.sleep(printInterval);

                    synchronized (counter)
                    {
                        tmp = counter;
                    }

                    float result = ((float) (tmp - c)) / printIntervalMin;
                    writer.println(result);
                    c = tmp;
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

        public void stop()
        {
            try
            {
                this.statsT.interrupt();
                this.statsT.join();
            }
            catch(InterruptedException e)
            {
            }
        }

    }

    /* ---------------------------------------------------------------------- */

    public Fetcher(OAuthAuthorization auth,
                   long printInterval,
                   String filename)
    {
        try
        {
            this.twitter = new TwitterStreamFactory().getInstance(auth);
            this.listener = new RawListener(printInterval, filename);

            this.twitter.addListener(this.listener);
        }
        catch(Exception e)
        {
        }
    }

    /* ---------------------------------------------------------------------- */

    public static FilterQuery getQuery(
            Set<String> countries, Set<String> languages)
    {
        FilterQuery query = new FilterQuery();

        double[] southwestcornerPT = {-9.52657060387, 36.838268541};
        double[] northeastcornerPT = {-6.3890876937, 42.280468655};

        double[] southwestcornerUS = {-171.791110603, 18.91619};
        double[] northeastcornerUS = {-66.96466, 71.3577635769};

        double[] southwestcornerES= {-9.39288367353, 35.946850084};
        double[] northeastcornerES = {3.03948408368, 43.7483377142};

        double[] southwestcornerUK = {-7.57216793459, 49.959999905};
        double[] northeastcornerUK = {1.68153079591, 58.6350001085};

        double[] southwestcornerFR = {-54.5247541978, 2.05338918702};
        double[] northeastcornerFR = {9.56001631027, 51.1485061713};

        double[] southwestcornerBR = {-73.9872354804, -33.7683777809};
        double[] northeastcornerBR = {-34.7299934555, 5.24448639569};

        double[] southwestcornerWD = {-180, -90};
        double[] northeastcornerWD = {180, 90};

        for(String country : countries)
        {
            switch (country)
            {
                case "pt":
                    query.locations(southwestcornerPT, northeastcornerPT);
                    break;
                case "us":
                    query.locations(southwestcornerUS, northeastcornerUS);
                    break;
                case "es":
                    query.locations(southwestcornerES, northeastcornerES);
                    break;
                case "uk":
                    query.locations(southwestcornerUK, northeastcornerUK);
                    break;
                case "fr":
                    query.locations(southwestcornerFR, northeastcornerFR);
                    break;
                case "br":
                    query.locations(southwestcornerBR, northeastcornerBR);
            }
        }

        for(String lang : languages)
        {
            query.language(lang);
        }

        return query;
    }

    /* -------------------------------------------------------------------------- */

    public void start(FilterQuery query)
    {
        this.twitter.filter(query);
    }

    public void stop()
    {
        this.listener.stop();
        this.twitter.shutdown();
    }

}

/* -------------------------------------------------------------------------- */



/* -------------------------------------------------------------------------- */
