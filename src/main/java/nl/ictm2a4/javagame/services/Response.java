package nl.ictm2a4.javagame.services;

import org.json.simple.JSONObject;

public class Response {

    private int responseCode;
    private JSONObject body;

    /**
     * A response returned by the API service after processing a request.
     *
     * @param rc, The response code, 404, 400, 200, 201 etc.
     * @param b, The response body in JSON format
     */
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
