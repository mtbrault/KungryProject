package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RestaurantActivity extends AppCompatActivity {

    private Restaurant restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");
        API.getInstance().getRestaurant(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject data = new JSONObject(response.body().string()).getJSONObject("content");
                        restaurant = new Restaurant(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        setContentView(R.layout.activity_restaurant);
    }
}
