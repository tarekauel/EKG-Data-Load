package infoprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This Provider can connect to an http resource like wikipedia.org
 */
public class HttpProvider extends InfoProvider {
    /**
     * will set an baseUri as rootpath to the api or Whatever.
     * This should only help you to focus on creating the queries, and not everytime carrying around the baseuri.
     *
     * @param baseUri
     */
    public HttpProvider(String baseUri) {
        super(baseUri);
    }

    /**
     * Will send a request like:
     * baseUri?query
     *
     * @param query
     * @return returns the response body in case of HTTP StatusCode 2XX.
     * If statuscode not 2XX it will return an empty String.
     */
    @Override
    public String requestResource(String query) {
        String sQuery = query.replace(" ", "%20");
        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(baseUri + sQuery).openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("User-Agent", "Mozilla");
            String contentType = conn.getHeaderField("Content-Type");
            String charset = null;

            if (conn.getResponseCode() > 299) {
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

                return "";
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                StringBuilder sb = new StringBuilder();

                for (String line; (line = reader.readLine()) != null; ) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
