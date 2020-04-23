package ca.ulaval.ima.mp.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.common.api.Api;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.services.API;

public class AccountFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        if (!API.isConnected()) {
            Log.d("API", "Not connected");
            LoginFragment loginFragment = new LoginFragment();
            FragmentChangeListener fc =(FragmentChangeListener)getActivity();
            fc.replaceFragment(loginFragment);
        }
        final TextView textName = root.findViewById(R.id.name);
        final TextView textEmail = root.findViewById(R.id.email);
        final TextView textNumber = root.findViewById(R.id.number);

        return root;
    }
}
