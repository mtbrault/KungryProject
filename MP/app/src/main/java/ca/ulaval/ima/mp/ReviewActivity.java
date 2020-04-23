package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

public class ReviewActivity extends AppCompatActivity {

    private ReviewActivity that = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_with_back_button);
        getSupportActionBar().setElevation(0);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String idRestaurant = getIntent().getStringExtra("idRestaurant");
        final LinearLayout layout = findViewById(R.id.reviewsLinearLayout);
        API.getInstance().getRestaurantReviews(idRestaurant, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final JSONObject data = new JSONObject(response.body().string()).getJSONObject("content");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TextView reviewCount = findViewById(R.id.reviewCount);
                                    reviewCount.setText("(" + data.getString("count") + ")");
                                    JSONArray jsonReviews = data.getJSONArray("results");
                                    for (int i = 0; i < jsonReviews.length(); i++) {
                                        layout.addView(new CommentCardViewModel(that, new Restaurant.Review(jsonReviews.getJSONObject(i))));
                                    }
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
