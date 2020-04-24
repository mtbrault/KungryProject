package ca.ulaval.ima.mp.ui.account;

import android.os.Bundle;
import android.util.Log;
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

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.Restaurant;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterFragment extends Fragment {


    FragmentChangeListener fc;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText name = root.findViewById(R.id.name);
        final EditText lastName = root.findViewById(R.id.lastname);
        final EditText email = root.findViewById(R.id.email);
        final EditText password = root.findViewById(R.id.password);
        final TextView connect = root.findViewById(R.id.connect);
        final Button loginBtn = root.findViewById(R.id.loginBtn);


        connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("onClick", "LoginFragment");
                LoginFragment loginFragment = new LoginFragment();
                fc = (FragmentChangeListener)getActivity();
                fc.replaceFragment(loginFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                JSONObject user = new JSONObject();
                try {
                    user.put("first_name", name.getText().toString());
                    user.put("last_name", lastName.getText().toString());
                    user.put("email", email.getText().toString());
                    user.put("password", password.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("UserDebug", String.valueOf(user));
                try {
                    API.getInstance().createAccount(user, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                AccountFragment accountFragment = new AccountFragment();
                                FragmentChangeListener fc =(FragmentChangeListener)getActivity();
                                fc.replaceFragment(accountFragment);
                            } else {
                                displayMessage("Failed to register.");
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
