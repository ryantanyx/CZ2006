package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the Maps Boundary to display the map showing user's address and nearby schools
 */
public class MapsFragment extends Fragment {
    /**
     * Location of the user in LatLng
     */
    private LatLng userLocation;
    /**
     * Initialise Search Adapter Controller to retrieve list of schools
     */
    private SearchAdapter adapter;
    /**
     * Stores the list of schools
     */
    private List<School> schoolList;
    /**
     * Callback interface for when the map is ready to be used.
     */
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Preparing the map and controlling the display
         * @param googleMap
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLngBounds sgBounds = new LatLngBounds(
                    new LatLng(1.264850, 103.622483), // SW bounds
                    new LatLng(1.475187, 104.016803)  // NE bounds
            );

            if (userLocation == null) {
                userLocation = sgBounds.getCenter();
            }
            HashMap<String, LatLng> latLngList = MapController.getLatLong(getView().getContext(), schoolList);
            HashMap<String, LatLng> nearbyList = MapController.getLatLong(getView().getContext(), latLngList, userLocation);

            googleMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Current Location"));

            for (Map.Entry<String, LatLng> entry : latLngList.entrySet()){
                googleMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey()));
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng value : nearbyList.values()) {
                builder.include(value);
            }
            LatLngBounds bounds = builder.build();

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25));
            googleMap.setLatLngBoundsForCameraTarget(sgBounds);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
        }
    };

    /**
     * Initialisation of the fragment and inflates the layout of the fragment onto a container
     * @param inflater Inflate the layout of the fragment
     * @param container Container for the layout
     * @param savedInstanceState
     * @return The map fragment page
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        FirebaseUser user;
        DatabaseReference reference;
        String userID;

        adapter = new SearchAdapter(getActivity());
        schoolList = adapter.getSchoolList();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // getting firebase reference
        reference.child(userID).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userAddress = snapshot.getValue(String.class);
                if (userAddress != null){
                    userLocation = MapController.getLocationFromAddress(view.getContext(), userAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        reference.child(userID).child("address").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                return;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userAddress = snapshot.getValue(String.class);
                if (userAddress != null){
                    userLocation = MapController.getLocationFromAddress(view.getContext(), userAddress);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                return;
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

        return view;
    }

    /**
     * Called immediately after onCreateView to initialise Map
     * @param view View
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}