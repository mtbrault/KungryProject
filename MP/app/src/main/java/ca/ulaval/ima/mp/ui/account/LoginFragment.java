package ca.ulaval.ima.mp.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private FragmentChangeListener loggedListenner;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText email = root.findViewById(R.id.email);
        final EditText password = root.findViewById(R.id.password);
        final TextView register = root.findViewById(R.id.register);
        final Button loginBtn = root.findViewById(R.id.loginBtn);
        loggedListenner = (MainActivity) getActivity();

        if (API.isConnected()) {
            loggedListenner.redirectToAccountFragment();
        }
        
            register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loggedListenner.redirectToRegisterFragment();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                JSONObject user = new JSONObject();
                try {
                    user.put("email", email.getText().toString());
                    user.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    API.getInstance().login(user, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            displayMessage("Failed to login.");
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                loggedListenner.redirectToAccountFragment();
                            } else {
                                displayMessage("Failed to login.");
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void displayMessage(String message) {
        Snackbar.make(root, message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
