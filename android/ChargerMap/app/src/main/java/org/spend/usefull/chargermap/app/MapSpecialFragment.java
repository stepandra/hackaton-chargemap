package org.spend.usefull.chargermap.app;

import android.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_map)
public class MapSpecialFragment extends Fragment {

    private GoogleMap map;

    protected

    private void setCallbacks() {
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                moveCameraToZoo();
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                initMapUISettings();
                //initOverlay();
                map.setMyLocationEnabled(true);
                for (Animal animal : Animal.values()) {
                    map.addMarker(new MarkerOptions()
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(animal.getMarkerImageId()))
                            .title(animal.getName())
                            .position(new LatLng(animal.getCoordinates()[0], animal.getCoordinates()[1])));
                }
            }
        });
    }

    private void moveCameraToZoo() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kyivZooLatLng, 16));
    }

    @AfterViews
    public void initMap() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        setCallbacks();
    }

    private void initMapUISettings() {
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
    }
}
