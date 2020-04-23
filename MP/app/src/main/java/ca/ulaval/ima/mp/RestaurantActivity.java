package ca.ulaval.ima.mp;

import androidx.appcompat.app.AppCompatActivity;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

public class RestaurantActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private RestaurantActivity that = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_restaurant);
        getSupportActionBar().hide();

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

                        final TextView name = findViewById(R.id.restaurantName);
                        final TextView distance = findViewById(R.id.distance);
                        final TextView reviewCount = findViewById(R.id.reviewCount);
                        final Button phoneNumber = findViewById(R.id.buttonPhone);
                        final Button websiteButton = findViewById(R.id.buttonWebsite);
                        final TextView lundiTextView = findViewById(R.id.lundiHorraire);
                        final TextView mardiTextView = findViewById(R.id.mardiHorraire);
                        final TextView mercrediTextView = findViewById(R.id.mercrediHorraire);
                        final TextView jeudiTextView = findViewById(R.id.jeudiHorraire);
                        final TextView vendrediTextView = findViewById(R.id.vendrediHorraire);
                        final TextView samediTextView = findViewById(R.id.samediHorraire);
                        final TextView dimancheTextView = findViewById(R.id.dimancheHorraire);
                        final ImageView imageRestaurant = findViewById(R.id.imageViewRestaurant);
                        final RatingBar ratingBar = findViewById(R.id.ratingBar2);
                        final Restaurant.OpeningHour monday = restaurant.openingHours.get(Restaurant.MONDAY);
                        final Restaurant.OpeningHour tuesday = restaurant.openingHours.get(Restaurant.TUESDAY);
                        final Restaurant.OpeningHour wednesday = restaurant.openingHours.get(Restaurant.WEDNESDAY);
                        final Restaurant.OpeningHour thursday = restaurant.openingHours.get(Restaurant.THURSDAY);
                        final Restaurant.OpeningHour friday = restaurant.openingHours.get(Restaurant.FRIDAY);
                        final Restaurant.OpeningHour saturday = restaurant.openingHours.get(Restaurant.SATURDAY);
                        final Restaurant.OpeningHour sunday = restaurant.openingHours.get(Restaurant.SUNDAY);
                        TextView viewMoreReviewTextView = findViewById(R.id.seeMoreReview);
                        viewMoreReviewTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(that, ReviewActivity.class);
                                intent.putExtra("idRestaurant", restaurant.id);
                                startActivity(intent);
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(restaurant.name);
                                distance.setText(restaurant.distance);
                                reviewCount.setText("(" + restaurant.reviewCount + ")");
                                phoneNumber.setText(restaurant.phoneNumber);
                                websiteButton.setText(restaurant.website);
                                ratingBar.setRating(restaurant.reviewAverage);
                                Picasso.get().load(restaurant.image).into(imageRestaurant);
                                if (monday != null) {
                                    lundiTextView.setText(monday.opening_hour != "null" ? monday.opening_hour + " à " + monday.closing_hour : "Fermé");
                                    mardiTextView.setText(tuesday.opening_hour != "null" ? tuesday.opening_hour + " à " + tuesday.closing_hour : "Fermé");
                                    mercrediTextView.setText(wednesday.opening_hour != "null" ? wednesday.opening_hour + " à " + wednesday.closing_hour : "Fermé");
                                    jeudiTextView.setText(thursday.opening_hour != "null" ? thursday.opening_hour + " à " + thursday.closing_hour : "Fermé");
                                    vendrediTextView.setText(friday.opening_hour != "null" ? friday.opening_hour + " à " + friday.closing_hour : "Fermé");
                                    samediTextView.setText(saturday.opening_hour != "null" ? saturday.opening_hour + " à " + saturday.closing_hour : "Fermé");
                                    dimancheTextView.setText(sunday.opening_hour != "null" ? sunday.opening_hour + " à " + sunday.closing_hour : "Fermé");
                                }
                                LinearLayout commentsLayout = findViewById(R.id.comments);
                                for(int i = 0; i < 2 && i < restaurant.reviews.size(); i++) {
                                    try {
                                        commentsLayout.addView(new CommentCardViewModel(that, restaurant.reviews.get(i)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                final Button buttonBack = findViewById(R.id.backButton);
                                buttonBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onBackPressed();
                                    }
                                });
                                buttonBack.bringToFront();
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
