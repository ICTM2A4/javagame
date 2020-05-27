package nl.ictm2a4.javagame.services;

import org.json.simple.JSONObject;

public class Response {

    private int responseCode;
    private JSONObject body;

    public Response(int rc, JSONObject b){
        responseCode = rc;
        body = b;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public JSONObject getBody() {
        return body;
    }
}
