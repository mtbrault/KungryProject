package ca.ulaval.ima.mp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import ca.ulaval.ima.mp.dummy.DummyContent;
import ca.ulaval.ima.mp.ui.account.AccountFragment;
import ca.ulaval.ima.mp.ui.account.FragmentChangeListener;
import ca.ulaval.ima.mp.ui.account.LoginFragment;
import ca.ulaval.ima.mp.ui.account.RegisterFragment;
import ca.ulaval.ima.mp.ui.list.ResaurantListFragment;

public class MainActivity extends AppCompatActivity implements ResaurantListFragment.OnListFragmentInteractionListener, FragmentChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                CharSequence label = destination.getLabel();
                System.out.println(label);
                if (label.toString().equals("Profile")) {
                    System.out.println("hiiiiiiiide");
                    getSupportActionBar().hide();
                } else {
                    System.out.println("shoooooooow");
                    getSupportActionBar().show();
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra("id", item.id);
        startActivity(intent);
    }


    @Override
    public void redirectToAccountFragment() {
        AccountFragment fragment = new AccountFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLogin, fragment).commit();
    }

    @Override
    public void redirectToRegisterFragment() {
        RegisterFragment fragment = new RegisterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLogin, fragment).commit();
    }

    @Override
    public void redirectToLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLogin, fragment).commit();
    }

    public void  goToLoginScreen() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.navigation_profile);
        nav_login.setTitle("Login");
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }
}
