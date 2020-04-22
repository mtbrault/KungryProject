package ca.ulaval.ima.mp.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import ca.ulaval.ima.mp.R;

public class AccountFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onAttach(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String token = sharedPreferences.getString("token", null);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        if (token == null) {
            root = inflater.inflate(R.layout.fragment_login, container, false);
        }
        final TextView textName = root.findViewById(R.id.name);
        final TextView textEmail = root.findViewById(R.id.email);
        final TextView textNumber = root.findViewById(R.id.number);

        return root;
    }
}
