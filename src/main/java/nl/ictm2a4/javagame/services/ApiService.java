package nl.ictm2a4.javagame.services;

import nl.ictm2a4.javagame.screens.GameScreen;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiService {

    protected String baseUrl = "https://javagameapi.rillprogramming.com"; // prod
    //protected String baseUrl = "https://localhost:44320"; // local

    public Response sendRequest(String uri){
        return sendRequest(uri, "GET", "");
    }

    public Response sendRequest(String uri, String method, String body){
        int responseCode = -1;

        try {
            // Verbinding opbouwen
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            String apiToken = GameScreen.getInstance().getApiToken();

            if(apiToken != null && !apiToken.equals("")){
                connection.setRequestProperty("Authorization", "Bearer " + apiToken);
            }

            if(method.equals("POST") || method.equals("PUT")){

                int length = body.getBytes(StandardCharsets.UTF_8).length;

                connection.setRequestProperty("Content-Length", String.valueOf(length));
                connection.setRequestProperty("Content-Type", "application/json");

                    connection.setDoOutput(true);

                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = body.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                }
            }

            // Request sturen en verwerken
            responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            if(responseCode == 204){
                // Niet ombouwen naar JSON, 204 is no content
                return new Response(responseCode, new JSONObject());
            }
            // Ombouwen naar Json

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

            return new Response(responseCode, responseBody);
        } catch (IOException | ParseException e) {
            System.out.println("Request failed: " + e.toString());
        }

        if(responseCode != -1){
            return new Response(responseCode, null);
        }

        return null;
    }
}
