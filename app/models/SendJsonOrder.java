package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by rmasal on 2015-06-11.
 */
public class SendJsonOrder {
    public static void post(String apiKey, JsonNode jsonNode){
        try{
            // 1. URL
            URL url = new URL("https://secure.payu.com/api/v2_1/orders");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", apiKey);
            connection.setDoOutput(true);
            byte[] data = jsonNode.toString().getBytes(Charset.forName("UTF-8"));
            connection.getOutputStream().write(data);
//            wr.flush();
//            wr.close();
            System.out.println("Output stream : " + connection.getOutputStream());
            int responseCode = connection.getResponseCode();
            String responseMsg = connection.getResponseMessage();
//            System.out.println(wr);
            System.out.println("Sending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response message : " + responseMsg);
//input
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } catch (Exception e) {
            Logger.debug(e.getMessage());
        }
    }
}
