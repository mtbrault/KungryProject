package ca.ulaval.ima.mp.services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
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
    private double longitude = -71.2908;
    private double latitude = 46.8454;
    private int    distance = 80;
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

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void getRestaurants(Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant/search?latitude=" + this.latitude + "&longitude=" + this.longitude + "&radius=" + this.distance)
                .build();
        newCall(request).enqueue(callback);
    }

    public void getMe(Callback callback) {
        MediaType JSON = MediaType.parse("application/json");
        final Request request = new Request.Builder()
                .url(URL + "/account/me")
                .addHeader("Authorization", access_token)
                .build();
        newCall(request).enqueue(callback);
    }

    public void getRestaurants(int page, Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant/search?page=" + page + "&latitude=" + this.latitude + "&longitude=" + this.longitude + "&radius=" + this.distance)
                .build();

        newCall(request).enqueue(callback);
    }

    public void getRestaurantFromPosition(int page, Callback callback) {
        HttpUrl.Builder builder = HttpUrl.parse(URL + "/restaurant/search").newBuilder();

        builder.addQueryParameter("latitude", String.valueOf(this.latitude));
        builder.addQueryParameter("longitude", String.valueOf(this.longitude));
        builder.addQueryParameter("radius", String.valueOf(this.distance));
        builder.addQueryParameter("page", String.valueOf(page));

        final Request request = new Request.Builder()
                .url(builder.build().toString())
                .build();
        newCall(request).enqueue(callback);
    }

    public void getRestaurant(String id, Callback callback) {
        final Request request = new Request.Builder()
                .url(URL + "/restaurant/" + id + "?latitude=" + this.latitude + "&longitude=" + this.longitude)
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
                    final JSONObject responseBody;
                    try {
                        responseBody = new JSONObject(response.body().string());
                        access_token = "Bearer " + responseBody.getJSONObject("content").getString("access_token");
                        refresh_token = responseBody.getJSONObject("content").getString("refresh_token");
                    } catch (JSONException err) {
                    }
                }
                Log.d("ERROR-API", String.valueOf(response));
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
                        access_token = "Bearer " + responseBody.getJSONObject("content").getString("access_token");
                        refresh_token = responseBody.getJSONObject("content").getString("refresh_token");
                    } catch (JSONException err) {
                        Log.d("ERROR-API", String.valueOf(err));
                    }
                }
                callback.onResponse(call, response);
            }
        });
    }

    public void logout() {
        connected = false;
    }

    public void uploadReview(String review, Callback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, review);
        final Request request = new Request.Builder()
                .url(URL + "/review/")
                .post(body)
                .addHeader("Authorization", "Bearer " + this.access_token)
                .build();

        newCall(request).enqueue(callback);
    }
}
