package ca.ulaval.ima.mp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class EvaluationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        getSupportActionBar().hide();
    }


}
