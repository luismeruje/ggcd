package twitter.fetcher;/* -------------------------------------------------------------------------- */

import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.InputMismatchException;
import java.util.Scanner;

/* -------------------------------------------------------------------------- */

/**
 * The Authentication class is responsible the storage and retrieval of the
 * parameters needed for authentication in the twitter stream.
 *
 * @author Ângela Amorim
 * @author Fábio Fontes
 * @author Luís Ferreira
 */
public class Authentication
{

    private static boolean initCipher(String password,
                                      Cipher c,
                                      int opMode,
                                      AlgorithmParameterSpec sp)
    {
        boolean exit = false, initiated = false;

        try
        {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            System.out.println("Insert the password or write /r to go back:");

           // while(!exit && s.hasNext())
            //{
             //   String str = s.next();
              //  if (!str.isEmpty() && !str.equals("/r"))
               // {
                    try
                    {
                        KeySpec spec = new PBEKeySpec(password.toCharArray(),
                                "ggcd1819".getBytes("UTF-8"),
                                65536,
                                256);

                        SecretKey tmp = factory.generateSecret(spec);
                        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

                        c.init(opMode, secret, sp);
                        initiated = exit = true;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input");
                    }
              /*  }
                else if (str.equals("/r"))
                {
                    exit = true;
                }
                else
                {
                    System.out.println("Invalid input");
                }

                if (!exit)
                    System.out.println("Insert the password or write /r to go back:");
                */
            //}
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return initiated;
    }

    private static String getNonEmptyString(String prompted, Scanner s)
    {
        String result = null;
        boolean exit = false;

        System.out.println(prompted);
        while(!exit && s.hasNext())
        {
            String tmp = s.next();
            if (!tmp.isEmpty())
            {
                result = tmp;
                exit = true;
            }

            if (!exit)
                System.out.println(prompted);
        }

        return result;
    }

    /* ---------------------------------------------------------------------- */

    /*public static void register(Cipher c,
                                Scanner s,
                                String filename)
    {
        try {

            DataOutputStream writer = new DataOutputStream(
                    new FileOutputStream(filename, false));

            writer.writeBytes("");

            if (initCipher(s, c, Cipher.ENCRYPT_MODE, null)) {

                AlgorithmParameters params = c.getParameters();
                byte[] iv =
                        params.getParameterSpec(IvParameterSpec.class).getIV();
                writer.writeInt(iv.length);
                writer.write(iv, 0, iv.length);

                byte[] key = c.doFinal(
                        getNonEmptyString(
                                "Insert the Consumer API key:", s)
                        .getBytes("UTF-8"));
                writer.writeInt(key.length);
                writer.write(key, 0, key.length);

                byte[] secretKey = c.doFinal(
                        getNonEmptyString(
                        "Insert the Consumer API secret key:", s)
                        .getBytes("UTF-8"));
                writer.writeInt(secretKey.length);
                writer.write(secretKey, 0, secretKey.length);

                byte[] token = c.doFinal(
                        getNonEmptyString(
                                "Insert the Access Token:", s)
                        .getBytes("UTF-8"));
                writer.writeInt(token.length);
                writer.write(token, 0, token.length);

                byte[] tokenSecret = c.doFinal(
                        getNonEmptyString(
                                "Insert the Access Token secret:", s)
                        .getBytes("UTF-8"));
                writer.writeInt(tokenSecret.length);
                writer.write(tokenSecret, 0, tokenSecret.length);

                writer.flush();
                writer.close();

                System.out.println("Registration successful");
            }
            else
            {
                System.out.println("Registration unsuccessful");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }*/


    public static Configuration authenticate(Cipher c,
                                             String password,
                                             String filename)
    {
        Configuration config = null;

        try
        {
            DataInputStream reader =
                    new DataInputStream(new FileInputStream(filename));

            int ivSize = reader.readInt();
            byte[] iv = new byte[ivSize];
            reader.read(iv);

            if (initCipher(password, c, Cipher.DECRYPT_MODE, new IvParameterSpec(iv)))
            {

                int keySize = reader.readInt();
                byte[] key = new byte[keySize];
                reader.read(key);

                int secretKeySize = reader.readInt();
                byte[] secretKey = new byte[secretKeySize];
                reader.read(secretKey);

                int tokenSize = reader.readInt();
                byte[] token = new byte[tokenSize];
                reader.read(token);

                int tokenSecretSize = reader.readInt();
                byte[] tokenSecret = new byte[tokenSecretSize];
                reader.read(tokenSecret);

                reader.close();

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(new String(
                                c.doFinal(key), "UTF-8"))
                        .setOAuthConsumerSecret(new String(
                                c.doFinal(secretKey), "UTF-8"))
                        .setOAuthAccessToken(new String(
                                c.doFinal(token), "UTF-8"))
                        .setOAuthAccessTokenSecret(new String(
                                c.doFinal(tokenSecret), "UTF-8"));

                config = cb.build();

                System.out.println("Authentication Successful");
            }
            else
            {
                System.out.println("Authentication unsuccessful");
            }
        }
        catch(Exception e)
        {
            System.out.println("Authentication unsuccessful");
        }

        return config;
    }

    /* ---------------------------------------------------------------------- */

    private static void printMenu()
    {
        System.out.println(
                "Insert the number of the action you wish to execute:");
        System.out.println("Login - 1");
        System.out.println("Register - 2");
        System.out.println("Quit - 3");
    }

    public static OAuthAuthorization authentication(String password)
            throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        Configuration config = null;
        OAuthAuthorization auth = null;
        boolean exit = false;
        String filename = "src/main/resources/auth.txt";

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        /*Scanner s = new Scanner(System.in);

        printMenu();

        while(!exit && s.hasNext())
        {
            try
            {
                int o = s.nextInt();

                switch(o)
                {
                    case 1:
                        config = authenticate(c, s, filename);
                        exit = config != null;
                        break;

                    case 2:
                        register(c, s, filename);
                        break;

                    case 3:
                        exit = true;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Invalid option");
            }

            if (!exit)
                printMenu();
        }
        */

        config = authenticate(c, password , filename);


        if (config != null)
            auth = new OAuthAuthorization(config);

        return auth;
    }
}

/* -------------------------------------------------------------------------- */
