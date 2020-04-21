package ca.ulaval.ima.mp.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import ca.ulaval.ima.mp.R;

public class AccountFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        final TextView textName = root.findViewById(R.id.name);
        final TextView textEmail = root.findViewById(R.id.email);
        final TextView textNumber = root.findViewById(R.id.number);

        return root;
    }
}
