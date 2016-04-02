package fashiome.android.v2.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.Manifest;
import fashiome.android.R;
import fashiome.android.adapters.CustomWindowAdapter;
import fashiome.android.adapters.MapviewAdapter;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.models.Address;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import fashiome.android.v2.activities.PanacheHomeActivity;
import permissions.dispatcher.NeedsPermission;

/**
 * Created by dsaha on 3/30/16.
 */
public class ProductMapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapLongClickListener{


    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private FragmentActivity myContext;
    private ParseQuery<Product> productParseQuery;
    //private MapviewAdapter productAdapter;
    private BannerAdapter bannerAdapter;

    @Bind(R.id.viewpager) ViewPager pager;
    //@Bind(R.id.rvMap) RecyclerView productRecyclerView;

    public void setProductAdapter(BannerAdapter bannerAdapter) {
        //this.productAdapter = productAdapter;
        this.bannerAdapter = bannerAdapter;
    }

    public ArrayList<LatLng> getAllLocations(){
        ArrayList<Product> products = bannerAdapter.mProducts;
        ArrayList<LatLng> points = new ArrayList<>();
        for(Product p: products){
            points.add(p.getAddress().getPoint());
        }
        return points;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i("info","onAttach called");
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_map, container, false);
        ButterKnife.bind(this, v);

        if (bannerAdapter == null) {
            bannerAdapter = new BannerAdapter(getActivity());
        }

        if (pager.getAdapter() == null) {
            pager.setAdapter(bannerAdapter);
        }

        //if (productRecyclerView.getLayoutManager() == null ) {
        //    productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
       // }

        mapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                map = mapFragment.getMap();
                if (map != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap map) {
                            loadMap(map);
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            //map.setInfoWindowAdapter(new CustomWindowAdapter(inflater));
                            drawAllMarkers();
                        }
                    });
                } else {
                    Toast.makeText(myContext, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.mapContainer, mapFragment).commit();
        return v;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        Log.i("info", "onViewCreated called");
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(myContext, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setOnMapLongClickListener(this);

            MapActivityPermissionDispatcher.getMyLocationWithCheck(this, myContext);
        } else {
            Toast.makeText(myContext, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapActivityPermissionDispatcher.onRequestPermissionsResult(this, myContext, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public  void getMyLocation() {
        if (map != null) {
            // Now that map has loaded, let's get our location!
            try {
                map.setMyLocationEnabled(true);
            } catch (SecurityException e){
                e.printStackTrace();
            }
            map.getUiSettings().setMapToolbarEnabled(true);
            mGoogleApiClient = new GoogleApiClient.Builder(myContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(myContext);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, myContext,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(myContext.getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Log.i("info", "received long click");
        // Custom code here...
        showAlertDialogForPoint(latLng);


    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


    // Display the alert that adds the marker
    private void showAlertDialogForPoint(final LatLng point) {
        // inflate message_item.xml view
        View messageView = LayoutInflater.from(myContext).
                inflate(R.layout.message_item, null);
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Define color of marker icon

                        //BitmapDescriptor defaultMarker =
                        //        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

                        BitmapDescriptor customMarker =
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_flag);

                        // Extract content from alert dialog
                        String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).
                                getText().toString();
                        String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).
                                getText().toString();

                        createMarkerAndEffect(point, title, snippet, customMarker);
                    }
                });

        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        // Display the dialog
        alertDialog.show();
    }

    private void moveToLocation(final LatLng latLng, final boolean moveCamera) {

        Log.i("info","moving camera");

        if (latLng == null) {
            return;
        }

        Log.i("info","zooming to first location "+latLng.toString());
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 10.0f)));


/*
        moveMarker(latLng);
        mLocation = latLng;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (map != null && moveCamera) {
                    Log.i("info","zooming to first location "+latLng.toString());
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 5.0f)));
                }
            }
        };
*/

    }

    private void drawAllMarkers() {
        ArrayList<LatLng> points = getAllLocations();
        BitmapDescriptor customMarker =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_flag);

        if (points.size() > 0) {
            moveToLocation(points.get(0), true);
        }

        for (LatLng p : points) {
            createMarkerAndEffect(p, "", "", customMarker);
        }
    }

    private void createMarkerAndEffect(LatLng point, String title, String snippet, BitmapDescriptor customMarker){
        // Creates and adds marker to the map
        Marker marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .icon(customMarker));

        // Animate marker using drop effect
        // --> Call the dropPinEffect method here
        dropPinEffect(marker);

    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

}
