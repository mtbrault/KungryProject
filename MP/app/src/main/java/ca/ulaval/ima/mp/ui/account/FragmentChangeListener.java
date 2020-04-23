package ca.ulaval.ima.mp.ui.account;

import androidx.fragment.app.Fragment;

import okhttp3.Callback;

public interface FragmentChangeListener {
    void replaceFragment(Fragment fragment);
    void destroyFragment(Callback fragment);
}
