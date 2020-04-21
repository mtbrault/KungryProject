package ca.ulaval.ima.mp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

import ca.ulaval.ima.mp.R;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap   mMap;
    private Location    location;
    private View        root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return this.root;
    }

    private void displayMessage(String message) {
        Snackbar.make(root, message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void findLocation() {
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 123);
        else if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 123);
        }
        else {
            LocationManager manager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = manager.getProviders(true);
            Location myLocation = null;
            for (String provider : providers) {
                Location locate = manager.getLastKnownLocation(provider);
                System.out.println("FUCK1");
                if (locate != null) {
                    System.out.println("FUCK2");
                    if (myLocation == null || locate.getAccuracy() < myLocation.getAccuracy())
                        myLocation = locate;
                }
            }
            this.location = myLocation;
            if (this.location == null)
                this.displayMessage("Impossible to get your location");
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        this.findLocation();
        if (this.location != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.location.getLatitude(), this.location.getLongitude()), 15.0f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.findLocation();
            } else {
                this.displayMessage("You need to unable your location permissions.");
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
