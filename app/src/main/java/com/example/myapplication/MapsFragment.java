package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

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
            SearchAdapter adapter = new SearchAdapter(getActivity());
            List<School> schoolList = adapter.getSchoolList();

            LatLng curLocation = new LatLng(1.3048, 103.8318);
            HashMap<String, LatLng> latLngList = MapController.getLatLong(schoolList, curLocation);

            for (School sch : schoolList){
                googleMap.addMarker(new MarkerOptions().position(sch.getLatLng()).title(sch.getSchoolName()));
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng value : latLngList.values()) {
                builder.include(value);
            }
            LatLngBounds bounds = builder.build();

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
            //googleMap.animateCamera(CameraUpdateFactory.zoomIn());

            LatLngBounds sgBounds = new LatLngBounds(
                    new LatLng(1.264850, 103.622483), // SW bounds
                    new LatLng(1.475187, 104.016803)  // NE bounds
            );
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
        return inflater.inflate(R.layout.fragment_maps, container, false);
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