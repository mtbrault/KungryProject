package ca.ulaval.ima.mp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.RestaurantActivity;
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
    private Marker      markerSelected = null;
    private String      idSelected = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.root.findViewById(R.id.map_info_restaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSelected == null)
                    return ;
                Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                intent.putExtra("id", idSelected);
                startActivity(intent);
            }
        });
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
            final JSONObject res = new JSONObject(response.body().string());
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
                            Marker mark = mMap.addMarker(new MarkerOptions().position(position).icon(getBitmapFromDrawable(getActivity(), R.drawable.icone_pin)));
                            mark.setTag(element);
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (markerSelected == null)
                                    ((ViewSwitcher)root.findViewById(R.id.switcher)).showNext();
                                else
                                    markerSelected.setIcon(getBitmapFromDrawable(getActivity(), R.drawable.icone_pin));
                                marker.setIcon(getBitmapFromDrawable(getActivity(), R.drawable.ic_black_pin));
                                markerSelected = marker;
                                try {
                                    JSONObject element = (JSONObject) marker.getTag();
                                    idSelected = element.getString("id");
                                    ((TextView)root.findViewById(R.id.map_name)).setText(element.getString("name"));
                                    ((TextView)root.findViewById(R.id.map_distance)).setText(element.getString("distance") + " km");
                                    ((TextView)root.findViewById(R.id.map_reviewCount)).setText("(" + element.getString("review_count") + ")");
                                    ((RatingBar)root.findViewById(R.id.map_ratingBar)).setRating((float)element.getDouble("review_average"));
                                    JSONArray jsonCuisineArray = element.getJSONArray("cuisine");
                                    ((TextView)root.findViewById(R.id.map_type)).setText(jsonCuisineArray.getJSONObject(0).getString("name"));
                                    Picasso.get().load(element.getString("image")).into((ImageView)root.findViewById(R.id.map_image));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    displayMessage("Impossible to parse data from restaurant");
                                }
                                return false;
                            }
                        });
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
        for (int page = 0; page < 3; page ++) {
            API.getInstance().getRestaurantFromPosition(page, new Callback() {
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
        }
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
