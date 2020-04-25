package ca.ulaval.ima.mp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class EvaluationActivity extends AppCompatActivity {

    private String  id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        getSupportActionBar().hide();
        this.id = getIntent().getStringExtra("id");
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadReview();
            }
        });
    }

    private void uploadReview() {
        double rate = ((RatingBar)findViewById(R.id.review_note)).getRating();
        String comment = ((EditText)findViewById(R.id.review_comment)).getText().toString();
        JSONObject review = new JSONObject();

        if (rate == 0 ) {
            displayMessage("You need to select a rate !");
            return ;
        }
        try {
            review.put("restaurant_id", this.id);
            review.put("stars", rate);
            if (comment.length() > 0)
                review.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
            displayMessage("Problem while reading data");
        }
        API.getInstance().uploadReview(review, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                displayMessage("Impossible to upload review");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                    displayMessage("Review well uploaded !");
                else {
                    displayMessage("Impossible to upload review");
                }
            }
        });
    }

    private void displayMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
