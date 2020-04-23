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
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

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

    FragmentChangeListener fc;
    private JSONObject data;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        //((MainActivity)getActivity()).getSupportActionBar().getCustomView().setVisibility(View.GONE);
        final EditText email = root.findViewById(R.id.email);
        final EditText password = root.findViewById(R.id.password);
        final TextView register = root.findViewById(R.id.register);
        final Button loginBtn = root.findViewById(R.id.loginBtn);


        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("onClick", "RegisterFragment");
                RegisterFragment registerFragment = new RegisterFragment();
                fc = (FragmentChangeListener)getActivity();
                fc.replaceFragment(registerFragment);
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
                Log.d("UserDebug", String.valueOf(user));
                try {
                    API.getInstance().login(user, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject data = new JSONObject(response.body().string()).getJSONObject("content");
                                    Log.d("data", String.valueOf(data));
                                    fc.destroyFragment(this);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

}
