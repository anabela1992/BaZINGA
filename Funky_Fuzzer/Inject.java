import java.net.*;
import java.io.*;

public class Inject {
    private String url;

    public Inject(String url){
        this.url = url;
    }

    public void inject() {
        try {
            URL injectable = new URL(url);
            URLConnection yc = injectable.openConnection();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null);
            System.out.println("Injected: " + url);
            in.close();
        } catch(Exception e) {
            System.out.println("There was an error injecting " + url);
        }
    }
}