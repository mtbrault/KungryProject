package ca.ulaval.ima.mp.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API extends OkHttpClient {
    private static final API ourInstance = new API();
    private static boolean connected = false;
    private String access_token;
    private String refresh_token;
    private String CLIENT_ID = "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0";
    private String CLIENT_SECRET = "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK";
    public static boolean isConnected() {
        return connected;
    }

    public static API getInstance() {
        return ourInstance;
    }

    private String URL = "https://kungry.ca/api/v1";
    private API() {
        super();
    }

    public void getRestaurants(Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant")
                .build();

        newCall(request).enqueue(callback);
    }

    public void getRestaurant(String id, Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant/" + id)
                .build();

        newCall(request).enqueue(callback);
    }

    public void getRestaurantReviews(String id, Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant/" + id + "/reviews")
                .build();

        newCall(request).enqueue(callback);
    }

    public void createAccount(JSONObject body, final Callback callback) throws JSONException {
        body.put("client_id", CLIENT_ID);
        body.put("client_secret", CLIENT_SECRET);
        MediaType JSON = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(JSON, body.toString());
        final Request request = new Request.Builder()
                .url(URL + "/account")
                .post(requestBody)
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    connected = true;
                    try {
                        JSONObject responseBody = new JSONObject(response.body().string());
                        access_token = "Basic " + responseBody.getJSONObject("content").getString("access_token");
                        refresh_token = responseBody.getJSONObject("content").getString("refresh_token");
                    } catch (JSONException err) {

                    }
                }
                callback.onResponse(call, response);
            }
        });
    }

    public void login(JSONObject body, final Callback callback) throws JSONException {
        body.put("client_id", CLIENT_ID);
        body.put("client_secret", CLIENT_SECRET);
        MediaType JSON = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(JSON, body.toString());
        final Request request = new Request.Builder()
                .url(URL + "/account/login")
                .post(requestBody)
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    connected = true;
                    try {
                        JSONObject responseBody = new JSONObject(response.body().string());
                        access_token = "Basic " + responseBody.getJSONObject("content").getString("access_token");
                        refresh_token = responseBody.getJSONObject("content").getString("refresh_token");
                    } catch (JSONException err) {

                    }
                }
                callback.onResponse(call, response);
            }
        });
    }


}
