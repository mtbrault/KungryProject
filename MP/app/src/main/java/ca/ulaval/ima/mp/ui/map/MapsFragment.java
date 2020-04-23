package ca.ulaval.ima.mp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.services.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap   mMap;
    private double      myLatitude;
    private double      myLongitude;
    private float       zoomFactor = 10.0f;
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
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 123);
        }
        LocationManager manager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = manager.getProviders(true);
        Location myLocation = null;
        for (String provider : providers) {
            Location locate = manager.getLastKnownLocation(provider);
            if (locate != null) {
                if (myLocation == null || locate.getAccuracy() < myLocation.getAccuracy())
                    myLocation = locate;
            }
        }
        if (myLocation == null) {
            this.displayMessage("Impossible to get your location, so we will locate you in Quebec");
            this.myLatitude = 46.8454;
            this.myLongitude = -71.2908;
        } else {
            this.myLatitude = myLocation.getLatitude();
            this.myLongitude = myLocation.getLongitude();
            API.getInstance().setLatitude(this.myLatitude);
            API.getInstance().setLongitude(this.myLongitude);
        }
    }

    private void addRestaurantToMap(final Response response) throws JSONException {
        try {
            JSONObject res = new JSONObject(response.body().string());
            final JSONObject content = new JSONObject(res.getString("content"));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray restaurantList = content.getJSONArray("results");
                        for (int i = 0; i < restaurantList.length(); i++) {
                            JSONObject element = new JSONObject(restaurantList.getString(i));
                            JSONObject locate = new JSONObject(element.getString("location"));
                            LatLng position = new LatLng(Double.parseDouble(locate.getString("latitude")), Double.parseDouble(locate.getString("longitude")));
                            mMap.addMarker(new MarkerOptions().position(position).icon(getBitmapFromDrawable(getActivity(), R.drawable.icone_pin)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        displayMessage("Impossible to parse JSON");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            displayMessage("Impossible to get restaurants list");
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        this.findLocation();
        API.getInstance().getRestaurantFromPosition(this.myLatitude, this.myLongitude, 40, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                displayMessage("Impossible to get close restaurants");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        addRestaurantToMap(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    displayMessage("Can't get restaurants from API");
                }
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.myLatitude, this.myLongitude), this.zoomFactor));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.findLocation();
            } else {
                this.displayMessage("You need to unable your location permissions.");
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BitmapDescriptor getBitmapFromDrawable(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
