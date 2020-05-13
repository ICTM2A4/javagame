package nl.ictm2a4.javagame.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiService {
    public Response sendRequest(String uri){
        return sendRequest(uri, "GET", "");
    }

    public Response sendRequest(String uri, String method, String body){
        int responseCode;

        try {
            // Verbinding opbouwen
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            // Request sturen en verwerken
            responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            JSONParser parser = new JSONParser();
            Reader rd = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            Object object = parser.parse(rd);
            var responseBody = new JSONObject();

            if(object instanceof JSONArray){
                responseBody.put("Values", object);
            } else if(object instanceof JSONObject) {
                responseBody = (JSONObject) object;
            }

            in.close();
            connection.disconnect();

            // Ombouwen naar Json
            return new Response(responseCode, responseBody);
        } catch (ProtocolException e) {
            System.out.println("Request failed");
            return null;
        } catch (MalformedURLException e) {
            System.out.println("Request failed");
            return null;
        } catch (IOException e) {
            System.out.println("Request failed");
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Als de request faalt, return null
        return null;
    }
}
