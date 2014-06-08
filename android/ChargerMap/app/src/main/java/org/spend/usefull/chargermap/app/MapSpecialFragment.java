package org.spend.usefull.chargermap.app;

import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareAnswer;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareItem;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.Answer;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.ChargerPointDTO;
import org.spend.usefull.chargermap.app.rest.ChargerPointRest;
import org.spend.usefull.chargermap.app.rest.FoursquareRest;
import org.spend.usefull.chargermap.app.service.LocationManager;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EFragment(R.layout.fragment_map)
public class MapSpecialFragment extends Fragment {
    private GoogleMap map;
    protected List<Marker> markers = new LinkedList<Marker>();

    @Bean
    protected LocationManager locationManager;

    @RestService
    protected ChargerPointRest chargerPointRest;

    @RestService
    protected FoursquareRest foursquareRest;

    private void setCallbacks() {
        map.setMyLocationEnabled(true);
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                initMapUISettings();
                if (locationManager.isLocationDetected())
                    moveCameraToUser();
            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (!locationManager.isLocationDetected())
                    return;
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                cameraChanged(bounds, cameraPosition.target);
            }
        });
    }

    @Background
    void cameraChanged(LatLngBounds bounds, LatLng center) {
        Answer<List<ChargerPointDTO>> answer;
        try {
            answer = chargerPointRest.list(
                    bounds.northeast.latitude,
                    bounds.southwest.longitude,
                    bounds.southwest.latitude,
                    bounds.northeast.longitude
            );
        } catch (RestClientException exception) {
            showError(exception.getLocalizedMessage());
            answer = new Answer<List<ChargerPointDTO>>();
        }
        FoursquareAnswer foursquareAnswer;
        try {
            foursquareAnswer = foursquareRest.explore(center.latitude, center.longitude);
        } catch (RestClientException exception) {
            showError(exception.getMessage());
            foursquareAnswer = new FoursquareAnswer();
            foursquareAnswer.setGroups(Collections.EMPTY_LIST);
        }
        List<FoursquareItem> foursquareItems = foursquareAnswer.getAllItems();
        merge(answer.getElements(), foursquareItems);
        List<ChargerPointDTO> chargerPointDTOs = merge(answer.getElements(), foursquareItems);
        pickPointsOnMap(chargerPointDTOs);
        if (answer.getError() != null) {
            showError(answer.getError());
        }
    }

    private List<ChargerPointDTO> merge(List<ChargerPointDTO> chargerPoints, List<FoursquareItem> foursquareItems) {
        if (chargerPoints == null) {
            chargerPoints = new LinkedList<ChargerPointDTO>();
        }
        foursquareFor:
        for (Iterator<FoursquareItem> foursquareItem = foursquareItems.iterator(); foursquareItem.hasNext(); ) {
            FoursquareItem item = null;
            for (ChargerPointDTO chargerPoint : chargerPoints) {
                item = foursquareItem.next();
                if (chargerPoint.getFoursquareId() == item.getId()) {
                    foursquareItem.remove();
                    continue foursquareFor;
                }
            }
            chargerPoints.add(new ChargerPointDTO(item));
        }
        return chargerPoints;
    }

    @UiThread
    void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void pickPointsOnMap(List<ChargerPointDTO> chargerPoints) {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers = new LinkedList<Marker>();
        for (ChargerPointDTO chargerPoint : chargerPoints) {
            markers.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng(chargerPoint.getLat(), chargerPoint.getLng()))
                    .title(chargerPoint.getDescription())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))));
        }
        moveCameraToUser();
        moveCameraToSeePosition();
    }

    private void moveCameraToSeePosition() {
        if (markers.size() == 0) {
            return;
        }
        Marker nearestPoint = markers.get(0);
        double nearestPointDistance = 272;
        double tmpPointDistance;
        Location myLocation =  locationManager.getLocation();
        double centerLat = myLocation.getLatitude();
        double centerLng = myLocation.getLongitude();
        for (Marker marker : markers) {
            if ((tmpPointDistance = calculateDifferent(marker, centerLat, centerLng)) < nearestPointDistance) {
                nearestPoint = marker;
                nearestPointDistance = tmpPointDistance;
            }
        }
        while (!see(nearestPoint)) {
            map.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    private boolean see(Marker nearestPoint) {
        LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
        if (nearestPoint.getPosition().latitude > latLngBounds.northeast.latitude || nearestPoint.getPosition().latitude < latLngBounds.southwest.latitude)
            return false;
        if (nearestPoint.getPosition().longitude > latLngBounds.northeast.longitude || nearestPoint.getPosition().longitude < latLngBounds.southwest.longitude)
            return false;
        return true;
    }

    private static double calculateDifferent(Marker marker, double centerLat, double centerLng) {
        return Math.abs(centerLat - marker.getPosition().latitude) + Math.abs(centerLng - marker.getPosition().longitude);
    }

    private void moveCameraToUser() {
        Location location = locationManager.getLocation();
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
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
