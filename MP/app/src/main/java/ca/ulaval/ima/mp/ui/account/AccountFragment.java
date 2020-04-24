package ca.ulaval.ima.mp.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountFragment extends Fragment {

    FragmentChangeListener fc;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        final TextView textName = root.findViewById(R.id.name);
        final TextView textEmail = root.findViewById(R.id.email);
        final TextView textNumber = root.findViewById(R.id.number);
        final Button logout = root.findViewById(R.id.logout);

        if (API.isConnected()) {
            API.getInstance().getMe(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            final JSONObject res = new JSONObject(response.body().string()).getJSONObject("content");
                            final String fullName = res.getString("first_name") + " " + res.getString("last_name");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textName.setText(fullName);
                                    try {
                                        textEmail.setText(res.getString("email"));
                                        textNumber.setText(res.getString("total_review_count"));
                                    } catch (JSONException e) {
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
        } else {
            root = inflater.inflate(R.layout.fragment_login, container, false);
            LoginFragment loginFragment = new LoginFragment();
            FragmentChangeListener fc =(FragmentChangeListener)getActivity();
            Log.d("WTF", "EEEE");
            fc.replaceFragment(loginFragment);
        }

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                API.getInstance().logout();
                AccountFragment registerFragment = new AccountFragment();
                fc = (FragmentChangeListener)getActivity();
                fc.replaceFragment(registerFragment);
            }
        });
        return root;
    }
}
