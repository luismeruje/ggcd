/* -------------------------------------------------------------------------- */

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/* -------------------------------------------------------------------------- */

public class Authentication
{

    private static boolean initCipher(Scanner s,
                                      Cipher c,
                                      int opMode)
    {
        boolean exit = false, initiated = false;

        try
        {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            System.out.println("Insert the password or write /r to go back:");

            while(!exit && s.hasNext())
            {
                String str = s.next();
                if (!str.isEmpty() && !str.equals("/r"))
                {
                    KeySpec spec;
                    char[] passwd;

                    passwd = s.next().toCharArray();
                    spec = new PBEKeySpec(passwd);
                    Arrays.fill(passwd, ' ');

                    try
                    {
                        SecretKey key = factory.generateSecret(spec);
                        c.init(opMode, key);
                        initiated = exit = true;
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid input");
                    }
                }
                else if (str.equals("/r"))
                {
                    exit = true;
                }
                else
                {
                    System.out.println("Invalid input");
                }

                System.out.println("Insert the password or write /r to go back:");
            }


        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return initiated;

    }

    public static void register(Cipher c,
                                Scanner s,
                                String filename)
    {
        boolean exit = false;
        if (initCipher(s, c, Cipher.ENCRYPT_MODE))
        {

            while(!exit)
            {
                String str = s.next();

                System.out.println("Insert the Consumer API key:");


                System.out.println("Insert the Consumer API secret key:");
                System.out.println("Insert the Access Token:");
                System.out.println("Insert the Access Token secret:");
            }

        }
        else
        {
            System.out.println("Registeration unsuccessful");
        }




    }

    public static Configuration authenticate(Cipher c,
                                             Scanner s,
                                             String filename)
    {

        Configuration config = null;

        if (initCipher(s, c, Cipher.DECRYPT_MODE))
        {
            try
            {
                BufferedReader reader =
                        new BufferedReader(new FileReader(filename));

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(new String(c.doFinal(reader.readLine().getBytes())))
                        .setOAuthConsumerSecret(new String(c.doFinal(reader.readLine().getBytes())))
                        .setOAuthAccessToken(new String(c.doFinal(reader.readLine().getBytes())))
                        .setOAuthAccessTokenSecret(new String(c.doFinal(reader.readLine().getBytes())));

                config = cb.build();
            }
            catch(Exception e)
            {
                System.out.println("Authentication unsuccessful");
            }
        }
        else
        {
            System.out.println("Authentication unsuccessful");
        }

        return config;

    }

    private static void printMenu()
    {
        System.out.println("Insert the number of the action you wish to execute:");
        System.out.println("Login - 1");
        System.out.println("Register - 2");
        System.out.println("Quit - 3");
    }

    public static Configuration authentication() throws NoSuchAlgorithmException, NoSuchPaddingException
    {


        Configuration config = null;
        boolean exit = false;
        String filename = "auth.txt";
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        Scanner s = new Scanner(System.in);

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

        return config;

    }
}
