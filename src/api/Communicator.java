package api;

/**
 * Created by Lars on 10.11.2014.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Communicator {
    private static Communicator that;
    private final String baseUrl;

    public Communicator(String baseUrl) {
        this.baseUrl = baseUrl;
        that = this;
    }

    public static Communicator getCommunicator() {
        return that;
    }

    public void requestInfo(String title) {
        try {
            String query = "?continue=&action=query&format=json&prop=info&titles=";
            if(title.contains(" ")){
                query = query + title.replaceAll(" ", "%20");
            }else{
                query = query + title;
            }
            System.out.println(sendRequest(query));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String sendRequest(String query) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl + query).openConnection();
        conn.setReadTimeout(5000);
        conn.addRequestProperty("User-Agent", "Mozilla");
        String contentType = conn.getHeaderField("Content-Type");
        String charset = null;
        if(conn.getResponseCode()>299){
            //Missed anything on the connection and received an unexpected http response.
            //Quit processing
            throw new IOException("Did not receive an valid answer");

        }
        //Get the charset
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }
        //parse the answer
        if (charset == null) {
            //Missed anything on the connection and received an unexpected http response.
            //Problem is the encoding, therefor quit processing
            throw new IOException("Did not receive an valid answer");
        }else{
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            StringBuilder sb = new StringBuilder();

            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

}
