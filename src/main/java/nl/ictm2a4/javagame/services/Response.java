package nl.ictm2a4.javagame.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Response {
    public Response(int rc, JSONObject b){
        responseCode = rc;
        body = b;
    }

    public int responseCode;

    public JSONObject body;
}
