package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {
    private LatLng userLocation;
    private SearchAdapter adapter;
    private List<School> schoolList;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
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

            for (Map.Entry<String, LatLng> entry : latLngList.entrySet()){
                googleMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey()));
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng value : nearbyList.values()) {
                builder.include(value);
            }
            LatLngBounds bounds = builder.build();

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
            //googleMap.animateCamera(CameraUpdateFactory.zoomIn());

            googleMap.setLatLngBoundsForCameraTarget(sgBounds);

            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
        }

    };

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
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String address = userProfile.getAddress();
                    userLocation = MapController.getLocationFromAddress(view.getContext(), address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        reference.child(userID).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                return;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String address = userProfile.getAddress();
                    userLocation = MapController.getLocationFromAddress(view.getContext(), address);
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